using DistComp.DTO.RequestDTO;
using FluentValidation;

namespace DistComp.Infrastructure.Validators;

public class MarkRequestDTOValidator : AbstractValidator<MarkRequestDTO>
{
    public MarkRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}