package com.simbirsoft.chat.config;

import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.service.auth.*;
import com.simbirsoft.chat.service.auth.oauth2.ApplicationOAuth2UserService;
import com.simbirsoft.chat.service.http.NoRedirectStrategy;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final int BCRYPT_ROUNDS = 4;

    private final UserRepository userRepository;

    private final SuccessLoginHandler successLoginHandler;

    private final FailureLoginHandler failureLoginHandler;

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    private final ApplicationOAuth2UserService applicationOAuth2UserService;

    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/messages/**"),
            new AntPathRequestMatcher("/api/send")
    );

    public SecurityConfig(
            UserRepository userRepository,
            SuccessLoginHandler successLoginHandler,
            FailureLoginHandler failureLoginHandler,
            TokenAuthenticationProvider tokenAuthenticationProvider,
            ApplicationOAuth2UserService applicationOAuth2UserService) {
        this.userRepository = userRepository;
        this.successLoginHandler = successLoginHandler;
        this.failureLoginHandler = failureLoginHandler;
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
        this.applicationOAuth2UserService = applicationOAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/" + UserConfig.AVATAR_PATH + "/**").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/signup").anonymous()

                // Rest API authentication config
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
                .and()
                .authenticationProvider(tokenAuthenticationProvider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()

                .requestMatchers(PROTECTED_URLS)
                .authenticated()

                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successLoginHandler)
                .failureHandler(failureLoginHandler)
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")

                .and()
                .oauth2Login()
                .loginPage("/login/google")
                .userInfoEndpoint()
                .userService(applicationOAuth2UserService);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(daoAuthenticationProvider());
        providers.add(tokenAuthenticationProvider);

        return new ProviderManager(providers);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoderMap = new HashMap<>();
        encoderMap.put("bcrypt", new BCryptPasswordEncoder(BCRYPT_ROUNDS));

        return new DelegatingPasswordEncoder("bcrypt", encoderMap);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return new RepositoryUserDetailsService(repository);
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));

        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(tokenAuthenticationProvider);
    }

    @Bean
    TokenAuthenticationFilter restAuthenticationFilter() {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());

        return filter;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());

        return successHandler;
    }

    @Bean
    FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);

        return registration;
    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}
