package br.com.trapmonitor.core.resources;

import br.com.trapmonitor.core.aop.LogExecutionTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrapMonitorController {

    @GetMapping(value = "/monitors", produces = "application/json; charset=UTF-8")
    @LogExecutionTime(responseTimeAvg = 80)
    public String monitors() throws InterruptedException {
        server();
        return "Hello";
    }

    public void server() throws InterruptedException {
        Thread.sleep(100);
    }

}
