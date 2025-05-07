using Discussion.DTO.Request;
using FluentValidation;

namespace Discussion.Infrastructure.Validators;

public class PostRequestDtoValidator : AbstractValidator<PostRequestDTO>
{
    public PostRequestDtoValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}