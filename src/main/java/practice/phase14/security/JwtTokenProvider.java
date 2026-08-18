package practice.phase14.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtTokenProvider
{
	private final SecretKey key;
	private final long validityMillis;

	public JwtTokenProvider(String secret, long validityMillis)
	{
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		this.validityMillis = validityMillis;
	}

	public String createToken(String username)
	{
		Date now = new Date();
		Date expiry = new Date(now.getTime() + validityMillis);

		return Jwts.builder().subject(username).issuedAt(now).expiration(expiry).signWith(key).compact();
	}

	public String getUsername(String token)
	{
		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

		return claims.getSubject();
	}

	public boolean isValid(String token)
	{
		try
		{
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
