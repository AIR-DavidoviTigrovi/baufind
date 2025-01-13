using BusinessLogicLayer.AppLogic.Reviews.GetUserReviews;
using BusinessLogicLayer.AppLogic.Reviews.ReviewRequest;
using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Reviews
{
    public interface IReviewService
    {
        public GetUserReviewsResponse GetUserReviews(int userId);
        int SaveEmployerReview(EmployerReviewRequest employerReview);
        public int SaveWorkerReview(WorkerReviewRequest workerReviewRequest);
        public List<ReviewNotificationModel> GetReviewNotifications(int userId);

    }
}
