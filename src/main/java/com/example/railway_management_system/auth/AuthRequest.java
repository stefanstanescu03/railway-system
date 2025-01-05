/** Clasa pentru AuthRequest
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.auth;

public class AuthRequest {

    private String email;
    String parola;

    public AuthRequest(String email, String parola) {
        this.email = email;
        this.parola = parola;
    }

    public AuthRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}
