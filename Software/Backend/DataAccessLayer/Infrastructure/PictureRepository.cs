using DataAccessLayer.AppLogic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Infrastructure
{
    public class PictureRepository : IPictureRepository
    {
        private readonly DB _db;
        public PictureRepository(DB db)
        {
            _db = db;
        }
        public void AddJobPicture(int job_id, List<byte[]> pictures)
        {
            string query = @"
                INSERT INTO picture
                (picture)
                VALUES
                (@picture)
                SELECT CAST(SCOPE_IDENTITY() AS INT);
            ";

            foreach (var picture in pictures)
            {
                var parameters = new Dictionary<string, object>
                {
                    { "@picture", picture }
                };

                object? picture_id = _db.ExecuteScalar(query, parameters);
                if (picture_id != null)
                {
                    AddJobPicture(job_id, (int)picture_id);
                }
            }
        }

        private void AddJobPicture(int job_id, int picture_id)
        {
            string query = @"
                INSERT INTO job_picture
                (job_id, picture_id)
                VALUES
                (@job_id, @picture_id)
                SELECT CAST(SCOPE_IDENTITY() AS INT);
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@job_id", job_id },
                { "@picture_id", picture_id }
            };

            _db.ExecuteNonQuery(query, parameters);
        }
    }
}
