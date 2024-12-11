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
    public class ReviewRepository : IReviewRepository
    {
        private readonly DB _db;
        public ReviewRepository(DB db)
        {
            _db = db;
        }
        public List<UserReviewModel> GetEmployerReviews(int userId)
        {
            /*string query = @"SELECT 
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
                WHERE w.worker_id = @UserId;";*/
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
                        p.picture AS PictureData,
                        erp.picture_id AS PictureId
                    FROM Employer_Review er
                    JOIN app_user u ON er.reviewer_id = u.id 
                    JOIN Job j ON er.job_id = j.id
                    LEFT JOIN Employer_Review_Picture erp ON er.id = erp.employer_review_id
                    LEFT JOIN Picture p ON erp.picture_id = p.id
                    WHERE j.employer_id = @UserId;
                    ";
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
            /*
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
             */
            string query = @"
                        SELECT 
                            wr.id AS ReviewId,
                            w.worker_id AS ReviewerId, 
                            u.name AS ReviewerName,
                            u.profile_picture AS ReviewerProfilePicture,
                            w.job_id AS ReviewedJobId,
                            j.title AS JobTitle,
                            wr.comment AS Comment,
                            wr.rating AS Rating,
                            p.picture AS PictureData,
                            wrp.picture_id AS PictureId
                        FROM Worker_Review wr
                        JOIN Working w ON wr.working_id = w.id
                        JOIN Job j ON w.job_id = j.id
                        JOIN app_user u ON w.worker_id = u.id 
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
                while (reader.Read())
                {
                    result.Add(UserReviewModelFromReader(reader));
                }
                return result;
            }
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
               // ReviewDate = reader.GetDateTime(reader.GetOrdinal("ReviewDate")),
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
