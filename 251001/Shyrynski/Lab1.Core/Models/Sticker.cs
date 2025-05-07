namespace Lab1.Core.Models
{
    public class Sticker
    {
        public string Name { get; } = string.Empty;
        private Sticker(string name) => Name = name;
        static public Sticker Construct(string name) => new Sticker(name);  
    }
}
