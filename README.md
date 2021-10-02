# backend-clock

A spring boot application that serves 2 RESTful endpoints.

`/api/v1/time/server` -- returns date, time and timezone of the server.
- Method: `GET`
- Response:
  - format: JSON, keys: `time`, `date`, `timezone`
  - `time`: current time of the server in `HH:mm:ss` format
  - `date`: date of the sever in `dd/MM/yyyy` format
  - `timezone`: timezone name of the server

`/api/v1/time/manila` -- returns time of the sever converted into `Asia/Manila` timezone. Uses public API for time conversiont - [TimezoneDB API](https://timezonedb.com/references/convert-time-zone)
- Method: `GET`
- Response:
  - format: JSON, keys: `time`
  - `time`: time of the server converted into `Asia/Manila` timezone in `HH:mm:ss` format

## Configuration

The application reads 2 environment variables upon running.

`TIMEZONEDB_API_KEY` -- API key provided by TimezoneDB

`TIMEZONE_LOCAL` -- Default: `Asia/Manila`, the destination timezone of the conversion

## Run the application

From the root directory of the repository, run 
```
./mvnw spring-boot run
```

## Build the application
From the root directory of the repository, run 
```
./mvnw package
```

## Build the Docker image

Make sure you already built the application before creating an image.
```
docker build -t <TAG> .
```