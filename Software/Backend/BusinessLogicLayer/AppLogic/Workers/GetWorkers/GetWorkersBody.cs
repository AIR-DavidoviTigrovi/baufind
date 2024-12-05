using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

///summary
/// Podaci koji se šalju u tijelu, naziv vještine
///summary

namespace BusinessLogicLayer.AppLogic.Workers.GetWorkers {
    public record GetWorkersBody {
        public string Title { get; set; }
    }
}
