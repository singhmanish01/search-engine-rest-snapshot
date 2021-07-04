package com.appperfect.searchengine.security;

import com.appperfect.searchengine.service.CatalogerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CatalogerService catalogerService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("auther: " + auth.toString());
        auth.userDetailsService(catalogerService);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/search","/register","/add-user","/download/document").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/showMyLoginPage")
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
        .defaultSuccessUrl("/",true);
	}




    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		UserBuilder users = User.withDefaultPasswordEncoder();
//
//		auth.inMemoryAuthentication()
//		.withUser(users.username("capSteve").password("test101").roles("EMPLOYEE"))
//		.withUser(users.username("thorAsguard").password("test101").roles("MANAGER"))
//		.withUser(users.username("tony").password("test101").roles("ADMIN"));
//	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
