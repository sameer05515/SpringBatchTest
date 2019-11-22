package com.p.spring.batch.tasklet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
 
public class MyTaskOne implements Tasklet {
	private static final Log log = LogFactory.getLog(MyTaskOne.class);
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception 
    {
        log.info("MyTaskOne start..");
 
        for(int i=0;i<1000;i++) {
        	System.out.print(" : "+i+" : ");
        }
         
        log.info("MyTaskOne done..");
        return RepeatStatus.FINISHED;
    }    
}