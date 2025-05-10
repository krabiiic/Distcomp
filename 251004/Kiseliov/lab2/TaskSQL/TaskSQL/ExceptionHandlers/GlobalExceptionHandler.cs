using System.ComponentModel.DataAnnotations;
using System.Net;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.EntityFrameworkCore;
using TaskSQL.Dto.Response;
using TaskSQL.Exceptions;

namespace TaskSQL.ExceptionHandlers;

public class GlobalExceptionHandler : IExceptionHandler
{
    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception,
        CancellationToken cancellationToken)
    {
        var errorCode = exception switch
        {
            EntityNotFoundException => (int)HttpStatusCode.BadRequest + "01",
            DbUpdateException => (int)HttpStatusCode.Forbidden + "02",
            ValidationException => (int)HttpStatusCode.BadRequest + "03",
            _ => (int)HttpStatusCode.InternalServerError + "00"
        };
        var response = new ErrorResponseTo(exception.Message, errorCode);
        httpContext.Response.StatusCode = Convert.ToInt32(errorCode.Substring(0, 3));
        await httpContext.Response.WriteAsJsonAsync(response, cancellationToken);
        return true;
    }
}