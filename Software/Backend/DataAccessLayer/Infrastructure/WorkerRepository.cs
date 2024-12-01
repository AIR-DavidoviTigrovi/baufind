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

            string query = $"SELECT \r\n    u.id, \r\n    u.name, \r\n    u.address, \r\n    COUNT(j.id) AS numOfJobs, \r\n    s.title AS skills,  \r\n    AVG(wr.rating) AS avgRating \r\nFROM \r\n    app_user u\r\nLEFT JOIN \r\n    working w ON u.id = w.worker_id\r\nLEFT JOIN \r\n    job j ON w.job_id = j.id\r\nLEFT JOIN \r\n    worker_review wr ON w.id = wr.working_id\r\nLEFT JOIN \r\n    user_skill us ON u.id = us.user_id\r\nLEFT JOIN \r\n    skill s ON us.skill_id = s.id\r\nWHERE \r\n\ts.title = '{skill}'\r\nGROUP BY \r\n    u.id, u.name, u.address, s.title;";
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
                AvgRating = (double)reader["avgRating"]

            };
        }

    }
}
