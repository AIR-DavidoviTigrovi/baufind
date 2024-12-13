using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.AppLogic
{
    public interface IReviewRepository
    {
        public List<UserReviewModel> GetWorkerReviews(int userId);
        public List<UserReviewModel> GetEmployerReviews(int userId);
    }
}
