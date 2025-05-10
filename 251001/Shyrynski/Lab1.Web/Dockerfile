FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

COPY ["Lab1.Web/", "Lab1.Web/"]
COPY ["Lab1.Infrastructure/", "Lab1.Infrastructure/"]
COPY ["Lab1.Application/", "Lab1.Application/"]
COPY ["Lab1.Core/", "Lab1.Core/"]

RUN dotnet restore "Lab1.Web/Lab1.Web.csproj"


WORKDIR /src/Lab1.Web
RUN dotnet build -c Release -o /app/build

FROM build AS publish
RUN dotnet publish -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Lab1.Web.dll"]