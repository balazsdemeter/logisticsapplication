package hu.cubix.balage.logisticsapplication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import hu.cubix.balage.logisticsapplication.config.LogisticsConfigurationProperties;
import hu.cubix.balage.logisticsapplication.config.LogisticsConfigurationProperties.JwtData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    public static final String AUTH = "auth";
    private final LogisticsConfigurationProperties properties;

    public JwtService(LogisticsConfigurationProperties properties) {
        this.properties = properties;
    }

    public String createJwt(UserDetails userDetails) {
        JwtData jwtData = properties.getJwtData();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withArrayClaim(AUTH, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtData.getDuration().toMillis()))
                .withIssuer(jwtData.getIssuer())
                .sign(Algorithm.HMAC256(jwtData.getSecret()));
    }

    public UserDetails parseJwt(String jwtToken) {
        JwtData jwtData = properties.getJwtData();

        DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256(jwtData.getSecret()))
                .withIssuer(jwtData.getIssuer())
                .build()
                .verify(jwtToken);

        return new LogisticsUser(decodedJwt.getSubject(), "dummy", decodedJwt.getClaim(AUTH).asList(String.class).stream().map(SimpleGrantedAuthority::new).toList());
    }
}