using DistComp.Models;

namespace DistComp.DTO.ResponseDTO;

public class StickerResponseDTO
{
    public long Id { get; set; }
    public string Name { get; set; }
    
    public List<Issue> Issues { get; set; } = [];
}