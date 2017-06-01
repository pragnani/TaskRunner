package com.taskrnr.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	MyJobs jobs;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(cron = "0 */2 * * * *")
	public void runJob() throws Exception {
		
		JobParameters jobParameters =
				  new JobParametersBuilder()
				  .addLong("time",System.currentTimeMillis()).toJobParameters();

		log.info("Job is Running");
		for (Job job : jobs.getJobs())
			jobLauncher.run(job, jobParameters);

	}

}
