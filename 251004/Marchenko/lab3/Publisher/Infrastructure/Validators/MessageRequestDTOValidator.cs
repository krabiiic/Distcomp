using Publisher.DTO.RequestDTO;
using FluentValidation;

namespace Publisher.Infrastructure.Validators;

public class MessageRequestDTOValidator : AbstractValidator<MessageRequestDTO>
{
    public MessageRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}