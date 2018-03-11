package at.c02.aai.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class WebConfiguration {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
	CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
	loggingFilter.setIncludeClientInfo(true);
	loggingFilter.setIncludeQueryString(true);
	loggingFilter.setIncludePayload(true);
	loggingFilter.setIncludeHeaders(false);
	return loggingFilter;
    }
}
