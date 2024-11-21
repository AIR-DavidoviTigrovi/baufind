using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using BusinessLogicLayer.AppLogic.Users.Login;
using BusinessLogicLayer.AppLogic.Users.RegisterUser;
using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;

namespace BusinessLogicLayer.Infrastructure;

/// <summary>
/// Servis za radnje s korisnicima
/// Konkretna implementacija sučelja IUserService
/// </summary>
public class UserService : IUserService
{
    private readonly IUserRepository _repository;

    public UserService(IUserRepository repository)
    {
        _repository = repository;
    }

    /// <summary>
    /// Metoda za dobivanje svih korisnika
    /// </summary>
    /// <returns>odgovor koji sadrži listu korisnika ili informacije o grešci</returns>
    public GetAllUsersResponse GetAllUsers()
    {
        var query = _repository.GetUsers();

        if (query == null || !query.Any())
        {
            return new GetAllUsersResponse()
            {
                Error = "U bazi nema korisnika!"
            };
        }

        var users = query.Select(x => new UserRecord()
        {
            Id = x.Id,
            Name = x.Name,
            Email = x.Email,
            Phone = x.Phone,
            Joined = x.Joined,
            Address = x.Address,
            ProfilePicture = x.ProfilePicture,
            Deleted = x.Deleted
        }).ToList();

        return new GetAllUsersResponse()
        {
            Users = users
        };
    }

    /// <summary>
    /// Metoda za dobivanje podataka jednog korisnika
    /// </summary>
    /// <returns>podatke korisnika ili informacije o grešci</returns>
    public GetUserResponse GetOneUser(int id)
    {
        var query = _repository.GetUser(id);

        if (query == null)
        {
            return new GetUserResponse()
            {
                Error = "Korisnik s tim ID ne postoji!"
            };
        }

        var user = new UserRecord()
        {
            Id = query.Id,
            Name = query.Name,
            Email = query.Email,
            Phone = query.Phone,
            Joined = query.Joined,
            Address = query.Address,
            ProfilePicture = query.ProfilePicture,
            Deleted = query.Deleted
        };

        return new GetUserResponse()
        {
            User = user
        };
    }

    /// <summary>
    /// Metoda za registriranje novog korisnika
    /// </summary>
    /// <param name="request">podaci za registraciju</param>
    /// <returns>poruka uspjeha ako je registracija uspješna, u protivnom poruka greške</returns>
    public RegisterUserResponse RegisterUser(RegisterUserRequest request)
    {
        var validator = new RegisterUserValidator(_repository);
        var result = validator.Validate(request);
        if (!result.IsValid)
        {
            return new RegisterUserResponse()
            {
                Error = string.Join(Environment.NewLine, result.Errors.Select(e => e.ErrorMessage))
            };
        }

        UserModel newUser = new UserModel()
        {
            Name = request.Name,
            Email = request.Email,
            Phone = request.Phone,
            PasswordHash = ComputePasswordHash(request.Password),
            Joined = DateTime.Now,
            Address = request.Address,
            Deleted = false
        };

        int? id = _repository.AddUser(newUser);
        if (id == null)
        {
            return new RegisterUserResponse()
            {
                Error = "Greška kod kreiranja korisnika"
            };
        }

        var user = _repository.GetUser(id.Value);

        return new RegisterUserResponse()
        {
            Success = $"Korisnik s e mail adresom {user!.Email} uspješno je registriran.",
            User = new UserRecord()
            {
                Id = user.Id,
                Name = user.Name,
                Email = user.Email,
                Phone = user.Phone,
                Joined = user.Joined,
                Address = user.Address,
                ProfilePicture = user.ProfilePicture,
                Deleted = user.Deleted
            }
        };
    }

    /// <summary>
    /// Metoda za prijavu postojećeg korisnika
    /// </summary>
    /// <param name="request">podaci za prijavu</param>
    /// <returns>token ako je uspješno registriran, a ako ne, onda poruku greške</returns>
    public LoginResponse Login(LoginRequest request)
    {
        var errors = ValidateLogin(request);
        if (errors.Any())
        {
            return new LoginResponse()
            {
                Error = errors.First()
            };
        }

        var users = _repository.GetUsers();
        var user = users.Where(x => x.Email == request.Email).FirstOrDefault();

        if (user == null)
        {
            return new LoginResponse()
            {
                Error = "Korisnik s tom e-mail adresom ne postoji!"
            };
        }

        var hashedPassword = ComputePasswordHash(request.Password);
        if (user.PasswordHash != hashedPassword)
        {
            return new LoginResponse()
            {
                Error = "Neispravna lozinka."
            };
        }

        return new LoginResponse()
        {
            JWT = "", // TODO: izračunaj JWT
            Success = $"Korisnik {user.Name} uspješno je prijavljen u sustav."
        };
    }

    // TODO: eventualno zamijeniti s FluentValidation ili nečim boljim
    /// <summary>
    /// Metoda koja validira podatke za prijavu
    /// </summary>
    /// <param name="request">proslijeđena iz prijave</param>
    /// <returns>praznu listu, ili listu grešaka</returns>
    private List<string> ValidateLogin(LoginRequest request)
    {
        List<string> errors = new List<string>();

        if (string.IsNullOrWhiteSpace(request.Password))
        {
            errors.Add("Lozinka je obavezna");
        }

        if (string.IsNullOrWhiteSpace(request.Email))
        {
            errors.Add("Email mora biti unesen");
        }
        else
        {
            string pattern = @"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
            bool isValid = Regex.IsMatch(request.Email, pattern);
            if (!isValid || request.Email.Length > 100)
            {
                errors.Add("Unesite validan email");
            }
        }

        return errors;
    }

    private string ComputePasswordHash(string password)
    {
        byte[] passwordBytes = Encoding.UTF8.GetBytes(password);

        using (SHA512 sha512 = SHA512.Create())
        {
            byte[] hashBytes = sha512.ComputeHash(passwordBytes);
            StringBuilder hash = new StringBuilder(hashBytes.Length * 2);

            foreach (byte b in hashBytes)
            {
                hash.AppendFormat("{0:x2}", b);
            }

            return hash.ToString();
        }
    }
}
