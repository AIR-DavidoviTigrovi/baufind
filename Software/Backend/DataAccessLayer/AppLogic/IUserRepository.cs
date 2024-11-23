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

    public UserProfileModel GetUserProfile(int id);
}
