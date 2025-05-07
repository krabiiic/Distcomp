using Publisher.DTO.RequestDTO;
using FluentValidation;

namespace Publisher.Infrastructure.Validators;

public class MarkRequestDTOValidator : AbstractValidator<MarkRequestDTO>
{
    public MarkRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}