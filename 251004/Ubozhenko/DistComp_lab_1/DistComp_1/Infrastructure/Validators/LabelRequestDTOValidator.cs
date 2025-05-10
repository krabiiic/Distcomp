using DistComp_1.DTO.RequestDTO;
using FluentValidation;

namespace DistComp_1.Infrastructure.Validators;

public class LabelRequestDTOValidator : AbstractValidator<LabelRequestDTO>
{
    public LabelRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}