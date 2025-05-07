using FluentValidation;
using Lab1.Core.Models;
namespace Lab1.Application.Validators
{
    public class CreatorValidator : AbstractValidator<Creator>
    {
        public CreatorValidator()
        {
            RuleFor(c => c.FirstName).MinimumLength(2).MaximumLength(64);
            RuleFor(c => c.LastName).MinimumLength(2).MaximumLength(64);
            RuleFor(c => c.Login).MinimumLength(2).MaximumLength(64);
            RuleFor(c => c.Password).MinimumLength(8).MaximumLength(128);
        }
    }
}
