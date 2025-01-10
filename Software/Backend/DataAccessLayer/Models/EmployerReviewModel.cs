using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record EmployerReviewModel
    {
        public int ReviewerId { get; set; }
        public int JobId { get; set; }
        public required string Comment { get; set; }
        public required int Rating { get; set; }
        public List<ImageModel>? Images { get; set; }
        public DateTime ReviewDate {  get; set; } 
    }
}
