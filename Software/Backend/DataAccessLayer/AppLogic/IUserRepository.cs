using DataAccessLayer.Models;

namespace DataAccessLayer.AppLogic;

/// <summary>
/// Repozitorij za dohvaćanje, dodavanje, brisanje i kreiranje novih korisnika
/// Ova klasa samo POPISUJE metode koje su potrebne
/// </summary>
public interface IUserRepository
{
    /// <summary>
    /// Metoda za dohvat svih korisnika
    /// </summary>
    /// <returns>lista svih korisnika (ako nema onda je prazna)</returns>
    public List<UserModel> GetUsers();

    /// <summary>
    /// Metoda za dohvat jednog korisnika prema ID-u
    /// </summary>
    /// <param name="id">ID korisnika</param>
    /// <returns>korisnika koji ima taj ID ili null</returns>
    public UserModel? GetUser(int id);

    /// <summary>
    /// Metoda za dodavanje novog korisnika u bazu
    /// </summary>
    /// <param name="user">korisnik koji se dodaje</param>
    /// <returns>ID ako je uspješno dodan, u protivnom null</returns>
    public int? AddUser(UserModel user);

    /// <summary>
    /// Metoda za dohvacanje profila korisnika
    /// </summary>
    /// <param name="id"></param>
    /// <returns>UserProfileModel</returns>
    public UserProfileModel GetUserProfile(int id);

    /// <summary>
    /// Metoda za ažuriranje profila
    /// </summary>
    /// <param name="user"></param>
    /// <returns></returns>
    public string? UpdateUserProfile(UserProfileUpdateModel user);

    /// <summary>
    /// Metoda za dodavanje skillova povezanih s nekim korisnikom
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="addSkills"></param>
    void AddUserSkills(int userId, List<int> addSkills);
    /// <summary>
    /// Uklanja sve skillove povezane s nekim korisnikom
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="removeSkills"></param>
    void RemoveUserSkills(int userId, List<int> removeSkills);

    /// <summary>
    /// Metoda koja izvodi brisanje korirsnika u bazi (samo postavlja deleted na 1 u app_user)
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    public bool DeleteUser(int userId);

    /// <summary>
    /// Doda token vezan uz Firebase notifikacije na korisnika i makne isti ako postoji kod nekog drugog korisnika
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="token"></param>
    /// <returns></returns>
    public bool AddUserToken(int userId, string token);

    /// <summary>
    /// Uklanja Firebase token korisnika (kod odjave)
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    public bool RemoveUserToken(int userId);

    /// <summary>
    /// Dohvaća token za korisnika (ako postoji u bazi)
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    public string? GetUserToken(int userId);
}
