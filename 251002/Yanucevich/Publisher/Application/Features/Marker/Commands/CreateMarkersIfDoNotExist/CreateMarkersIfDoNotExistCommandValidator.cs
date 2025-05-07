using System;
using FluentValidation;

namespace Application.Features.Marker.Commands;

public class CreateMarkersIfDoNotExistCommandValidator
    : AbstractValidator<CreateMarkersIfDoNotExistCommand>
{
    public CreateMarkersIfDoNotExistCommandValidator()
    {
        RuleForEach(x => x.markers)
            .ChildRules(v =>
            {
                v.RuleFor(x => x.Name).NotEmpty().Length(2, 32);
            });
    }
}
