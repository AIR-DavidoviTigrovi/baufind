﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public record JobHistoryModel
    {
        public int JobId { get; set; }
        public string JobTitle { get; set; }
        public string JobDescription { get; set; }
        public string JobLocation { get; set; }
        public string JobOwnerName { get; set; }
        public List<WorkerOnJobModel> Workers { get; set; }
        public List<EventModel> Events { get; set; }
    }
}
