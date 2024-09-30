package spring.tree.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info(this.getClass().getName() + ".PasswordEncoder Start!");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info(this.getClass().getName() + ".filterChain Start!");

        http.csrf(AbstractHttpConfigurer::disable)         // POST 방식 전송을 위해 csrf 막기
                .authorizeHttpRequests(authz -> authz // 페이지 접속 권한 설정
                                .requestMatchers("/user/myPage").hasAnyAuthority("ROLE_USER") // USER 권한
                                .requestMatchers("user/v1/**").authenticated() // Spring Security 인증된 사용자만 접근
                                .requestMatchers("tour/tourReg").authenticated() // Spring Security 인증된 사용자만 접근
                                .requestMatchers("tour/tourInfo").authenticated() // Spring Security 인증된 사용자만 접근
                                .requestMatchers("board/boardList").authenticated() // Spring Security 인증된 사용자만 접근
                                .requestMatchers("calendar/info").authenticated() // Spring Security 인증된 사용자만 접근
                                .requestMatchers("/html/user/**").authenticated() // Spring Security 인증된 사용자만 접근
                                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN") // 관리자 권한
                                .requestMatchers("/roomList").authenticated() // 방 리스트 보기 경로 인증 필요
                                .requestMatchers("/roomForm").authenticated() // 방 만들기 폼 경로 인증 필요
                                .requestMatchers("/{roomId}").authenticated() // 특정 방에 들어갈 때 인증 필요
                                .anyRequest().permitAll() // 그 외 나머지 url 요청은 인증 받지 않아도 접속 가능함


                )


                .formLogin(login -> login // 로그인 페이지 설정
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/loginProc")
                        .usernameParameter("userId") // 로그인 ID로 사용할 html의 input객체의 name 값
                        .passwordParameter("password") // 로그인 패스워드로 사용할 html의 input객체의 name 값
                        .successForwardUrl("/user/loginSuccess")
                        .failureUrl("/user/login?error=true")
                )


                .logout(logout -> logout
                        .logoutUrl("/user/v1/logout") // 로그이웃 요청 URL
                        .clearAuthentication(true) // Spring Security 저장된 인증 정보 초기화
                        .invalidateHttpSession(true) // 로그인 후, Controller에서 생성했한 세션(회원아이디 등) 삭제
                        .logoutSuccessUrl("/") // 로그아웃 성공 처리 URL(세션 값 삭제)
                );

        return http.build();
    }
}
