namespace Lab1.Core.Models
{
    public class Tag
    {
        public string Name { get; } = string.Empty;
        private Tag(string name) => Name = name;
        static public Tag Construct(string name) => new Tag(name);  
    }
}
