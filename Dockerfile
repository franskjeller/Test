#See https://aka.ms/customizecontainer to learn how to customize your debug container and how Visual Studio uses this Dockerfile to build your images for faster debugging.

# Base image for runtime
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app
EXPOSE 8080

# SDK image for building and copy files
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS copy
WORKDIR /src
COPY . ./

# Build and run tests
FROM copy AS test
WORKDIR "/src/Test.Tests"
RUN dotnet test "./Test.Tests.csproj"

# Build and publish application
FROM test AS publish
ARG BUILD_CONFIGURATION=Release
WORKDIR "/src/Test"
RUN dotnet publish "./Test.csproj" -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

# The final stage where we specify what to execute the Docker container is started
FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Test.dll"]