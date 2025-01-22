using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Workers.GetWorkers {
    public class GetWorkerResponse {
        public WorkerRecord workerRecord = new WorkerRecord();
        public string? Error { get; set; } = string.Empty;
    }
}
