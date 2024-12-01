using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Users.GetUserProfile
{
    public class UserProfileResponse
    {
        public UserProfileModel? userProfileModel { get; set; } = null;
        public string? Error { get; set; } = string.Empty;

    }
}
