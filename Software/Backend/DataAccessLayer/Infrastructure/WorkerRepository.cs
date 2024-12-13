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
    COUNT(j.id) AS numOfJobs, 
    STRING_AGG(s.title, ', ') AS skills,  
    COALESCE(CAST(AVG(CAST(wr.rating AS DECIMAL(3, 2))) AS DECIMAL(5, 2)), 0.00) AS avgRating
FROM 
    app_user u
LEFT JOIN 
    working w ON u.id = w.worker_id
LEFT JOIN 
    job j ON w.job_id = j.id
LEFT JOIN 
    worker_review wr ON w.id = wr.working_id
LEFT JOIN 
    user_skill us ON u.id = us.user_id
LEFT JOIN 
    (SELECT DISTINCT id, title
     FROM skill) s ON us.skill_id = s.id  
WHERE 
    s.title IN ({skills}) 
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
                Skills = (string)reader["skills"],
                AvgRating = reader.IsDBNull(reader.GetOrdinal("avgRating")) ? 0m : (decimal)reader["avgRating"]

            };
        }

    }
}
