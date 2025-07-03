package in.tech_camp.protospace_b;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// ログインエラーをモデルに渡す
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof DisabledException) {
            request.getSession().setAttribute("error", "アカウントが凍結されています。管理者にお問い合わせください。");
        } else {
            request.getSession().setAttribute("error", "ユーザー名またはパスワードが正しくありません。");
        }
        response.sendRedirect("/users/login?error");
    }
}