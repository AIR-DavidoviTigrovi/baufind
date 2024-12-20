﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Users.UpdateUserProfile
{
    /// <summary>
    /// Response koji će se vratiti nakon ažuriranja korisničkog računa
    /// </summary>
    public record UpdateUserResponse
    {
        public bool Success { get; set; }
        public string? Message { get; set; }
        public List<string>? Errors { get; set; }
    }
}
