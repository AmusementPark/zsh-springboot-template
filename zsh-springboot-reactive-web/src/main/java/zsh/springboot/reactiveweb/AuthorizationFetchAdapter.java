package zsh.springboot.reactiveweb;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizationFetchAdapter implements UserSessionFetchAdapter {
    @Override
    public String operator(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Authorization");
    }
}
