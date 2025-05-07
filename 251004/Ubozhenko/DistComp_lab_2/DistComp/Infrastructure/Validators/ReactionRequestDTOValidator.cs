using DistComp.DTO.RequestDTO;
using FluentValidation;

namespace DistComp.Infrastructure.Validators;

public class ReactionRequestDTOValidator : AbstractValidator<ReactionRequestDTO>
{
    public ReactionRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}