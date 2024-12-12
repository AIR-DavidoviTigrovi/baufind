using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    /// <summary>
    /// JobSearchModel je posao koji se prikazuje na pretraživanju poslova
    /// </summary>
    public record JobSearchModel
    {
        public int Id { get; set; }
        public int Employer_id { get; set; }
        public int Job_status_id { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public bool Allow_worker_invite { get; set; }
        public string Location { get; set; }
        public decimal? Lat { get; set; }
        public decimal? Lng { get; set; }
        public List<byte[]> Pictures { get; set; }
        public List<SkillModel> Skills { get; set; }
    }
}
