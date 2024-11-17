namespace BusinessLogicLayer.AppLogic.Users;

/// <summary>
/// Ovo je odgovor koji se šalje preko API zahtjeva za dobivanje jednog korisnika
/// </summary>
public class GetUserResponse
{
    public UserRecord? User { get; set; } = null;
    public string? Error { get; set; } = string.Empty;
}
