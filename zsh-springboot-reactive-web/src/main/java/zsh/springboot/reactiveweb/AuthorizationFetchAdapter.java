package zsh.springboot.reactiveweb;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizationFetchAdapter implements UserSessionFetchAdapter {
    @Override
    public String operator(HttpServletRequest httpServletRequest) {
        // Servlet方式拦截
//        HttpServletRequest request = attributes.getRequest();
//        authorization = request.getHeader("Authorization");
//        UserActionLoggerAspectModel userActionLoggerAspectModel = logThreadLocal.get();
//        if (authorization != null) {
//            Claims claims = Jwts.parser().setSigningKey("unv9145n_2.d8@ax").parseClaimsJws(authorization.substring(7)).getBody();
//            String account = claims.get("userAccount", String.class);
//            recordModel.setAccount(account);
//        }
        return httpServletRequest.getHeader("Authorization");
    }
}
