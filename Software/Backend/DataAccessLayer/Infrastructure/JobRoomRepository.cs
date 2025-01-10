using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Microsoft.Data.SqlClient;
using Microsoft.Identity.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Infrastructure {
    public class JobRoomRepository : IJobRoomRepository {
        private readonly DB db;
        public JobRoomRepository(DB Database)
        {
            this.db = Database;
        }
        public List<JobRoomModel>? GetJobRoom(int jobID) {

            string query = $@"SELECT 
    w.id AS working_id,
    w.job_id,
    j.title AS job_title,
    j.allow_worker_invite,
    j.employer_id,
    j.job_status_id,
    js.status AS job_status, -- Added the job status
    w.skill_id,
    s.title AS skill_title,
    w.worker_id,
    ISNULL(au.name, 'Nema radnika') AS worker_name,
    ws.status AS working_status
FROM 
    working w
LEFT JOIN 
    app_user au ON w.worker_id = au.id
JOIN 
    working_status ws ON w.working_status_id = ws.id
JOIN 
    job j ON w.job_id = j.id
JOIN 
    job_status js ON js.id = j.job_status_id
JOIN 
    skill s ON w.skill_id = s.id
WHERE 
    w.job_id = {jobID};
";
            using (var reader = db.ExecuteReader(query)) {
                var result = new List<JobRoomModel>();
                while (reader.Read()) {
                    result.Add(JobRoomModelFromReader(reader));
                }

                return result;
            }

        }
        private JobRoomModel JobRoomModelFromReader(SqlDataReader sqlDataReader) {
            return new JobRoomModel {
                JobId = (int)sqlDataReader["job_id"],
                WorkingId = (int)sqlDataReader["working_id"],
                JobTitle = (string)sqlDataReader["job_title"],
                AllowWorkerInvite = (bool)sqlDataReader["allow_worker_invite"],
                JobStatus = (string)sqlDataReader["job_status"],
                EmployerId = (int)sqlDataReader["employer_id"],
                SkillId = (int)sqlDataReader["skill_id"],
                SkillTitle = (string)sqlDataReader["skill_title"],
                WorkerId = sqlDataReader["worker_id"] as int?, 
                WorkerName = (string)sqlDataReader["worker_name"],
                WorkingStatus = (string)sqlDataReader["working_status"]
            };

        }
    }
}
