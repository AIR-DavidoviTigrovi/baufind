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

        /// <summary>
        /// Metoda za upis u slabi entitet gdje se povezuje posao sa svim pozicijama koje se traže
        /// </summary>
        /// <param name="skills"></param>
        /// <param name="job_id"></param>
        public void CreatePositionsForJob(List<int> skills, int job_id);
    }
}
