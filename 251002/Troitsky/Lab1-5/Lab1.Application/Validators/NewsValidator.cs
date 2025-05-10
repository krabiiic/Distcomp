using FluentValidation;
using Lab1.Core.Models;

namespace Lab1.Application.Validators
{
    public class NewsValidator : AbstractValidator<News>
    {
        public NewsValidator()
        {
            RuleFor(i => i.Title).MinimumLength(2).MaximumLength(64);
            RuleFor(i => i.Content).MinimumLength(4).MaximumLength(2048);
        }
    }
}
