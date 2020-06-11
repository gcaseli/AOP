package br.com.trapmonitor.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target essa anotação informa onde essa esta será aplicada, no caso apenas em métodos
 * @Retention informação onde a anotação será disponibilizada para a JVM em tempo de execução ou não
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

    long DEFAULT_RESPONSE_TIME_AVG = 90;

    /**
     * média do tempo de resposta (em millis) das execuções do método anotado.
     * @return
     */
    long responseTimeAvg() default DEFAULT_RESPONSE_TIME_AVG ;

}
