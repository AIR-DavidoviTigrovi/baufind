using DataAccessLayer.AppLogic;
using FluentValidation;

namespace BusinessLogicLayer.AppLogic.Users.RegisterUser;

/// <summary>
/// Klasa koja validira request koji se šalje kod registracije usera
/// Ove validacije bi trebale sve proći, jer na frontendu trebaju postojati iste, no ako se slučajno zaobiđu, ovdje također moraju biti
/// </summary>
public class RegisterUserValidator : AbstractValidator<RegisterUserRequest>
{
    /// <summary>
    /// Validacija napisana u konstruktor klase
    /// </summary>
    /// <param name="_repository">Repozitorij proslijeđen iz servisne klase</param>
    public RegisterUserValidator(IUserRepository _repository)
    {
        RuleFor(x => x.Password)
            .NotEmpty().WithMessage("Lozinka je obavezna");

        RuleFor(x => x.RepeatPassword)
            .Equal(x => x.Password).WithMessage("Lozinke se ne podudaraju");

        RuleFor(x => x.Name)
            .NotEmpty().WithMessage("Ime je obavezno")
            .MaximumLength(100).WithMessage("Korisničko ime ne smije prelaziti 100 znakova");

        RuleFor(x => x.Address)
            .NotEmpty().WithMessage("Adresa je obavezna")
            .MaximumLength(100).WithMessage("Adresa ne smije prelaziti 100 znakova");

        RuleFor(x => x.Phone)
            .NotEmpty().WithMessage("Broj telefona je obavezan")
            .MaximumLength(100).WithMessage("Broj telefona ne smije prelaziti 100 znakova");

        RuleFor(x => x.Email)
            .NotEmpty().WithMessage("Email mora biti unesen")
            .MaximumLength(100).WithMessage("Email ne smije prelaziti 100 znakova")
            .Matches(@"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$").WithMessage("Unesite validan email")
            .Custom((email, context) =>
            {
                var users = _repository.GetUsers();
                if (users.Any(u => u.Email == email))
                {
                    context.AddFailure("Email adresa već postoji");
                }
            });
    }
}
