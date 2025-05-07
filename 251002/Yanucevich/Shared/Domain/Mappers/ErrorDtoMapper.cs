namespace Shared.Domain.Mappers;

public static class ErrorDtoMapper
{
    public static ErrorDto ToErrorDto(this Error error)
    {
        return new ErrorDto
        {
            HttpStatusCode = error.HttpStatusCode,
            ErrorTarget = error.ErrorTarget,
            ErrorClass = error.ErrorClass,
            ErrorMessage = error.Message,
        };
    }

    public static Error ToError(this ErrorDto dto)
    {
        return new Error(dto.HttpStatusCode, dto.ErrorTarget, dto.ErrorClass, dto.ErrorMessage);
    }
}
