package br.com.trapmonitor.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Aroud means that we are adding extra code both before and after the method execution.
 * after the annotation has a point cut argument: "apply this advice any method witch is annotated with @LogExecutionTime
 * the method "LogExecutionTime" is our advice
 *
 *
 * A aop surgiu para eliminar os chamados cross cutting concerns. Para separar o código necessário, mas que não faz parte das obrigações do objeto.
 * Join Point: define dentro do advice o momento em que o código será executado. É o ponto da execução do programa, no Spring sempre representa a execução de um método
 * Point Cuts: é onde será feito. É a expressão que corresponde ao joinpoint (ponto de junção)
 * Advice: separa o código necessário em um lugar específico; Poder ser considerado como um interceptor. É associado a expressão pointcuts e executa qualquer join point correspondente ao point cut
 * Então Advice é o que será feito, o Join Point é quando e o Point Cuts é onde
 */

@Aspect
@Component
public class ExampleAspect {

    private static final Logger log = LoggerFactory.getLogger(ExampleAspect.class);

    @Before("execution(* br.com.trapmonitor.core.AppStartupRunner.run(..))") //point-cut expression
    public void logBefore(JoinPoint joinPoint){
        log.info("ExampleAspect.logBeforeV1() : " + joinPoint.getSignature().getName());
        System.out.println("logBefore");
    }

    /**
     * just print the amount time that it took
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(LogExecutionTime)") //point-cut expression
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        Method method = methodSignature.getMethod();

        long start = System.currentTimeMillis();

        Object proceed = proceedingJoinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String format = String.format("executed=%s in ms=%s", proceedingJoinPoint.getSignature(), executionTime);

        log.info(format);

        long threshold = retrieveResponseTimeAvgThreshold(method);

        String className = method.getDeclaringClass().getSimpleName();

        String methodName = method.getName();

        boolean isAVGUnderThreshold = executionTime >= threshold;

        if(isAVGUnderThreshold){
            String details =
                    String.format("=================== method=%s_%s, threshold=%s",
                            className, methodName, threshold);

            log.warn(details);
        }
        return proceed;
    }

    private long retrieveResponseTimeAvgThreshold(Method method) {
        LogExecutionTime logExecutionTime = method.getAnnotation(LogExecutionTime.class);
        return normalizeResponseTimeAvgThreshold(logExecutionTime.responseTimeAvg());
    }

    private long normalizeResponseTimeAvgThreshold(long responseTimeAvg) {
        if(responseTimeAvg >= 10L){
            return responseTimeAvg;
        }
        return LogExecutionTime.DEFAULT_RESPONSE_TIME_AVG;
    }

}
