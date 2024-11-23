namespace DataAccessLayer.Models
{
    /// <summary>
    /// Skill model koji odgovara tablici Skills u bazi podataka.
    /// </summary>
    public class SkillModel
    {
        public int Id { get; set; }
        public string? Title { get; set; }
    }
}