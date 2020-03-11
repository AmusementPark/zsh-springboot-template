package zsh.springboot.reactiveweb;

import javax.servlet.http.HttpServletRequest;

public interface UserSessionFetchAdapter {
    String operator(HttpServletRequest httpServletRequest);
}
