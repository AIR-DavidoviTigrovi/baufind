namespace DataAccessLayer.Models;

/// <summary>
/// Model za tablicu "User"
/// MODEL: 1 na 1 "preslika" zapisa u bazi, u ovo se deserijalizira iz baze
/// </summary>
public record UserModel
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string Email { get; set; }
    public string Phone { get; set; }
    public string PasswordHash { get; set; }
    public DateTime Joined { get; set; }
    public string Address { get; set; }
    public byte[]? ProfilePicture { get; set; }
    public bool Deleted { get; set; }
    public string? GoogleId { get; set; }
}
