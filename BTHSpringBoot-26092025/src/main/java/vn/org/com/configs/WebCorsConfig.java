package vn.org.com.configs;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("*")   // hoặc giới hạn origin của bạn
            .allowedMethods("GET","POST","PUT","DELETE","PATCH");
  }
}
