using FluentValidation;

namespace BusinessLogicLayer.AppLogic.Users.Login;

/// <summary>
/// Validira prenesene podatke za prijavu
/// </summary>
public class LoginValidator : AbstractValidator<LoginRequest>
{
    /// <summary>
    /// Validacija napisana u konstruktur klase
    /// </summary>
    public LoginValidator()
    {
        RuleFor(x => x.Password)
            .NotEmpty().WithMessage("Lozinka je obavezna");

        RuleFor(x => x.Email)
            .NotEmpty().WithMessage("Email mora biti unesen")
            .Matches(@"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$").WithMessage("Unesite validan email");
    }
}
