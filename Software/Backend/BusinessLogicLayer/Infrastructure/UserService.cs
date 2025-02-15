﻿using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.PushNotifications;
using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.AppLogic.Users.DeleteUser;
using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using BusinessLogicLayer.AppLogic.Users.GetUserProfile;
using BusinessLogicLayer.AppLogic.Users.Login;
using BusinessLogicLayer.AppLogic.Users.Logout;
using BusinessLogicLayer.AppLogic.Users.RegisterUser;
using BusinessLogicLayer.AppLogic.Users.UpdateUserProfile;
using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using FluentValidation;
using System.Security.Cryptography;
using System.Text;

namespace BusinessLogicLayer.Infrastructure;

/// <summary>
/// Servis za radnje s korisnicima
/// Konkretna implementacija sučelja IUserService
/// </summary>
public class UserService : IUserService
{
    private readonly IUserRepository _repository;
    private readonly IJwtService _jwtService;

    public UserService(IUserRepository repository, IJwtService jwtService)
    {
        _repository = repository;
        _jwtService = jwtService;
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
        };
    }

    /// <summary>
    /// Metoda za prijavu postojećeg korisnika
    /// </summary>
    /// <param name="request">podaci za prijavu</param>
    /// <returns>token ako je uspješno registriran, a ako ne, onda poruku greške</returns>
    public LoginResponse Login(LoginRequest request)
    {
        var validator = new LoginValidator();
        var result = validator.Validate(request);
        if (!result.IsValid)
        {
            return new LoginResponse()
            {
                Error = string.Join(Environment.NewLine, result.Errors.Select(e => e.ErrorMessage))
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

        var newUser = new UserRecord()
        {
            Id = user.Id,
            Email = user.Email
        };

        if (request.FirebaseToken != null)
        {
            _repository.AddUserToken(user.Id, request.FirebaseToken);
        }

        return new LoginResponse()
        {
            JWT = _jwtService.GenerateToken(newUser),
            Success = $"Korisnik {user.Name} uspješno je prijavljen u sustav."
        };
    }

    /// <summary>
    /// Metoda za odjavu korisnika.
    /// Samo briše postojeći Firebase token.
    /// </summary>
    /// <param name="userId">korisnikov ID</param>
    /// <returns></returns>
    /// <exception cref="NotImplementedException"></exception>
    public LogoutResponse Logout(int userId)
    {
        var result = _repository.RemoveUserToken(userId);
        if (result)
        {
            return new LogoutResponse()
            {
                Success = "Token uspješno uklonjen."
            };
        } else
        {
            return new LogoutResponse()
            {
                Error = "Greška kod uklanjanja tokena."
            };
        }
    }

    /// <summary>
    /// Metoda za dohvat podataka povezane s profilom nekog korisnika
    /// </summary>
    /// <param name="id"></param>
    /// <returns>Vraća podatke tj userProfileModel ili se vraća poruka greške</returns>
    public UserProfileResponse GetUserProfileData(int id)
    {
        var userProfile = _repository.GetUserProfile(id);
        if (userProfile == null)
        {
            return new UserProfileResponse()
            {
                Error = "Korisnik s tim ID ne postoji"
            };
        }
        return new UserProfileResponse()
        {
            userProfileModel = userProfile
        };
    }

    /// <summary>
    /// Metoda koja se koristi za azuriranje korisnika.
    /// </summary>
    /// <param name="request"></param>
    /// <returns>Vraca updateuserResponse</returns>
    public UpdateUserResponse UpdateUser(UpdateUserRequest request) 
    {
        var validator = new UpdateUserValidator();
        var validationResult = validator.Validate(request);

        if (!validationResult.IsValid)
        {
            return new UpdateUserResponse
            {
                Success = false,
                Errors = validationResult.Errors.Select(e => e.ErrorMessage).ToList()
            };
        }
        byte[]? profilePictureBytes = null;
        if (!string.IsNullOrEmpty(request.ProfilePicture))
        {
            try
            {
                profilePictureBytes = Convert.FromBase64String(request.ProfilePicture);
            }
            catch (FormatException ex)
            {
                return new UpdateUserResponse
                {
                    Success = false,
                    Errors = ["Invalid Base64 string for profile picture."]
                };
            }
        }

        var updateModel = new UserProfileUpdateModel
        {
            UserId = request.UserId,
            Name = request.Name,
            Address = request.Address,
            Phone = request.Phone,
            ProfilePicture = profilePictureBytes
        };

        var result = _repository.UpdateUserProfile(updateModel);

        if (request.AddSkills != null && request.AddSkills.Count != 0)
        {
            _repository.AddUserSkills(request.UserId, request.AddSkills);
        }

        if (request.RemoveSkills != null && request.RemoveSkills.Count != 0)
        {
            _repository.RemoveUserSkills(request.UserId, request.RemoveSkills);
        }

        return new UpdateUserResponse
        {
            Success = true,
            Message = "Profile and skills updated successfully."
        };
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
    /// <summary>
    /// Metoda za brisanje korisnika, zahtjeva ID kao argument
    /// </summary>
    /// <param name="id"></param>
    /// <returns></returns>
    public DeleteUserResponse DeleteUser(int id)
    {
        bool deleted = _repository.DeleteUser(id);
        if (!deleted)
        {
            return new DeleteUserResponse { 
                Message="Greška prilikom brisanja korisnika", 
                Success = false 
            };
        }
        return new DeleteUserResponse {
            Message="Korisnik je uspješno obrisan",
            Success=true 
        };

    }
}
