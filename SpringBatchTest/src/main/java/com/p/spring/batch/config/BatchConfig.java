package com.p.spring.batch.config;

import java.io.File;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private static final Log log = LogFactory.getLog(BatchConfig.class);

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public Step stepOne() {
		return steps.get("stepOne").tasklet(new MyTaskOne()).build();
	}

	@Bean
	public Step stepTwo() {
		return steps.get("stepTwo").tasklet(new MyTaskTwo()).build();
	}

	@Bean
	public Job firstJob() {

		return jobs.get("firstJob").incrementer(new RunIdIncrementer()).start(stepOne()).next(stepTwo()).build();

	}

	int counter = 1;

//	@Bean
	public FlowDecision decision(String flowName) {
		return new FlowDecision("obj-" + counter++);
	}

	@Bean
	public Job secondJob() {

		try {
			final PrintStream ps = new PrintStream(
					new File("C:\\Users\\premendra.kumar\\Desktop\\Batch-Steps\\steps.txt"));

			FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow1");

			Flow flow = flowBuilder.start(steps.get("stepOne").tasklet(new Tasklet() {

				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					log.info("StepOne : Task s1.1 : Actual code yet to be written!");
					ps.println("StepOne : Task s1.1 : Actual code yet to be written!");

					return RepeatStatus.FINISHED;
				}
			}).build()).next(decision("flow1")).on(FlowDecision.COMPLETED)
					.to(steps.get("stepTwo").tasklet(new Tasklet() {

						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
								throws Exception {
							log.info(
									"stepTwo : Task s2.1 : Actual code yet to be written! Task s1.1 should be completed successfully before this step.");
							ps.println(
									"stepTwo : Task s2.1 : Actual code yet to be written! Task s1.1 should be completed successfully before this step.");

							return RepeatStatus.FINISHED;
						}
					}).build()).from(decision("flow1")).on(FlowDecision.FAILED)
					.to(steps.get("stepThree").tasklet(new Tasklet() {

						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
								throws Exception {
							log.info(
									"stepThree : Task s3.1 : Actual code yet to be written! Task s1.1 should be failed before this step.");
							ps.println(
									"stepThree : Task s3.1 : Actual code yet to be written! Task s1.1 should be failed before this step.");

							return RepeatStatus.FINISHED;
						}
					}).build()).from(decision("flow1")).on(FlowDecision.STOPPED)
					.to(steps.get("stepFour").tasklet(new Tasklet() {

						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
								throws Exception {
							log.info(
									"stepFour : Task s4.1 : Actual code yet to be written! Task s1.1 should be stopped before this step.");
							ps.println(
									"stepFour : Task s4.1 : Actual code yet to be written! Task s1.1 should be stopped before this step.");

							return RepeatStatus.FINISHED;
						}
					}).build()).from(decision("flow1")).on(FlowDecision.CUSTOM)
					.to(steps.get("stepSix").tasklet(new Tasklet() {

						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
								throws Exception {
							log.info(
									"stepSix : Task s6.1 : Actual code yet to be written! Task s1.1 should be in custom state before this step.");
							ps.println(
									"stepSix : Task s6.1 : Actual code yet to be written! Task s1.1 should be in custom state before this step.");

							return RepeatStatus.FINISHED;
						}
					}).build()).from(decision("flow1")).on(FlowDecision.UNKNOWN)
					.to(steps.get("stepFive").tasklet(new Tasklet() {

						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
								throws Exception {
							log.info(
									"stepFive : Task s5.1 : Actual code yet to be written! Task s1.1 should have unknown result before this step.");
							ps.println(
									"stepFive : Task s5.1 : Actual code yet to be written! Task s1.1 should have unknown result before this step.");

							return RepeatStatus.FINISHED;
						}
					}).build()).end();

			return jobs.get("secondJob").incrementer(new RunIdIncrementer()).start(flow).end().build();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}