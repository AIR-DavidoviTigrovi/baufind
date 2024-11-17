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
}
