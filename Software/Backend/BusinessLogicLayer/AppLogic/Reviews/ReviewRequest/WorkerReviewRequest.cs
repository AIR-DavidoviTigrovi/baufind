using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Reviews.ReviewRequest
{
    public record WorkerReviewRequest
    {
        public int WorkingId { get; set; }
        public required string Comment { get; set; }
        public required int Rating { get; set; }
        public List<ImageRecord>? Images { get; set; }
    }
}