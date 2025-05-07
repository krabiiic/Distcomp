using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class LabelRequestDTOValidator : AbstractValidator<LabelRequestDTO>
{
    public LabelRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}