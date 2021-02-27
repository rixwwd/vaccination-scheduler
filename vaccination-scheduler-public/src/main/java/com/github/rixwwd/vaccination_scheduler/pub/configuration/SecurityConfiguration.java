package com.github.rixwwd.vaccination_scheduler.pub.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http.authorizeRequests().antMatchers("/signIn").permitAll()
		    .and().authorizeRequests().anyRequest().authenticated()
		    .and().formLogin()
		          .loginPage("/signIn")
		          .usernameParameter("username")
			      .passwordParameter("password")
			      .loginProcessingUrl("/signIn")
			      .defaultSuccessUrl("/menu/")
		    .and().logout()
			      .logoutUrl("/signOut")
			      .logoutSuccessUrl("/");
		//@formatter:on
	}
}
