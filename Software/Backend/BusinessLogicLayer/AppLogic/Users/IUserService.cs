using BusinessLogicLayer.AppLogic.Users.DeleteUser;
using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using BusinessLogicLayer.AppLogic.Users.GetUserProfile;
using BusinessLogicLayer.AppLogic.Users.Login;
using BusinessLogicLayer.AppLogic.Users.Logout;
using BusinessLogicLayer.AppLogic.Users.RegisterUser;
using BusinessLogicLayer.AppLogic.Users.UpdateUserProfile;

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
    /// Metoda za dohvat podataka povezane s profilom nekog korisnika
    /// </summary>
    /// <param name="id"></param>
    /// <returns>Vraća podatke tj userProfileModel ili se vraća poruka greške</returns>
    public UserProfileResponse GetUserProfileData(int id);

    /// <summary>
    /// Metoda za registriranje novog korisnika
    /// </summary>
    /// <param name="request">podaci za registraciju</param>
    /// <returns>poruka uspjeha ako je registracija uspješna, u protivnom poruka greške</returns>
    public RegisterUserResponse RegisterUser(RegisterUserRequest request);

    /// <summary>
    /// Metoda za prijavu postojećeg korisnika
    /// </summary>
    /// <param name="request">podaci za prijavu</param>
    /// <returns>token ako je uspješno registriran, a ako ne, onda poruku greške</returns>
    /// 
    public UpdateUserResponse UpdateUser(UpdateUserRequest request);
    public LoginResponse Login(LoginRequest request);
    public LogoutResponse Logout(int userId);
    public DeleteUserResponse DeleteUser(int id);
}
