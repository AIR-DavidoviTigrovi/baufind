using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.AppLogic
{
    public interface IPictureRepository
    {
        public void AddJobPicture(int job_id, List<byte[]> pictures);
    }
}
