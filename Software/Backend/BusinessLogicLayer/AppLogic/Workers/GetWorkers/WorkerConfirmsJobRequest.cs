using BusinessLogicLayer.AppLogic.Jobs.ConfirmWorker;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Workers.GetWorkers {
    public class WorkerConfirmsJobRequest {
        public ConfirmWorkerRequest ConfirmWorkerRequest { get; set; }
        public int EmployerIdForNotification { get; set; }
    }
}
