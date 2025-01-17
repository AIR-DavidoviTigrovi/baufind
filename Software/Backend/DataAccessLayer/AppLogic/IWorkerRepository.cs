using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.AppLogic {
    public interface IWorkerRepository {
        public List<WorkerModel>? GetWorkers(string skill);
        /// <summary>
        /// Za posao dohvaća imena radnika i imena pozicija koje su imali na tom poslu
        /// </summary>
        /// <param name="jobId"></param>
        /// <returns>Za posao dohvaća imena radnika i imena pozicija koje su imali na tom poslu</returns>
        public List<WorkerOnJobModel> GetWorkerNameAndSkillTitleForJob(int jobId);
    }
}
