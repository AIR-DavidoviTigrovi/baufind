namespace DataAccessLayer.Models
{
    /// <summary>
    /// Model za tablicu "Skill"
    /// </summary>
    public record SkillModel
    {
        public int Id { get; set; }
        public string Title { get; set; }
    }
}
