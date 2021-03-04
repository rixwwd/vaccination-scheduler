package com.github.rixwwd.vaccination_scheduler.admin.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
			      .defaultSuccessUrl("/menu/", true)
		    .and().logout()
			      .logoutUrl("/signOut")
			      .logoutSuccessUrl("/signIn");
		//@formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico", "/js/**", "/css/**", "/images/**");
	}

}
