using Discussion.DTO.Request;
using FluentValidation;

namespace Discussion.Infrastructure.Validators;

public class ReactionRequestDtoValidator : AbstractValidator<ReactionRequestDTO>
{
    public ReactionRequestDtoValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}