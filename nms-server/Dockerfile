
from golang:1.23-alpine as builder

workdir /app

copy go.mod go.sum ./
run go mod download

copy server/ ./server/
workdir /app/server

run go build -o /main .

# ---
from alpine:3.20
workdir /root/

copy --from=builder /main .


expose 8080
cmd ["./main"]

