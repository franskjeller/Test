#See https://aka.ms/customizecontainer to learn how to customize your debug container and how Visual Studio uses this Dockerfile to build your images for faster debugging.

# Here we declare what base image we'll use to build our Docker image
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
USER app
WORKDIR /app
EXPOSE 8080

# Here we run tests
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS test

# Copy everything into src with the same structure
WORKDIR /src
COPY . ./

WORKDIR "/src/Test.Tests"
RUN dotnet restore "./Test.Tests.csproj"
RUN dotnet test "./Test.Tests.csproj"


# Here we build our application and copy files to container under src-directory
FROM test AS build
ARG BUILD_CONFIGURATION=Release
WORKDIR "/src/Test"
RUN dotnet restore "./Test.csproj"
RUN dotnet build "./Test.csproj" -c $BUILD_CONFIGURATION -o /app/build

# Here we publish our built application
FROM build AS publish
ARG BUILD_CONFIGURATION=Release
RUN dotnet publish "./Test.csproj" -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

# The final stage where we specify what to execute the Docker container is started
FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Test.dll"]