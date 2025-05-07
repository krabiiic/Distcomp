FROM golang:1.21 AS builder
WORKDIR /app
COPY . .
RUN go mod tidy && go build -o main_service

FROM gcr.io/distroless/base-debian11
WORKDIR /root/
COPY --from=builder /app/main_service .
CMD ["./main_service"]
