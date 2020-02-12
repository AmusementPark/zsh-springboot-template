package zsh.springboot.reactiveweb;

import javax.servlet.http.HttpServletRequest;

public class NoUserSessionFetchAdapter implements UserSessionFetchAdapter {
    @Override
    public String operator(HttpServletRequest httpServletRequest) {
        return "";
    }
}
