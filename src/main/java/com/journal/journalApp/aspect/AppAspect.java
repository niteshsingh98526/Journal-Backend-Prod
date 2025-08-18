package com.journal.journalApp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(0)
@Aspect
@Slf4j
public class AppAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)"
            + " || @annotation(org.springframework.web.bind.annotation.PostMapping)" + " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" + " || @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void selectAll() {}

    @Before("selectAll()")
    public void before (JoinPoint joinPoint) {
        log.info("Before method invoked::"+joinPoint.getSignature());
    }
    @After("selectAll()")
    public void after (JoinPoint joinPoint) {
        log.info("After method invoked::"+joinPoint.getSignature());
    }
}

