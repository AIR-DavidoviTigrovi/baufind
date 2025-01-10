using BusinessLogicLayer.AppLogic;
using BusinessLogicLayer.AppLogic.Reviews;
using BusinessLogicLayer.AppLogic.Reviews.GetUserReviews;
using BusinessLogicLayer.AppLogic.Reviews.ReviewRequest;
using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
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
                        ReviewDate = r.ReviewDate,
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
                        ReviewDate = r.ReviewDate,
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
        public int SaveEmployerReview(EmployerReviewRequest employerReview)
        {

            var userReviewModel = new EmployerReviewModel
            {
                ReviewerId = employerReview.ReviewerId,
                JobId = employerReview.JobId,
                Comment = employerReview.Comment,
                Rating = employerReview.Rating,
                ReviewDate = DateTime.UtcNow,
            };
            int newReviewId = _reviewRepository.InsertEmployerReview(userReviewModel);
            if (employerReview.Images == null || employerReview.Images.Count == 0)
            {
                return newReviewId;
            }
            foreach (var image in employerReview.Images)
            {
                var imageModel = new ImageModel
                {
                    Picture = image.Picture,
                    Id = image.Id,
                };

                _reviewRepository.InsertEmployerReviewPicture(newReviewId, imageModel);
            }

            return newReviewId;
        }

        public int SaveWorkerReview(WorkerReviewRequest workerReviewRequest)
        {
            var workerReviewModel = new WorkerReviewModel
            {
                WorkingId = workerReviewRequest.WorkingId,
                Comment = workerReviewRequest.Comment,
                Rating = workerReviewRequest.Rating,
                ReviewDate = DateTime.UtcNow
            };

            int reviewId = _reviewRepository.InsertWorkerReview(workerReviewModel);
            if (workerReviewRequest.Images != null && workerReviewRequest.Images.Count != 0)
            {
                foreach (var image in workerReviewRequest.Images)
                {
                    var imageModel = new ImageModel
                    {
                        Picture = image.Picture,
                        Id = image.Id,
                    };

                    _reviewRepository.InsertEmployerReviewPicture(reviewId, imageModel);
                }
            }
            return reviewId;
        }
    }
}
