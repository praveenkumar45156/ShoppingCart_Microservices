package com.kumarp.discoveryserver.config;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;

//import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig {

//    @Value("${eureka.username}")
//    private String username;
//    @Value("${eureka.password}")
//    private String password;
    /*@Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.inMemoryAuthentication ()
                .passwordEncoder ( NoOpPasswordEncoder.getInstance () )
                .withUser ( username ).password ( password )
                .authorities ( "USER" );
    }*/

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username(username)
//                .password(password)
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity.csrf ().disable ()
//                .authorizeRequests ().anyRequest ()
//                .authenticated ()
//                .and ()
//                .httpBasic ();
//    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
//        return http.build();
//    }
}
