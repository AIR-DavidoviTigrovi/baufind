using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJobHistory
{
    public record JobHistoryRecord
    {
        public int Id { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public string Location { get; set; }
        public string OwnerName { get; set; }
        public List<WorkerOnJobModel> Workers { get; set; }
        public List<EventModel> Events { get; set; }
    }
}
