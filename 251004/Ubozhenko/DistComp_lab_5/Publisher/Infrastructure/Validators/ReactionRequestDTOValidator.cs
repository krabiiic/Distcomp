using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class ReactionRequestDTOValidator : AbstractValidator<ReactionRequestDTO>
{
    public ReactionRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}