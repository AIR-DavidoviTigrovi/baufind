using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Reviews;
using BusinessLogicLayer.AppLogic.Reviews.GetUserReviews;
using DataAccessLayer.AppLogic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.Infrastructure
{
    public class ReviewService : IReviewService
    {
        private readonly IReviewRepository _reviewRepository;
        private readonly IJwtService _jwtService;

        public ReviewService(IReviewRepository reviewRepository, IJwtService jwtService)
        {
            _reviewRepository = reviewRepository;
            _jwtService = jwtService;
        }
        public GetUserReviewsResponse GetUserReviews(int userId)
        {
            try
            {
                var workerReviews = _reviewRepository.GetWorkerReviews(userId)
                    .Select(r => new ReviewRecord
                    {
                        ReviewId = r.ReviewId,
                        ReviewerId = r.ReviewerId,
                        ReviewerName = r.ReviewerName,
                        ReviewerImage = r.ReviewerImage,
                        ReviewedJobId = r.ReviewedJobId,
                        JobTitle = r.JobTitle,
                        Comment = r.Comment,
                        Rating = r.Rating,
                       // ReviewDate = r.ReviewDate,
                        Pictures = r.Pictures
                            .Select(p => new ImageRecord
                            {
                                Id = p.Id,
                                Picture = p.Picture
                            }).ToList()
                    }).ToList();

                var employerReviews = _reviewRepository.GetEmployerReviews(userId)
                    .Select(r => new ReviewRecord
                    {
                        ReviewId = r.ReviewId,
                        ReviewerId = r.ReviewerId,
                        ReviewerName = r.ReviewerName,
                        ReviewerImage = r.ReviewerImage,
                        ReviewedJobId = r.ReviewedJobId,
                        JobTitle = r.JobTitle,
                        Comment = r.Comment,
                        Rating = r.Rating,
                        //ReviewDate = r.ReviewDate,
                        Pictures = r.Pictures
                            .Select(p => new ImageRecord
                            {
                                Id = p.Id,
                                Picture = p.Picture
                            }).ToList()
                    }).ToList();

                return new GetUserReviewsResponse
                {
                    WorkerReviews = workerReviews,
                    EmployerReviews = employerReviews,
                    Error = null
                };
            }
            catch (Exception ex)
            {
                return new GetUserReviewsResponse
                {
                    Error = $"An error occurred while fetching reviews: {ex.Message}"
                };
            }
        }
    }
}
