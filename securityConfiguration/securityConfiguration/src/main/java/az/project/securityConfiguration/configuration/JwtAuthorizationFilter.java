package az.project.securityConfiguration.configuration;

import az.project.securityConfiguration.service.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null ) {
                filterChain.doFilter(request, response);
                return;
            }
            //log.info("token: {} ", accessToken);
            Claims claims = jwtUtil.resolveClaims(request);

            if(claims != null & jwtUtil.validateClaims(claims)){
                String user = claims.getSubject();
                Collection<GrantedAuthority> authorities = jwtUtil.extractAuthorities(claims);
                //log.info("user : {} ", user);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user,"",authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //log.info("authentication : {} ", authentication);
            }
        }catch (Exception e){
            log.error("Error due to: {}", e.getClass().getName() + " -> " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
