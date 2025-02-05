package dev.ppondeu.java_starter.config;

import dev.ppondeu.java_starter.common.interfaces.IJwtService;
import dev.ppondeu.java_starter.filters.AccessTokenFilter;
import dev.ppondeu.java_starter.filters.RefreshTokenFilter;
import dev.ppondeu.java_starter.users.interfaces.IUserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final IJwtService jwtService;
    private final IUserService userService;

    public FilterConfig(IJwtService jwtService, IUserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Bean
    public FilterRegistrationBean<AccessTokenFilter> jwtAccessFilter() {
        FilterRegistrationBean<AccessTokenFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AccessTokenFilter(jwtService, userService));
        filterRegistrationBean.addUrlPatterns("/api/users/*", "/api/pictures/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<RefreshTokenFilter> jwtRefreshFilter() {
        FilterRegistrationBean<RefreshTokenFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RefreshTokenFilter(jwtService, userService));
        filterRegistrationBean.addUrlPatterns("/api/auth/refresh-token", "/api/auth/logout");
        return filterRegistrationBean;
    }
}
