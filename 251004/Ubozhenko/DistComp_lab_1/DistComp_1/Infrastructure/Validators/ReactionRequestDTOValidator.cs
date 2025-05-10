using DistComp_1.DTO.RequestDTO;
using FluentValidation;

namespace DistComp_1.Infrastructure.Validators;

public class ReactionRequestDTOValidator : AbstractValidator<ReactionRequestDTO>
{
    public ReactionRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}