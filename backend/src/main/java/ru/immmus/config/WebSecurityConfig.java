package ru.immmus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.immmus.domain.Role;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.authorizeRequests()
                *//*разрешает заходить на http://www.nash.ru/  без авторизации*//*
                .mvcMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();*/
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/admin_panel/**").hasAuthority(Role.ADMIN.getAuthority())
                .antMatchers("/", "/login**", "/js/**", "/error**").permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();
    }
}
