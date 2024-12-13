using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Reviews.GetUserReviews
{
    public record GetUserReviewsResponse
    {
        public List<ReviewRecord>? WorkerReviews { get; set; }
        public List<ReviewRecord>? EmployerReviews { get; set; }
        public string? Error { get; set; }
    }
}
