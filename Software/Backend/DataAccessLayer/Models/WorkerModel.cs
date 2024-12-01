using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models {
    public record WorkerModel {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Address { get; set; }
        public int NumOfJobs { get; set; }
        public string Skills { get; set; }
        public double AvgRating { get; set; }
    }
}
