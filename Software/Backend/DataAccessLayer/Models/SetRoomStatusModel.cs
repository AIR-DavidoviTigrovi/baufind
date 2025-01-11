using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models {
    public record SetRoomStatusModel {
        public string? Success { get; set; } = string.Empty;
        public string? Error { get; set; } = string.Empty;
    }
}
