using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class PostRequestDTOValidator : AbstractValidator<PostRequestDTO>
{
    public PostRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}