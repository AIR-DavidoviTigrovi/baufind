using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using BusinessLogicLayer.AppLogic.Users.RegisterUser;

namespace BusinessLogicLayer.AppLogic.Users;

/// <summary>
/// Servis za radnje s korisnicima
/// Koristi jednostavne metode repozitorija za implementaciju kompleksnije logike
/// </summary>
public interface IUserService
{
    /// <summary>
    /// Metoda za dobivanje svih korisnika
    /// </summary>
    /// <returns>odgovor koji sadrži listu korisnika ili informacije o grešci</returns>
    public GetAllUsersResponse GetAllUsers();

    /// <summary>
    /// Metoda za dobivanje podataka jednog korisnika
    /// </summary>
    /// <param name="id">ID korisnika</param>
    /// <returns>podatke korisnika ili informacije o grešci</returns>
    public GetUserResponse GetOneUser(int id);

    /// <summary>
    /// Metoda za registriranje novog korisnika
    /// </summary>
    /// <param name="command">podaci za registraciju</param>
    /// <returns>korisnik i poruka uspjeha ako je registracija uspješna, u protivnom poruka greške</returns>
    public RegisterUserResponse RegisterUser(RegisterUserRequest command);
}
