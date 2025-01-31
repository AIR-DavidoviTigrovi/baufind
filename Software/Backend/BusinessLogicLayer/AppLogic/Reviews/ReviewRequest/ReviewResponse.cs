using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Reviews.ReviewRequest
{
    public record ReviewResponse
    {

        public string? Success { get; set; } = string.Empty;
        public string? Error { get; set; } = string.Empty;
    }
}
