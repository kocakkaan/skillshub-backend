package com.reply.skillshub.base.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.reply.skillshub.base.logging.StopwatchFilter;

@Configuration
public class FilterConfig {

    private final StopwatchFilter stopwatchFilter;

    public FilterConfig(StopwatchFilter stopwatchFilter) {
        this.stopwatchFilter = stopwatchFilter;
    }

    @Bean
    public FilterRegistrationBean<StopwatchFilter> stopwatchFilterRegistrationBean() {
        final FilterRegistrationBean<StopwatchFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(stopwatchFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
