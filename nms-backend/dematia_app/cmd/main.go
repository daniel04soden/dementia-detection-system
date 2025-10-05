package main

import (
    "log"
    "database/sql"

    "github.com/gofiber/fiber/v2"
    _ "github.com/lib/pq"
)

const (
    host = "localhost"
    port = 4000
    user = "postgres"
    password = secret
    dbname = ""
    )


func main() {

    app := fiber.New()

    app.Get("/", func(c *fiber.Ctx) error {
        return c.SendString("Hello, World 👋!")
    })

    log.Fatal(app.Listen(":3000"))
}
