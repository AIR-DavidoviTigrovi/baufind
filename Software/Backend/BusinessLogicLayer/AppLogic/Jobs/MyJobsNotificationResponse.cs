using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs
{
    public record MyJobsNotificationResponse
    {
        public List<MyJobNotificationModel> NotificationModels {  get; set; }
        public string Message { get; set; }
    }
}
