namespace BusinessLogicLayer.AppLogic.Users;

/// <summary>
/// Ovo je odgovor koji se šalje preko API zahtjeva za dobivanje svih korisnika
/// </summary>
public class GetAllUsersResponse
{
    public List<UserRecord> Users { get; set; } = new List<UserRecord>();

    public string? Error { get; set; } = string.Empty;
}
