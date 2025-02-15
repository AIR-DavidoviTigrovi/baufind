﻿using DataAccessLayer.Models;
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
        int InsertEmployerReview(EmployerReviewModel employerReview);
        void InsertEmployerReviewPicture(int employerReviewId, ImageModel imageRecord);
        public int InsertWorkerReview(WorkerReviewModel workerReview);
        public List<ReviewNotificationModel> GetAllReviewsToComplete(int userId);

        public void InsertWorkerReviewPictures(int workerReviewId, ImageModel imageRecord);

    }
}
