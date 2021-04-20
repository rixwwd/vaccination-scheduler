package com.github.rixwwd.vaccination_scheduler.admin.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

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
			      .logoutSuccessUrl("/signIn")
			.and().sessionManagement()
			      .sessionFixation().changeSessionId()
			      .maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/signIn?sessionExpired");
		//@formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico", "/js/**", "/css/**", "/images/**");
	}

	@Bean
	@ConditionalOnMissingBean(SessionRegistry.class)
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
