using DistComp.DTO.RequestDTO;
using FluentValidation;

namespace DistComp.Infrastructure.Validators;

public class NoteRequestDTOValidator : AbstractValidator<NoteRequestDTO>
{
    public NoteRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}