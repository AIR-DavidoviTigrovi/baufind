using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.AddUserToJob
{
    public record CallWarkerToJobResponse
    {
        public bool Success { get; set; }
        public string? Message { get; set; }
    }
}
