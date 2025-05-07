using Publisher.DTO.RequestDTO;
using FluentValidation;

namespace Publisher.Infrastructure.Validators;

public class IssueRequestDTOValidator : AbstractValidator<IssueRequestDTO>
{
    public IssueRequestDTOValidator()
    {
        RuleFor(dto => dto.Title).Length(2, 64);
        RuleFor(dto => dto.Content).Length(4, 2048);
    }
}