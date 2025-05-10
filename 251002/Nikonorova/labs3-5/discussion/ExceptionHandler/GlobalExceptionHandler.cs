using System.ComponentModel.DataAnnotations;
using System.Net;
using discussion.Exceptions;
using discussion.DTO.Response;
using Microsoft.AspNetCore.Diagnostics;

namespace discussion.ExceptionHandler
{
    public class GlobalExceptionHandler(ILogger<GlobalExceptionHandler> logger) : IExceptionHandler
    {
        public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception, CancellationToken cancelToken)
        {
            var errorCode = exception switch
            {
                EntityNotFoundException => (int)HttpStatusCode.BadRequest + "01",
                ValidationException => (int)HttpStatusCode.BadRequest + "03",
                _ => (int)HttpStatusCode.InternalServerError + "00"
            };
            logger.LogWarning(exception, "An error occured while processing the request");
            var response = new ErrorResponseTo(exception.Message, errorCode);
            httpContext.Response.StatusCode = Convert.ToInt32(errorCode[..3]);
            await httpContext.Response.WriteAsJsonAsync(response, cancelToken);
            return true;
        }
    }
}
