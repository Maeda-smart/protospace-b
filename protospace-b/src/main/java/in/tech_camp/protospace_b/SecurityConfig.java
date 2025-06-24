package in.tech_camp.protospace_b;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        //ここに記述されたGETリクエストは許可されます（ログイン不要です)
                        .requestMatchers(HttpMethod.GET, "/css/**", "/javascript/**", "/favicon.ico", "/image/**", "/", "/uploads/**", "/users/sign_up", "/users/login", "/users/{userId:[0-9]+}", "/prototypes/{prototypeId:[0-9]+}/detail").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .anyRequest().authenticated())
                        //上記以外のリクエストは認証されたユーザーのみ許可されます(要ログイン)

                .formLogin(login -> login
                        .loginProcessingUrl("/login")
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/users/login?error")
                        .usernameParameter("email")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        //ログアウトボタンを押した際のパスを設定しています
                        .logoutSuccessUrl("/"));
                        //ログアウト成功時のリダイレクト先です

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public AuthenticationSuccessHandler authenticationSuccessHandler(){
    //     return (request, response, authentication)->{
    //         CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

    //         response.setStatus(HttpServletResponse.SC_OK);
    //         response.setContentType("application/json");
    //         response.setCharacterEncoding("UTF-8");
    //         response.getWriter().write(String.format("{\"id\":%d,\"nickname\":\"%s\",\"email\":\"%s\"}",
    //             userDetails.getId(),
    //             userDetails.getNickname(),
    //             userDetails.getUsername()
    //         ));
    //     };
    // }
}
