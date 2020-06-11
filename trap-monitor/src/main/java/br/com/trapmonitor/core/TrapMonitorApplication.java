package br.com.trapmonitor.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TrapMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrapMonitorApplication.class, args);
    }

}
