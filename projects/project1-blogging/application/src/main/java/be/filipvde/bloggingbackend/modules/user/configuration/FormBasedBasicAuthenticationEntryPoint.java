package be.filipvde.bloggingbackend.modules.user.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

class FormBasedBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    public FormBasedBasicAuthenticationEntryPoint() {
        this("Realm");
    }

    public FormBasedBasicAuthenticationEntryPoint(String realmName) {
        setRealmName(realmName);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "FormBased");
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
}
