package com.example.notesimple.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class LoggingAspect {

    private val log = LoggerFactory.getLogger(javaClass)

    // 예시: com.example.notesimple.controller 패키지 내 모든 public 메소드 실행 시 로그
    @Around("execution(* com.example.notesimple.controller..*.*(..))")
    fun logControllerExecution(joinPoint: ProceedingJoinPoint): Any? {
        val stopWatch = StopWatch()
        stopWatch.start()

        val signature = joinPoint.signature.toShortString()
        log.info(">>> Entering method: $signature with args: ${joinPoint.args.joinToString()}")

        try {
            val result = joinPoint.proceed() // 원래 메소드 실행
            stopWatch.stop()
            log.info("<<< Exiting method: $signature, Result: $result, Execution Time: ${stopWatch.totalTimeMillis} ms")
            return result
        } catch (e: Throwable) {
            stopWatch.stop()
            log.error("!!! Exception in method: $signature, Execution Time: ${stopWatch.totalTimeMillis} ms", e)
            throw e // 예외는 다시 던져서 전역 핸들러 등에서 처리되도록 함
        }
    }
    // 필요에 따라 Service 계층 등 다른 Pointcut 설정 가능
}