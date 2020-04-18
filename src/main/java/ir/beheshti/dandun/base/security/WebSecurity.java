package ir.beheshti.dandun.base.security;

import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private UserRepository userRepository;
    private GeneralService generalService;

    public WebSecurity(UserDetailsServiceImpl userDetailsService, UserRepository userRepository, GeneralService generalService) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.generalService = generalService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
//                .antMatchers(HttpMethod.POST, SecurityConstants.LOGIN_URL).permitAll()
//                .antMatchers(HttpMethod.POST, "/rest/protocol/ping").hasRole("PING")
//                .antMatchers(HttpMethod.POST, "/rest/protocol/snmpDto").hasRole("SNMP")
//                .antMatchers(HttpMethod.PUT, "/rest/base/access").hasRole("MANAGER")
                .anyRequest().permitAll()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, generalService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

}
