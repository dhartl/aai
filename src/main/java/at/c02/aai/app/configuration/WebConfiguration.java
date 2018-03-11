package at.c02.aai.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
	CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
	loggingFilter.setIncludeClientInfo(true);
	loggingFilter.setIncludeQueryString(true);
	loggingFilter.setIncludePayload(true);
	loggingFilter.setIncludeHeaders(false);
	return loggingFilter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
