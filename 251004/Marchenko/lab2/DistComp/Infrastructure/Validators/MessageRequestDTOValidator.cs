using DistComp.DTO.RequestDTO;
using FluentValidation;

namespace DistComp.Infrastructure.Validators;

public class MessageRequestDTOValidator : AbstractValidator<MessageRequestDTO>
{
    public MessageRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}