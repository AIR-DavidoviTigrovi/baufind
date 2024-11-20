namespace BusinessLogicLayer.AppLogic.Users.GetAllUsers;

/// <summary>
/// Ovo je odgovor koji se šalje preko API zahtjeva za dobivanje svih korisnika
/// </summary>
public record GetAllUsersResponse
{
    public List<UserRecord> Users { get; set; } = new List<UserRecord>();

    public string? Error { get; set; } = string.Empty;
}
