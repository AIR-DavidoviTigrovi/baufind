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
            
        }
    }
}
