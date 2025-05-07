using DistComp_1.Models;

namespace DistComp_1.DTO.ResponseDTO;

public class LabelResponseDTO
{
    public long Id { get; set; }
    public string Name { get; set; }
    
    public List<Label> Labels { get; set; } = [];
}