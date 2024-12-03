using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Microsoft.Data.SqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Infrastructure
{
    /// <summary>
    /// Repozitorij za dohvaćanje skillova tj pozicija
    /// </summary>
    public class SkillRepository: ISkillRepository
    {
        private readonly DB _db;
        public SkillRepository(DB db)
        {
            _db = db;
        }
        /// <summary>
        /// Funkcija koja dohvaća sve skillove u bazi
        /// </summary>
        /// <returns>Listu skillova</returns>
        public List<SkillModel> GetAll()
        {
            string query = "SELECT * FROM skill;";
            using (var reader = _db.ExecuteReader(query))
            {
                var result = new List<SkillModel>();
                while (reader.Read())
                {
                    result.Add(SkillModelFromReader(reader));
                }

                return result;
            }
        }

        private SkillModel SkillModelFromReader(SqlDataReader reader)
        {
            return new SkillModel()
            {
                Id = (int)reader["id"],
                Title = (string)reader["title"]
            };
        }
    }
}
