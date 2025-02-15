﻿using DataAccessLayer.Models;

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
        /// Ne uzima poslove od korisnika koji traži poslove
        /// </summary>
        /// <param name="skillIds"></param>
        /// <param name="userId"></param>
        /// <returns>Vraća sve poslove koji imaju otvorene pozicije koje smo dali kroz argument funkcije</returns>
        public List<JobSearchModel> GetJobsWhereSkillPositionsOpen(List<int> skillIds, int userId);
        /// <summary>
        /// Dohvaća slike za posao čiji je ID dan
        /// </summary>
        /// <param name="jobId"></param>
        /// <returns>Slike za posao</returns>
        public List<byte[]> GetPicturesForJob(int jobId);
        /// <summary>
        /// Dohvaća sve pozicije posla za koje je korisnik validan i isključuje pozicije za koje se već prijavio na tom poslu
        /// </summary>
        /// <param name="jobId"></param>
        /// <returns>Vještine za posao</returns>
        public List<SkillModel> GetEmptySkillsWhichUserHasForJob(int jobId, List<int> skillIds, int userId);
        /// <summary>
        /// Dohvaća posao po ID-u
        /// </summary>
        /// <param name="jobId"></param>
        /// <returns></returns>
        public JobModel GetJob(int jobId);

        /// <summary>
        /// Dohvaća job i working po korisniku i statusu
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="statusId"></param>
        /// <returns></returns>
        public List<JobWorkingModel> GetJobWorkingByUserAndStatus(int userId, int statusId);

        /// <summary>
        /// Dohvaća poslove kojima je korisnik vlasnik ili je primljen na njih
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        public List<MyJobModel> GetMyJobsForUser(int userId);

        /// <summary>
        /// Funkcija dobiva userId korisnika i vraća popis poslova koji su završili a na kojima je korisnik bio radnik ili vlasnik
        /// </summary>
        /// <param name="userId"></param>
        /// <returns>Poslovi imaju: id, naslov, jednu sliku, datum završetka, bool je li vlasnik, </returns>
        /// 
        public List<AllJobsHistoryModel> GetAllJobsHistory(int userId);
        /// <summary>
        /// Dohvaća podatke koji se prikazuju na history-u za jedan posao
        /// </summary>
        /// <param name="jobId"></param>
        /// <returns>Vraća id, naziv, opis i lokaciju posla. Ime vlasnika posla, popis radnika i imena njihovih pozicija. Kronoloski slijed dogadaja posla.</returns>
        public JobHistoryModel GetJobHistory(int jobId);

        /// <summary>
        /// Provjerava je li korisnik radio na poslu ili bio vlasnik posla kako bi se znalo smije li gledati taj posao u povijesti
        /// </summary>
        /// <param name="jobId"></param>
        /// <param name="userId"></param>
        /// <returns>Boolean</returns>
        bool CheckIfUserWorkedOrOwnedJob(int jobId, int userId);
    }
}
