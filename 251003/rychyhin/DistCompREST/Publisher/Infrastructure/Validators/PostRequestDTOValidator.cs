using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class NoteRequestDTOValidator : AbstractValidator<NoteRequestDTO>
{
    public NoteRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}