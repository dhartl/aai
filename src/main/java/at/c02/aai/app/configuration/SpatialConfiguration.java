package at.c02.aai.app.configuration;

import org.locationtech.spatial4j.context.SpatialContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpatialConfiguration {

	@Bean
	public SpatialContext spatialContext() {
		return SpatialContext.GEO;
	}
}
