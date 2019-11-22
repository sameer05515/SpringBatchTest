package com.p.spring.batch.decision;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
	public static final String STOPPED = "STOPPED";
	public static final String UNKNOWN = "UNKNOWN";
	public static final String CUSTOM = "CUSTOM";

	private final String myStepName;
	PrintStream ps = null;

	public FlowDecision() {
		this.myStepName = "Premendra05515";
	}

	public FlowDecision(String myStepName) {
		this.myStepName = myStepName;
		try {
			ps = new PrintStream(new File("C:\\Users\\premendra.kumar\\Desktop\\Batch-Steps\\decision.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

		try {

			Random generator = new Random();
			int randomInt = generator.nextInt();

			randomInt = 3;

			log.info("Executing Decision with randomInt = " + randomInt);

			int randIntQuotient = (((randomInt % 5) + 5) % 5);

			log.info("Executing Decision with randIntQuotient = " + randIntQuotient);
			if (randIntQuotient == 0) {
				log.info("------------------------------------------");
				log.info(getMyStepName() + "COMPLETED");
				ps.println(getMyStepName() + "COMPLETED");
				log.info("------------------------------------------");
				return FlowExecutionStatus.COMPLETED;
			} else if (randIntQuotient == 1) {
				log.info("------------------------------------------");
				log.info(getMyStepName() + "STOPPED");
				ps.println(getMyStepName() + "STOPPED");
				log.info("------------------------------------------");
				return FlowExecutionStatus.STOPPED;
			} else if (randIntQuotient == 2) {
				log.info("------------------------------------------");
				log.info(getMyStepName() + "FAILED");
				ps.println(getMyStepName() + "FAILED");
				log.info("------------------------------------------");
				return FlowExecutionStatus.FAILED;
			} else if (randIntQuotient == 3) {
				log.info("------------------------------------------");
				log.info(getMyStepName() + "CUSTOM");
				ps.println(getMyStepName() + "CUSTOM");
				log.info("------------------------------------------");
				return new FlowExecutionStatus("CUSTOM");
			} else {
				log.info("------------------------------------------");
				log.info(getMyStepName() + "UNKNOWN");
				ps.println(getMyStepName() + "UNKNOWN");
				log.info("------------------------------------------");
				return FlowExecutionStatus.UNKNOWN;
			}

		}

		catch (Exception e) {

			e.printStackTrace();
			return FlowExecutionStatus.UNKNOWN;

		}
	}

	/**
	 * @return the myStepName
	 */
	public String getMyStepName() {
		return myStepName;
	}
}