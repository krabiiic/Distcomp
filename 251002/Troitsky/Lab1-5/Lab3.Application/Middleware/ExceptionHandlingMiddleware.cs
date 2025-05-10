using Lab3.Application.Exceptions;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;

namespace Lab3.Application.Middleware
{
    public class ExceptionHandlingMiddleware
    {
        private readonly RequestDelegate _next;
        public ExceptionHandlingMiddleware(RequestDelegate next)
        {
            // next - ссылка на следующий middleware в цепочке
            _next = next;
        }

        // передаёт запрос дальше, в случае исключения отлавливает
        public async Task InvokeAsync(HttpContext httpContext)
        {
            try
            {
                await _next(httpContext);
            }
            catch (IncorrectDataException ex)
            {
                httpContext.Response.ContentType = "application/json";
                httpContext.Response.StatusCode = StatusCodes.Status400BadRequest;

                // детали из цепочки исключений
                var errorMessages = GetErrorMessages(ex);

                var errorResponse = new
                {
                    message = "Bad request.",
                    details = errorMessages,
                    occuredAt = ex.StackTrace
                };

                await httpContext.Response.WriteAsync(JsonConvert.SerializeObject(errorResponse, Formatting.Indented));
            }
            catch (IncorrectDatabaseException ex)
            {
                httpContext.Response.ContentType = "application/json";
                httpContext.Response.StatusCode = StatusCodes.Status403Forbidden;


                var errorResponse = new
                {
                    message = "An internal error occurred.",
                    details = GetErrorMessages(ex),
                    occuredAt = ex.StackTrace
                };

                // Formatting.Indented – делает JSON читаемым, добавляя отступы и переносы строк
                await httpContext.Response.WriteAsync(JsonConvert.SerializeObject(errorResponse, Formatting.Indented));
            }
            catch (Exception ex)
            {
                httpContext.Response.ContentType = "application/json";
                httpContext.Response.StatusCode = StatusCodes.Status500InternalServerError;


                var errorResponse = new
                {
                    message = "An internal error occurred.",
                    details = GetErrorMessages(ex),
                    occuredAt = ex.StackTrace
                };

                await httpContext.Response.WriteAsync(JsonConvert.SerializeObject(errorResponse, Formatting.Indented));
            }
        }

        static private List<string> GetErrorMessages(Exception ex)
        {
            var messages = new List<string>();
            while (ex != null)
            {
                messages.Add(ex.Message);
                ex = ex.InnerException;
            }
            return messages;
        }
    }
}
