package org.kl.residentevil.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .and()
                .authorizeRequests()
                .antMatchers( "/login", "/register")
                .anonymous()
                .antMatchers(
                        "/",
                        "/css/**",
                        "/js/**",
                        "/index",
                        "/error/**"
                ).permitAll()
                .antMatchers(
                        "/viruses/show",
                        "/viruses/all",
                        "/users/role",
                        "/capitals/all"
                ).authenticated()
                .antMatchers(
                        "/viruses/add",
                        "/viruses/edit/*",
                        "/viruses/delete/*"
                        ).hasAnyAuthority("MODERATOR","ADMIN", "ROOT")
                .antMatchers(
                        "/users/all",
                        "/users/promote/*",
                        "/users/demote/*"
                ).hasAnyAuthority("ADMIN", "ROOT")
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home")
                .and()
                    .logout()
                .and()
                    .exceptionHandling()
                         .accessDeniedPage("/unauthorized");
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}
