namespace BusinessLogicLayer.AppLogic.Users.RegisterUser;

/// <summary>
/// Ovi podaci se šalju kod registracije novog korisnika
/// </summary>
public record RegisterUserRequest
{
    public string Name { get; set; }
    public string Email { get; set; }
    public string Phone { get; set; }
    public string Address { get; set; }
    public string Password { get; set; }
    public string RepeatPassword { get; set; }
}
