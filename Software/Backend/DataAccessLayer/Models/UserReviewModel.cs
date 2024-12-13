using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record UserReviewModel
    {
        public required int ReviewId { get; set; }
        public required int ReviewerId { get; set; }
        public required string ReviewerName { get; set; }
        public byte[]? ReviewerImage { get; set; }
        public required int ReviewedJobId { get; set; }
        public required string JobTitle { get; set; }
        public required string Comment { get; set; }
        public required int Rating { get; set; }
       // public required DateTime ReviewDate { get; set; }
        public required List<ImageModel> Pictures { get; set; } = new List<ImageModel>();
        
    }
}
