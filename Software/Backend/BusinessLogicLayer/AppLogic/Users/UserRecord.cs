namespace BusinessLogicLayer.AppLogic.Users;

/// <summary>
/// Record sa korisnika koji se vraća na API putanjama
/// Ovo ne mora biti 1 na 1 sa modelom u bazi, nego ovisi o API pozivu, što se šalje i što se prima!
/// </summary>
public record UserRecord
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string Email { get; set; }
    public string Phone { get; set; }
    public DateTime Joined { get; set; }
    public string Address { get; set; }
    public byte[]? ProfilePicture { get; set; }
    public bool Deleted { get; set; }
}
