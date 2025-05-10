using DistComp.DTO.RequestDTO;
using FluentValidation;

namespace DistComp.Infrastructure.Validators;

public class LabelRequestDTOValidator : AbstractValidator<LabelResponseDTO>
{
    public LabelRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}