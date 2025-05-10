FROM golang AS builder
WORKDIR /app
COPY . .
RUN go mod tidy && go build -o notes_service

FROM gcr.io/distroless/base-debian11
WORKDIR /root/
COPY --from=builder /app/notes_service .
CMD ["./notes_service"]
