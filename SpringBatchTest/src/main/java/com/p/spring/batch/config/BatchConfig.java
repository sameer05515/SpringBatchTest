package com.p.spring.batch.config;
 
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.p.spring.batch.decision.FlowDecision;
import com.p.spring.batch.tasklet.MyTaskOne;
import com.p.spring.batch.tasklet.MyTaskTwo;
 
@Configuration
@EnableBatchProcessing
public class BatchConfig {
     
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
     
    @Bean
    public Step stepOne(){
        return steps.get("stepOne")
                .tasklet(new MyTaskOne())
                .build();
    }
     
    @Bean
    public Step stepTwo(){
        return steps.get("stepTwo")
                .tasklet(new MyTaskTwo())
                .build();
    }   
     
    @Bean
    public Job firstJob(){
    	
    	return jobs.get("firstJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .next(stepTwo())
                .build();
    	
    	
    	
    }
    
    @Bean
    public FlowDecision decision(){
     return new FlowDecision();
    }
    
    @Bean
    public Job secondJob(){
    	
//		return jobs.get("secondJob")
//                .incrementer(new RunIdIncrementer())
//                .start(stepOne())
//                .next(stepTwo())
//                .build();
    	
    	
    	FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow1");

    	  Flow flow =  flowBuilder
    	    .start(steps.get("stepOne")
                    .tasklet(new Tasklet() {
						
						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
							System.out.println("StepOne : Task s1.1 : Actual code yet to be written!");	 
					        
					        return RepeatStatus.FINISHED;
						}
					})
                    .build())
    	    .next(decision())
    	    .on(FlowDecision.COMPLETED)
    	    .to(steps.get("stepTwo")
                    .tasklet(new Tasklet() {
						
						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
							System.out.println("stepTwo : Task s2.1 : Actual code yet to be written! Task s1.1 should be completed successfully before this step.");	 
					        
					        return RepeatStatus.FINISHED;
						}
					})
                    .build())
    	    .from(decision())
    	    .on(FlowDecision.FAILED)
    	    .to(steps.get("stepThree")
                    .tasklet(new Tasklet() {
						
						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
							System.out.println("stepThree : Task s3.1 : Actual code yet to be written! Task s1.1 should be failed before this step.");	 
					        
					        return RepeatStatus.FINISHED;
						}
					})
                    .build())
    	    .end();

    	  return jobs.get("secondJob")
    	    .incrementer(new RunIdIncrementer())
    	    .start(flow)
    	    .end()
    	    .build();
    	
    	
    	
    }
}