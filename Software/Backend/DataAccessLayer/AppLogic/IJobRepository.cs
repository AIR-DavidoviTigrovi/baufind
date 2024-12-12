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
        /// <param name="skills"> lista ID-ova</param>
        /// <param name="job_id"> prenosi se od JWT-a </param>
        public void CreatePositionsForJob(List<int> skills, int job_id);
        /// <summary>
        /// Uzima poslove koji traže pozicije koje se daju kroz argument, tj. gdje su te pozicije otvorene.
        /// Pretpostavka je da će working_status_id = 1 biti nekakav "Pozicija otvorena" status tj kad se tek kreira posao
        /// </summary>
        /// <param name="skillIds"></param>
        /// <returns>Vraća sve poslove koji imaju otvorene pozicije koje smo dali kroz argument funkcije</returns>
        public List<JobModel> GetJobsWhereSkillPositionsOpen(List<int> skillIds);
    }
}
