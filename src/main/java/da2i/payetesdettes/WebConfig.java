package da2i.payetesdettes;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("da2i.payetesdettes")
public class WebConfig implements WebMvcConfigurer {

	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp().prefix("/WEB-INF/jsp/").suffix(".jsp");
	}

   public void addResourceHandlers(ResourceHandlerRegistry registry) {
	   registry.addResourceHandler("/profileImg/**").addResourceLocations("/profileImg/");
	   registry.addResourceHandler("/img/**").addResourceLocations("/img/");
	   registry.addResourceHandler("/js/**").addResourceLocations("/js/");
	   registry.addResourceHandler("/css/**").addResourceLocations("/css/");
	   registry.addResourceHandler("/profileImg/*/**").addResourceLocations("/profileImg/");
	   
	   registry.addResourceHandler("/eventImg/**").addResourceLocations("/eventImg/");
	   registry.addResourceHandler("/eventImg/*/**").addResourceLocations("/eventImg/");
   }
}

