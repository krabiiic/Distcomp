FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

COPY ["Lab3.MessageService/", "Lab3.MessageService/"]
COPY ["Lab3.Infrastructure/", "Lab3.Infrastructure/"]
COPY ["Lab3.Application/", "Lab3.Application/"]
COPY ["Lab3.Core/", "Lab3.Core/"]

RUN dotnet restore "Lab3.MessageService/Lab3.MessageService.csproj"


WORKDIR /src/Lab3.MessageService
RUN dotnet build -c Release -o /app/build

FROM build AS publish
RUN dotnet publish -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Lab3.MessageService.dll"]