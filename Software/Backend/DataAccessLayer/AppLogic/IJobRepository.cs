using DataAccessLayer.Models;

namespace DataAccessLayer.AppLogic
{
    public interface IJobRepository
    {
        /// <summary>
        /// Metoda za dodavanje novog posla
        /// </summary>
        /// <param name="jobModel"></param>
        /// <returns>ID ako uspješno, inače null, možda negdje zatreba</returns>
        public int? AddJob(JobModel jobModel);
    }
}
