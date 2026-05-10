package trainapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import trainapp.security.JwtAuthFilter;
import trainapp.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers("/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/trains").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/trains/*/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/trains/*/delete").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/trains/*/delay").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/routes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/routes/*/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/routes/*/delete").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/schedules").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/schedules/*/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/schedules/*/delete").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,   "/api/trains/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/trains/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/trains/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/api/routes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/routes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/routes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/api/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/schedules/**").hasRole("ADMIN")
                        .requestMatchers("/api/bookings/train/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/trains", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionFixation(sf -> sf.newSession())
                )
                .headers(h -> h.frameOptions(fo -> fo.disable()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}