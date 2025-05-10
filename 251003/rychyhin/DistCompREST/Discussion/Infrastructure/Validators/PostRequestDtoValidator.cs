using Discussion.DTO.Request;
using FluentValidation;

namespace Discussion.Infrastructure.Validators;

public class NoteRequestDtoValidator : AbstractValidator<NoteRequestDTO>
{
    public NoteRequestDtoValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}