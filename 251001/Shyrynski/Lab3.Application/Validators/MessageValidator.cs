using FluentValidation;
using Lab3.Core.Models;

namespace Lab3.Application.Validators
{
    public class MessageValidator : AbstractValidator<Message>
    {
        public MessageValidator()
        {
            RuleFor(m => m.Content).MinimumLength(2).MaximumLength(2048);
        }
    }
}
