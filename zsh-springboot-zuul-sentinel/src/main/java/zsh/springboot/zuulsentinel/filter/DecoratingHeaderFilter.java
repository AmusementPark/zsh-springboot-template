//package zsh.springboot.zuulsentinel.filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.stereotype.Component;
//
//import java.util.Enumeration;
//
//@Slf4j
//@Component
//public class DecoratingHeaderFilter extends ZuulFilter implements InitializingBean {
//
//    @Override
//    public String filterType() {
//        return FilterConstants.PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        Enumeration<String> headers = ctx.getRequest().getHeaderNames();
//        while (headers.hasMoreElements()) {
//            String header = headers.nextElement();
//            System.out.println(header+" : "+ctx.getRequest().getHeader(header));
//        }
//        ctx.addZuulRequestHeader("ANOTHER-HEADER", "111111");
//        return null;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("++++++++++++++++++++++++++++++");
//    }
//}
