using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BusinessLogicLayer.AppLogic.Jobs.GetJob
{
    public record GetJobResponse
    {
        public FullJobRecord Job { get; set; } = new FullJobRecord();
        public string? Error { get; set; } = string.Empty;
    }
}
