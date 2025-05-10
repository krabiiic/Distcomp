namespace MyApp.Services
{
    using Microsoft.AspNetCore.Routing;

    public class PortConstraint : IRouteConstraint
    {
        private readonly int _port;

        public PortConstraint(string port)
        {
            _port = int.Parse(port);
        }

        public bool Match(HttpContext httpContext, IRouter route, string routeKey,
            RouteValueDictionary values, RouteDirection routeDirection)
        {
            return httpContext.Request.Host.Port == _port;
        }
    }
}
