using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record AllJobsHistoryModel
    {
        public int JobId { get; set; }
        public string Title { get; set; }
        public byte[] Picture { get; set; }
        public string CompletionDate { get; set; }
        public bool IsOwner { get; set; }
    }
}
