using System.ComponentModel.DataAnnotations;
using System.Net;
using Discussion.Dto.Response;
using Discussion.Exceptions;
using Microsoft.AspNetCore.Diagnostics;
namespace Discussion.ExceptionHandlers;

public class GlobalExceptionHandler(ILogger<GlobalExceptionHandler> logger) : IExceptionHandler
{
    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception,
        CancellationToken cancellationToken)
    {
        var errorCode = exception switch
        {
            EntityNotFoundException => (int)HttpStatusCode.BadRequest + "01",
            ValidationException => (int)HttpStatusCode.BadRequest + "03",
            _ => (int)HttpStatusCode.InternalServerError + "00"
        };
        logger.LogWarning(exception, "An error occurred while processing the request");
        var response = new ErrorResponseTo(exception.Message, errorCode);
        httpContext.Response.StatusCode = Convert.ToInt32(errorCode[..3]);
        await httpContext.Response.WriteAsJsonAsync(response, cancellationToken);
        return true;
    }
}