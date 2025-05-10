using FluentValidation;
using Lab1.Core.Models;

namespace Lab1.Application.Validators
{
    public class TagValidator : AbstractValidator<Tag>
    {
        public TagValidator()
        {
            RuleFor(s => s.Name).MinimumLength(2).MaximumLength(32);
        }
    }
}
