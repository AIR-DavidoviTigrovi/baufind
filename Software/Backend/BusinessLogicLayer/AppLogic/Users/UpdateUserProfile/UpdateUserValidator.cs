using DataAccessLayer.Models;
using FluentValidation;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Users.UpdateUserProfile
{
    public class UpdateUserValidator : AbstractValidator<UpdateUserRequest>
    {
        public UpdateUserValidator()
        {
            RuleFor(request => request.UserId)
                .GreaterThan(0).WithMessage("UserId must be a positive integer.");

            RuleFor(request => request.Name)
                .MaximumLength(100).WithMessage("Name cannot exceed 100 characters.")
                .When(request => request.Name != null); 

            RuleFor(request => request.Address)
                .MaximumLength(100).WithMessage("Address cannot exceed 100 characters.")
                .When(request => request.Address != null); 

            RuleFor(request => request.Phone)
                .MaximumLength(30).WithMessage("Phone number cannot exceed 30 characters.")
                .When(request => request.Phone != null); 

            RuleForEach(request => request.AddSkills)
                .GreaterThan(0).WithMessage("Skill IDs to add must be positive integers.")
                .When(request => request.AddSkills != null);

            RuleForEach(request => request.RemoveSkills)
                .GreaterThan(0).WithMessage("Skill IDs to remove must be positive integers.")
                .When(request => request.RemoveSkills != null);
        }
    }
}
