package in.tech_camp.protospace_b;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http               
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 管理者専用ページ
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // モデレータ or 管理者が入れる
                        .requestMatchers("/moderate/**").hasAnyRole("MODERATOR", "ADMIN")
                        // ここに記述されたGETリクエストは許可されます（ログイン不要です)
                        .requestMatchers(HttpMethod.GET, "/css/**", "/javascript/**", "/favicon.ico", "/image/**", "/",
                                "/uploads/**", "/users/sign_up", "/users/login", "/prototypes/search",
                                "/prototypes/ranking", "/users/{userId:[0-9]+}",
                                "/prototypes/{prototypeId:[0-9]+}/detail")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .anyRequest().authenticated())
                // 上記以外のリクエストは認証されたユーザーのみ許可されます(要ログイン)

                .formLogin(login -> login
                        .loginProcessingUrl("/login")
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/", true)
                        // .failureUrl("/users/login?error")
                        .failureHandler(customAuthenticationFailureHandler()) // カスタムハンドラー設定
                        .usernameParameter("email")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        // ログアウトボタンを押した際のパスを設定しています
                        .logoutSuccessUrl("/"));
        // ログアウト成功時のリダイレクト先です

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ログインエラーのカスタムハンドラー
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
