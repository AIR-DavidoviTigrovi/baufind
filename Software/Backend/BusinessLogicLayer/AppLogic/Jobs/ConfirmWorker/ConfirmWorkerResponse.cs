using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.ConfirmWorker
{
    public record ConfirmWorkerResponse
    {
        public bool Success { get; set; }
        public string? Message { get; set; }
    }
}
