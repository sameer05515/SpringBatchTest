package com.p.spring.batch.tasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
 
public class MyTaskOne implements Tasklet {
 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception 
    {
        System.out.println("MyTaskOne start..");
 
        for(int i=0;i<1000;i++) {
        	System.out.print(" : "+i+" : ");
        }
         
        System.out.println("MyTaskOne done..");
        return RepeatStatus.FINISHED;
    }    
}