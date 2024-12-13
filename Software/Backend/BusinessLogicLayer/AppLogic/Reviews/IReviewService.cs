using BusinessLogicLayer.AppLogic.Reviews.GetUserReviews;
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
    }
}
