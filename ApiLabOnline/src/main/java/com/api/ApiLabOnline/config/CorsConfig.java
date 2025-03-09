package com.api.ApiLabOnline.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

//    @Bean
//    CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOriginPattern("*");
//        config.addAllowedHeader("*");
//        config.addExposedHeader("*");
//        config.addAllowedMethod("*");
////        config.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type"));
////        config.setExposedHeaders(Arrays.asList("X-Get-Header"));
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); 
//        source.registerCorsConfiguration("/**", config);
//        
//        return new CorsFilter(source);
//        
//    }
    
	@Bean
	WebMvcConfigurer mvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
				.allowedOrigins("*")
				.allowedMethods("*");
				registry.addMapping("/auth/**")
				.allowedOrigins("*")
				.allowedMethods("*");
			}
		};
	}
}
