using DistComp.DTO.RequestDTO;
using FluentValidation;

namespace DistComp.Infrastructure.Validators;

public class StickerRequestDTOValidator : AbstractValidator<StickerRequestDTO>
{
    public StickerRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}