package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 관리자 API 실행 전 요청 로그를 기록하는 AOP 클래스
 */
@Slf4j
@Aspect
@Component
public class AdminAccessLoggingAspect {

    /**
     * UserAdminController의 changeUserRole 메소드가 실행되기 전에 로그를 찍음
     *
     * @param joinPoint 실행되는 메소드의 정보를 담고 있음
     */
    @Before("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void logBeforeChangingUserRole(JoinPoint joinPoint) {
        log.info("[ADMIN LOG] changeUserRole() 실행 전 호출됨: {}", joinPoint.getSignature());
    }
}
