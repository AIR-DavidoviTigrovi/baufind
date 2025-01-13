using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Microsoft.Data.SqlClient;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Infrastructure
{
    public class ReviewRepository : IReviewRepository
    {
        private readonly DB _db;
        public ReviewRepository(DB db)
        {
            _db = db;
        }
        public List<UserReviewModel> GetEmployerReviews(int userId)
        {
            string query = @"SELECT 
                    wr.id AS ReviewId,
                    wr.rating AS Rating,
                    wr.comment AS Comment,
                    wrp.picture_id AS PictureId,
                    p.picture AS PictureData,
                    w.job_id AS ReviewedJobId,
                    j.title AS JobTitle,
                    u.name AS ReviewerName,
                    u.profile_picture AS ReviewerProfilePicture,
                    j.datetime AS ReviewDate
                FROM Worker_Review wr
                JOIN Working w ON wr.working_id = w.id
                JOIN Job j ON w.job_id = j.id
                JOIN app_user u ON wr.reviewer_id = u.id
                LEFT JOIN Worker_Review_Picture wrp ON wr.id = wrp.worker_review_id
                LEFT JOIN Picture p ON wrp.picture_id = p.id
                WHERE w.worker_id = @UserId;";
           
            var parameters = new Dictionary<string, object>
            {
                { "@UserId", userId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var result = new List<UserReviewModel>();
                while (reader.Read()) {
                    result.Add(UserReviewModelFromReader(reader));
                }
                return result;
            }
        }

        
        public List<UserReviewModel> GetWorkerReviews(int userId)
        {
            
              string query = @"
                    SELECT 
                        er.id AS ReviewId,
                        er.reviewer_id AS ReviewerId,
                        u.name AS ReviewerName,
                        u.profile_picture AS ReviewerProfilePicture,
                        er.job_id AS ReviewedJobId,
                        j.title AS JobTitle,
                        er.comment AS Comment,
                        er.rating AS Rating,
                        j.datetime AS ReviewDate,
                        p.picture AS PictureData,
                        erp.picture_id AS PictureId
                    FROM Employer_Review er
                    JOIN app_user u ON er.reviewer_id = u.id
                    JOIN Job j ON er.job_id = j.id
                    LEFT JOIN Employer_Review_Picture erp ON er.id = erp.employer_review_id
                    LEFT JOIN Picture p ON erp.picture_id = p.id
                    WHERE j.employer_id = @UserId;";

            var parameters = new Dictionary<string, object>
            {
                { "@UserId", userId }
            };

            using (var reader = _db.ExecuteReader(query, parameters))
            {
                var result = new List<UserReviewModel>();
                while (reader.Read())
                {
                    result.Add(UserReviewModelFromReader(reader));
                }
                return result;
            }
        }
        /// <summary>
        /// Unosi novi zapis u tablicu Employer_Review i vraća ID (SCOPE_IDENTITY).
        /// </summary>
        public int InsertEmployerReview(EmployerReviewModel employerReview)
        {
            if (employerReview.ReviewDate == default(DateTime))
            {
                employerReview.ReviewDate = DateTime.UtcNow;
            }

            string query = @"
                INSERT INTO Employer_Review 
                    (reviewer_id, job_id, comment, rating, review_date)
                VALUES
                    (@ReviewerId, @JobId, @Comment, @Rating, @ReviewDate);

                SELECT SCOPE_IDENTITY();
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@ReviewerId", employerReview.ReviewerId },
                { "@JobId", employerReview.JobId },
                { "@Comment", employerReview.Comment },
                { "@Rating", employerReview.Rating },
                { "@ReviewDate", employerReview.ReviewDate }
            };

            object scalarResult = _db.ExecuteScalar(query, parameters);
            return Convert.ToInt32(scalarResult);
        }

        public void InsertEmployerReviewPicture(int employerReviewId, ImageModel imageRecord)
        {
            int pictureId = InsertPicture(imageRecord);

            string sql = @"
                INSERT INTO Employer_Review_Picture (employer_review_id, picture_id)
                VALUES (@ReviewId, @PictureId);
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@ReviewId", employerReviewId },
                { "@PictureId", pictureId }
            };

            _db.ExecuteNonQuery(sql, parameters);
        }
        

        /// <summary>
        /// Pomoćna metoda za snimanje slike u tablicu Picture i dobivanje ID-a te slike.
        /// </summary>
        private int InsertPicture(ImageModel imageRecord)
        {
            string sql = @"
                INSERT INTO Picture (picture)
                VALUES (@PictureData);
                SELECT SCOPE_IDENTITY();
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@PictureData", imageRecord.Picture }
            };

            object scalarResult = _db.ExecuteScalar(sql, parameters);
            return Convert.ToInt32(scalarResult);
        }

        public void InsertWorkerReviewPictures(int workerReviewId, ImageModel imageRecord)
        {
            int pictureId = InsertPicture(imageRecord);

            string sql = @"
                INSERT INTO Worker_Review_Picture (worker_review_id, picture_id)
                VALUES (@ReviewId, @PictureId);
            ";

            var parameters = new Dictionary<string, object>
            {
                { "@ReviewId", workerReviewId },
                { "@PictureId", pictureId }
            };

            _db.ExecuteNonQuery(sql, parameters);
        }

        public int InsertWorkerReview(WorkerReviewModel workerReview)
        {
            string query = @"
                INSERT INTO Worker_Review 
                    (working_id, comment, rating, review_date)
                VALUES
                    (@WorkingId, @Comment, @Rating, @ReviewDate);

                SELECT SCOPE_IDENTITY();
            ";

                    var parameters = new Dictionary<string, object>
            {
                { "@WorkingId", workerReview.WorkingId },
                { "@Comment", workerReview.Comment },
                { "@Rating", workerReview.Rating },
                { "@ReviewDate", workerReview.ReviewDate == default(DateTime) ? DateTime.UtcNow : workerReview.ReviewDate }
            };

            object scalarResult = _db.ExecuteScalar(query, parameters);
            return Convert.ToInt32(scalarResult);
        }
        public List<ReviewNotificationModel> GetAllReviewsToComplete(int userId)
        {
            string sql = @"
        (  -- radnik
           SELECT 
               j.id            AS JobId,
               j.title         AS JobTitle,
               j.employer_id   AS PersonId,
               u.name          AS PersonName,
               'employer'      AS Position
           FROM job j
           JOIN working w ON j.id = w.job_id
           JOIN app_user u ON j.employer_id = u.id
           WHERE w.worker_id = @UserId
             AND j.job_status_id = 3
             AND NOT EXISTS (
                 SELECT 1
                 FROM Employer_Review er
                 WHERE er.job_id = j.id
                   AND er.reviewer_id = @UserId
             )
        )
        UNION
        (  -- poslodavac
           SELECT
               j.id           AS JobId,
               j.title        AS JobTitle,
               w.worker_id    AS PersonId,
               u.name         AS PersonName,
               s.title        AS Position
           FROM job j
           JOIN working w ON j.id = w.job_id
           JOIN app_user u ON w.worker_id = u.id
           JOIN skill s ON w.skill_id = s.id
           WHERE j.employer_id = @UserId
             AND j.job_status_id = 3
             AND NOT EXISTS (
                 SELECT 1
                 FROM Worker_Review wr
                 WHERE wr.working_id = w.id
                   AND wr.reviewer_id = @UserId
             )
        )
    ";

            var parameters = new Dictionary<string, object>{
        {"@UserId", userId}
    };

            return _db.ExecuteQuery(
                sql,
                parameters,
                reader => new ReviewNotificationModel
                {
                    JobId = reader.GetInt32(reader.GetOrdinal("JobId")),
                    JobTitle = reader.GetString(reader.GetOrdinal("JobTitle")),
                    PersonId = reader.GetInt32(reader.GetOrdinal("PersonId")),
                    PersonName = reader.GetString(reader.GetOrdinal("PersonName")),
                    Position = reader.GetString(reader.GetOrdinal("Position"))
                }
            );
        }
        private UserReviewModel UserReviewModelFromReader(SqlDataReader reader)
        {
            var userReview = new UserReviewModel
            {
                ReviewId = reader.GetInt32(reader.GetOrdinal("ReviewId")),
                ReviewerId = reader.GetInt32(reader.GetOrdinal("ReviewerId")),
                ReviewerName = reader.GetString(reader.GetOrdinal("ReviewerName")),
                ReviewerImage = reader.IsDBNull(reader.GetOrdinal("ReviewerProfilePicture"))
            ? null : (byte[])reader["ReviewerProfilePicture"],
                ReviewedJobId = reader.GetInt32(reader.GetOrdinal("ReviewedJobId")),
                JobTitle = reader.GetString(reader.GetOrdinal("JobTitle")),
                Comment = reader.GetString(reader.GetOrdinal("Comment")),
                Rating = reader.GetInt32(reader.GetOrdinal("Rating")),
                ReviewDate = reader.GetDateTime(reader.GetOrdinal("ReviewDate")),
                Pictures = new List<ImageModel>()
            };

            if (!reader.IsDBNull(reader.GetOrdinal("PictureData")))
            {
                userReview.Pictures.Add(new ImageModel
                {
                    Id = reader.GetInt32(reader.GetOrdinal("PictureId")),
                    Picture = (byte[])reader["PictureData"]
                });
            }

            return userReview;

        }

    }
}
