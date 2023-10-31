package da2i.payetesdettes.authentification;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import da2i.payetesdettes.entities.User;
import da2i.payetesdettes.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Autowired
	UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http.headers().frameOptions().disable(); // for access to h2-console
    	http
    		.csrf().disable()
	    	.formLogin((form) -> form
	    		.loginPage("/login")
				.usernameParameter("email")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login?error=loginFailed")
				.successHandler(new SavedRequestAwareAuthenticationSuccessHandler() {
			         
			        @Override
			        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
			            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			            User user = userDetails.getUser();
			            userService.updateLastLoginDate(user);
			            request.getSession(true).setAttribute("user", user);
			            super.onAuthenticationSuccess(request, response, authentication);
			        }
			    })
				.failureHandler(new SimpleUrlAuthenticationFailureHandler() {

					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
						if (exception.getClass().equals(DisabledException.class)) {
							response.sendRedirect("/login?error=emailNotConfirmed");
						} else if (exception.getClass().equals(BadCredentialsException.class)) {
							response.sendRedirect("/login?error=loginFailed");
						} else {
							super.onAuthenticationFailure(request, response, exception);
						}
						
					}
					
				})
				.permitAll())
				
			.logout( (logout) -> logout
					.permitAll()
					.logoutSuccessUrl("/login?success=logout"))
			.authorizeRequests()
				.requestMatchers("/login**","/register**","/init","/user/confirmEmail/**","/forgotPassword**").permitAll()
				.requestMatchers("/*").authenticated();
    	
	
		return http.build();
    }

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public HttpFirewall getHttpFirewall() {
	    StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
	    strictHttpFirewall.setAllowSemicolon(true);
	    strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
	    return strictHttpFirewall;
	}

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
