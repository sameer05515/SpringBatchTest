package com.p.spring.batch.main;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.p.spring.batch")
public class App implements CommandLineRunner {
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier(value = "firstJob")
	Job job1;

	@Autowired
	@Qualifier(value = "secondJob")
	Job job2;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		JobParameters params1 = new JobParametersBuilder()
//				.addString("JobID", String.valueOf(System.currentTimeMillis())).addString("Job Name", "First Job")
//				.toJobParameters();
//		jobLauncher.run(job1, params1);

		JobParameters params2 = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis())).addString("Job Name", "Second Job")
				.toJobParameters();
		JobExecution je2=jobLauncher.run(job2, params2);
		System.out.println("\n ====== \n\nPremendra je2"+je2+"\n ======== \n\n");
	}
}