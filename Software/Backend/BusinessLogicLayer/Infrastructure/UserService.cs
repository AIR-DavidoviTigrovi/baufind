using BusinessLogicLayer.AppLogic.Users;
using DataAccessLayer.AppLogic;

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
}
