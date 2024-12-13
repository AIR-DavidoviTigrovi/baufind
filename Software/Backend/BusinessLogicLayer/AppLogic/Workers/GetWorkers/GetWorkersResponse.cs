using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

/// <summary>
/// Odgovor koji se šalje preko API zahtjeva za dobivanje svih radnika po vještini
/// </summary>

namespace BusinessLogicLayer.AppLogic.Workers.GetWorkers {

    public record GetWorkersResponse {
        public List<WorkerRecord> WorkerRecords { get; set; } = new List<WorkerRecord>();
        public string? Error { get; set; } = string.Empty;
    }
}
