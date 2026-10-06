package com.banking.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : JwtService
//
//  Description         : Provides JWT-related operations for the
//                        authentication system.
//
//                        It generates JWT tokens, extracts user
//                        information from tokens, and validates
//                        whether a token is valid.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

@Service
public class JwtService
{
    // Stores the secret key used for signing and validating JWT tokens
    @Value("${jwt.secret-key}")
    private String secretKey;

    // Stores the JWT token expiration time of one hour
    private final long expirationTime = 1000 * 60 * 60;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getSigningKey
    //
    //  Description         : Creates and returns the secret signing
    //                        key used for JWT token operations.
    //
    //                        The configured secret key is converted
    //                        into bytes using UTF-8 encoding and then
    //                        used to create an HMAC signing key.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    private SecretKey getSigningKey()
    {
        // Convert the secret key into UTF-8 bytes and create an HMAC key
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : generateToken
    //
    //  Description         : Generates a JWT token for an authenticated
    //                        user.
    //
    //                        It stores the user's email as the subject
    //                        and the user's role as a custom claim.
    //
    //                        The token also contains the issue time and
    //                        expiration time and is signed using the
    //                        configured secret key.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public String generateToken(String email, String role)
    {
        // Create and configure the JWT token
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : extractEmail
    //
    //  Description         : Extracts the user's email address from
    //                        the JWT token.
    //
    //                        It verifies the token using the signing
    //                        key and retrieves the email stored as
    //                        the token subject.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public String extractEmail(String token)
    {
        // Parse and verify the JWT token
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : isTokenValid
    //
    //  Description         : Checks whether the provided JWT token
    //                        is valid.
    //
    //                        It parses and verifies the token using
    //                        the configured signing key.
    //
    //                        If token validation succeeds, true is
    //                        returned. If an exception occurs, false
    //                        is returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public boolean isTokenValid(String token)
    {
        try
        {
            // Parse and verify the JWT token
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            // Return true when the token is valid
            return true;
        }
        catch (Exception e)
        {
            // Display the JWT validation error
            System.out.println("JWT Validation Error : " + e.getMessage());

            // Return false when the token is invalid
            return false;
        }
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : extractRole
    //
    //  Description         : Extracts the user's role from the JWT
    //                        token.
    //
    //                        It verifies the token and retrieves the
    //                        role stored in the "role" claim.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public String extractRole(String token)
    {
        // Parse the token and extract the role claim
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}