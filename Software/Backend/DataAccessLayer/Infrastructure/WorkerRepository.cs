using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Microsoft.Data.SqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Infrastructure {

    /// <summary>
    /// Repozitorij za dohvaćanje radnika prema vještini za popunavanje poslova.
    /// </summary>
    public class WorkerRepository : IWorkerRepository {

        private readonly DB dB;
        public WorkerRepository(DB Db)
        {
            this.dB = Db;
        }
        /// <summary>
        /// Implementacija dohvaćanja radnika prema vještini
        /// </summary>
        /// Returns WorkerModel - Radnik koji zadovoljava određenu vještinu i njegove informacije o bivšim poslovima i prosječnoj ocjeni
         public List<WorkerModel>? GetWorkers(string skill) 
        {
            // Trim the input string and split it into individual skills
            string trimmed = skill.Trim('[', ']');
            var elements = trimmed.Split(',')
                                  .Select(e => e.Trim()) // Trim spaces around each element
                                  .Select(e => $"'{e}'"); // Add single quotes around each skill

            // Join the elements into a single string to pass into the SQL query
            string skills = string.Join(", ", elements);

            // Construct the SQL query
            string query = $@"
SELECT 
    u.id, 
    u.name, 
    u.address, 
    COUNT(DISTINCT j.id) AS numOfJobs, 
    STUFF((
        SELECT ', ' + s2.title
        FROM user_skill us2
        JOIN skill s2 ON us2.skill_id = s2.id
        WHERE us2.user_id = u.id AND s2.title IN ({skills})
        FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 2, '') AS skills,  
    COALESCE(CAST(AVG(CAST(wr.rating AS DECIMAL(3, 2))) AS DECIMAL(5, 2)), 0.00) AS avgRating
FROM 
    app_user u
LEFT JOIN 
    working w ON u.id = w.worker_id
LEFT JOIN 
    job j ON w.job_id = j.id
LEFT JOIN 
    worker_review wr ON w.id = wr.working_id
WHERE 
    EXISTS (
        SELECT 1
        FROM user_skill us
        JOIN skill s ON us.skill_id = s.id
        WHERE us.user_id = u.id AND s.title = 'prvi skill'
    )
GROUP BY 
    u.id, u.name, u.address;

";

            using (var reader = dB.ExecuteReader(query)) {
                var result = new List<WorkerModel>();
                while (reader.Read()) {
                    result.Add(WorkerModelFromReader(reader));
                }

                return result;
            }

        }
        private WorkerModel WorkerModelFromReader(SqlDataReader reader) {
            return new WorkerModel {
                Id = (int)reader["id"],
                Name = (string)reader["name"],
                Address = (string)reader["address"],
                NumOfJobs = (int)reader["numOfJobs"],
                Skills = reader.IsDBNull(reader.GetOrdinal("skills")) ? string.Empty : (string)reader["skills"],
                AvgRating = reader.IsDBNull(reader.GetOrdinal("avgRating")) ? 0m : (decimal)reader["avgRating"]
            };

        }
        /// <summary>
        /// Za posao dohvaća imena radnika i imena pozicija koje su imali na tom poslu
        /// </summary>
        /// <param name="jobId"></param>
        /// <returns>Za posao dohvaća imena radnika i imena pozicija koje su imali na tom poslu</returns>
        public List<WorkerOnJobModel> GetWorkerNameAndSkillTitleForJob(int jobId)
        {
            string query = @"
                SELECT u.name AS WorkerName, s.title AS RoleTitle
                FROM working w
                JOIN app_user u ON w.worker_id = u.id
                JOIN skill s ON w.skill_id = s.id
                WHERE w.job_id = @jobId
                AND w.working_status_id = 4;
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@jobId", jobId }
            };

            var workers = new List<WorkerOnJobModel>();

            using (var reader = dB.ExecuteReader(query, parameters))
            {
                while (reader.Read())
                {
                    workers.Add(new WorkerOnJobModel
                    {
                        WorkerName = (string)reader["WorkerName"],
                        RoleTitle = (string)reader["RoleTitle"]
                    });
                }
            }
            return workers;
        }

    }
}
