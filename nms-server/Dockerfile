
from golang:1.25-alpine as builder

workdir /app

copy go.mod go.sum ./
run go mod download

copy server/ ./server/

WORKDIR /app/server/cmd/app

run go build -o /main .

# ---
from alpine:3.20
workdir /root/

copy --from=builder /main .
COPY articles ./articles/


expose 8080
cmd ["./main"]

