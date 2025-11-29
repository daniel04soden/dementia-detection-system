
package middleware

import (
    "context"
    "net/http"
    "strings"

    "nms-server/server/internal/auth"
)

func AuthMiddleware(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        var token string

        authHeader := r.Header.Get("Authorization")
        if strings.HasPrefix(authHeader, "Bearer ") {
            token = strings.TrimPrefix(authHeader, "Bearer ")
        } else {
            cookie, err := r.Cookie("auth_token")
            if err == nil {
                token = cookie.Value
            }
        }

        if token == "" {
            http.Error(w, "Missing token", http.StatusUnauthorized)
            return
        }

        claims, err := auth.ValidateJWT(token)
        if err != nil {
            http.Error(w, "Invalid or expired token", http.StatusUnauthorized)
            return
        }

        ctx := context.WithValue(r.Context(), "userID", claims.UserID)
        next.ServeHTTP(w, r.WithContext(ctx))
    })
}


