using DataAccessLayer.AppLogic;
using FluentValidation;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.AddJob
{
    public class AddJobValidator : AbstractValidator<AddJobRequest>
    {
        public AddJobValidator(IJobRepository _jobRepository)
        {
            RuleFor(x => x.Title)
                .NotEmpty().WithMessage("Naslov je obavezan");
            RuleFor(x => x.Description)
                .NotEmpty().WithMessage("Opis je obavezan");
            RuleFor(x => x.Allow_worker_invite)
                .NotNull().WithMessage("Dozvoli radniku da pozove je obavezan");
            RuleFor(x => x.Pictures)
                .NotEmpty().WithMessage("Slike su obavezne")
                .Must(x => x.Count > 0).WithMessage("Mora postojati barem jedna slika");
            RuleFor(x => x.Skills)
                .NotEmpty().WithMessage("Pozicije su obavezne")
                .Must(x => x.Count > 0).WithMessage("Mora postojati barem jedna pozicija");
        }
    }
}