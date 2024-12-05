﻿using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs
{
    public interface IJobService
    {
        public AddJobResponse AddJob(AddJobRequest request, int user_id);
    }
}