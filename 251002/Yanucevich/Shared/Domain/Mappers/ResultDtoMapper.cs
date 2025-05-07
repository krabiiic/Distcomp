using Shared.Domain;

namespace Shared.Domain.Mappers;

public static class ResultDtoMapper
{
    public static Result ToResult(this ResultDto result)
    {
        return result.IsSuccess ? Result.Success() : Result.Failure(result.Error.ToError());
    }

    public static Result<TValue> ToResult<TValue>(this ResultDto<TValue> result)
    {
        return result.IsSuccess
            ? Result.Success<TValue>(result.Value)
            : Result.Failure<TValue>(result.Error.ToError());
    }

    public static ResultDto ToResultDto(this Result result)
    {
        return new ResultDto
        {
            IsSuccess = result.IsSuccess,
            Error = result.IsSuccess ? Error.None.ToErrorDto() : result.Error.ToErrorDto(),
        };
    }

    public static ResultDto<TValue> ToResultDto<TValue>(this Result<TValue> result)
    {
        return new ResultDto<TValue>
        {
            IsSuccess = result.IsSuccess,
            Error = result.IsSuccess ? Error.None.ToErrorDto() : result.Error.ToErrorDto(),
            Value = result.IsSuccess ? result.Value : default,
        };
    }
}
