using DistComp_1.Models;

namespace DistComp_1.DTO.ResponseDTO;

public class CreatorResponseDTO
{
    public long Id { get; set; }
    public string Login { get; set; }
    public string Password { get; set; }
    public string Firstname { get; set; }
    public string Lastname { get; set; }

    public List<Issue> Issues { get; set; } = [];
}