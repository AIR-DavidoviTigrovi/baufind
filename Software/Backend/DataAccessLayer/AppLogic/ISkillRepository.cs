using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace DataAccessLayer.AppLogic
{
    /// <summary>
    /// Interface za skillove
    /// </summary>
    public interface ISkillRepository
    {
        /// <summary>
        /// Funkcija koja dohvaća sve skillove u bazi
        /// </summary>
        /// <returns>Listu svih skillova</returns>
        public List<SkillModel> GetAll();
        /// <summary>
        /// Dobiva id korisnika od JWT-a  i vraća sve skillove koje korisnik ima navedene u profilu
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Listu skillova koji su vezani za prijavljenog korisnika</returns>
        public List<SkillModel> GetSkillsForUser(int id);
    }
}
