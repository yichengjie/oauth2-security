package com.yicj.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//由于Spring boot starter自动装配机制，
// 这里无需使用@EnableWebSecurity，WebSecurityConfig内容如下
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Spring Security可以通过 http.authorizeRequests() 对web请求进行授权保护
    //Spring Security使用标准Filter建立了对web请求的拦截，最终实现对资源的授权访问
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // url匹配/r/**的资源，经过认证后才能访问
                .antMatchers("/r/**").authenticated()
                // 其他url完全开放
                .anyRequest().permitAll()
                .and()
                //支持form表单认证，认证成功后转向/login-success
                .formLogin()
                    .loginPage("/login-view")
                    .loginProcessingUrl("/login")
                    .successForwardUrl("/login-success")
                    .permitAll()
                    .and()
                //屏蔽CSRF控制，即spring security不再限制CSRF
                .csrf().disable()
        ;
    }
}
