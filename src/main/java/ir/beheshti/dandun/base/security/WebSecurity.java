package ir.beheshti.dandun.base.security;

import io.swagger.v3.oas.annotations.servers.Server;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Server
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeneralService generalService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/ws").authenticated()
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
