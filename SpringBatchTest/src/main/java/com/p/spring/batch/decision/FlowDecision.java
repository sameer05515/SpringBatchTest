package com.p.spring.batch.decision;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component("decider")
public class FlowDecision implements JobExecutionDecider {
     
    private static final Log log = LogFactory.getLog(FlowDecision.class);
    public static final String COMPLETED = "COMPLETED";
    public static final String FAILED = "FAILED";
    
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        Random generator = new Random();
        int randomInt = generator.nextInt();
         
        log.info("Executing Decision with randomInt = " + randomInt);

        if(randomInt % 2 == 0){
         log.info("------------------------------------------");
         log.info("Completed");
         log.info("------------------------------------------");
            return FlowExecutionStatus.COMPLETED;   
        }
                log.info("------------------------------------------");
                log.info("Failed");
                log.info("------------------------------------------");
        return FlowExecutionStatus.FAILED;
    }
}