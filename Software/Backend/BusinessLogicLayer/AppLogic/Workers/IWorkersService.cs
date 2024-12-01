using BusinessLogicLayer.AppLogic.Workers.GetWorkers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Workers {
    public interface IWorkersService {
        public GetWorkersResponse GetWorkers(string skill);
    }
}
