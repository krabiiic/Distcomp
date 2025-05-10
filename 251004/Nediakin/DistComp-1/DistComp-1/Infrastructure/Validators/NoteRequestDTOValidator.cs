using DistComp_1.DTO.RequestDTO;
using FluentValidation;

namespace DistComp_1.Infrastructure.Validators;

public class NoteRequestDTOValidator : AbstractValidator<NoteRequestDTO>
{
    public NoteRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}