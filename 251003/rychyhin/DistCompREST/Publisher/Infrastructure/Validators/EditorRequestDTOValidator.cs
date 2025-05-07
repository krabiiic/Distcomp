using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class EditorRequestDTOValidator : AbstractValidator<EditorRequestDTO>
{
    public EditorRequestDTOValidator()
    {
        RuleFor(dto => dto.Login).Length(2, 64);
        RuleFor(dto => dto.Password).Length(8, 128);
        RuleFor(dto => dto.Firstname).Length(2, 64);
        RuleFor(dto => dto.Lastname).Length(2, 64);
    }
}