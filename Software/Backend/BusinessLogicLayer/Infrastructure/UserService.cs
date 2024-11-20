using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users.GetUser;
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
    /// <param name="command">podaci za registraciju</param>
    /// <returns>korisnik i poruka uspjeha ako je registracija uspješna, u protivnom poruka greške</returns>
    public RegisterUserResponse RegisterUser(RegisterUserRequest command)
    {
        var errors = ValidateUserRegistration(command);
        if (errors.Any())
        {
            return new RegisterUserResponse()
            {
                Error = errors.First()
            };
        }

        UserModel newUser = new UserModel()
        {
            Name = command.Name,
            Email = command.Email,
            Phone = command.Phone,
            PasswordHash = ComputePasswordHash(command.Password),
            Joined = DateTime.Now,
            Address = command.Address,
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

    // TODO: eventualno zamijeniti s FluentValidation ili nečim boljim
    /// <summary>
    /// Metoda koja validira poslane korisničke podatke
    /// Ove validacije bi trebale sve proći, jer na frontendu trebaju postojati iste, no ako se slučajno zaobiđu, ovdje također moraju biti
    /// </summary>
    /// <param name="command">proslijeđena iz registracije</param>
    /// <returns>praznu listu, ili listu grešaka</returns>
    private List<string> ValidateUserRegistration(RegisterUserRequest command)
    {
        List<string> errors = new List<string>();
        
        if (command.Password != command.RepeatPassword)
        {
            errors.Add("Lozinke se ne podudaraju");
        }

        if (string.IsNullOrWhiteSpace(command.Password))
        {
            errors.Add("Lozinka je obavezna");
        }

        if (string.IsNullOrWhiteSpace(command.Name))
        {
            errors.Add("Ime je obavezno");
        } else if (command.Name.Length > 100)
        {
            errors.Add("Korisničko ime ne smije prelaziti 100 znakova");
        }

        if (string.IsNullOrWhiteSpace(command.Address))
        {
            errors.Add("Adresa je obavezna");
        } else if (command.Address.Length > 100)
        {
            errors.Add("Adresa ne smije prelaziti 100 znakova");
        }

        if (string.IsNullOrWhiteSpace(command.Phone))
        {
            errors.Add("Broj telefona je obavezan");
        }
        else if (command.Phone.Length > 100)
        {
            errors.Add("Broj telefona ne smije prelaziti 100 znakova");
        }

        if (string.IsNullOrWhiteSpace(command.Email))
        {
            errors.Add("Email mora biti unesen");
        } else
        {
            string pattern = @"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
            bool isValid = Regex.IsMatch(command.Email, pattern);
            if (!isValid || command.Email.Length > 100)
            {
                errors.Add("Unesite validan email");
            } else
            {
                var users = _repository.GetUsers();
                var usersWithEmail = users.Where(x => x.Email == command.Email).ToList();
                if (usersWithEmail.Any())
                {
                    errors.Add("Email adresa već postoji");
                }
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
