using DistComp_1.DTO.RequestDTO;
using FluentValidation;

namespace DistComp_1.Infrastructure.Validators;

public class MarkRequestDTOValidator : AbstractValidator<MarkRequestDTO>
{
    public MarkRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}