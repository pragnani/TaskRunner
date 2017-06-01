package com.taskrnr.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class TaskRunnerApplication {
	

	private static final Logger log = LoggerFactory.getLogger(TaskRunnerApplication.class);
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	public static void main(String[] args) {
		SpringApplication.run(TaskRunnerApplication.class, args);
	}
	
	@Bean
	public JobLauncher jobLauncher(JobRepository jobRepository){
		SimpleAsyncTaskExecutor taskExecutor= new SimpleAsyncTaskExecutor();
		SimpleJobLauncher launcher= new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		launcher.setTaskExecutor(taskExecutor);
		return launcher;
	}
	@Bean
	public Step job1Step(String jobid) {

		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
				log.info("{} step executed",jobid);
				return null;
			}
		}).build();
	}
	@Bean
	public Step job2Step(String jobid) {

		return stepBuilderFactory.get("step2").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
				log.info("{} step executed",jobid);
				return null;
			}
		}).build();
	}
	@Bean
	public Step job3Step(String jobid) {

		return stepBuilderFactory.get("step3").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
				log.info("{} step executed",jobid);
				return null;
			}
		}).build();
	}
	@Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(ResourcelessTransactionManager txManager)
            throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
        return factory.getObject();
    }

    @Bean
    public JobExplorer jobExplorer(MapJobRepositoryFactoryBean factory) {
        return new SimpleJobExplorer(factory.getJobInstanceDao(), factory.getJobExecutionDao(),
                factory.getStepExecutionDao(), factory.getExecutionContextDao());
    }

	
	
	@Bean
	public Job job1(Step job1Step) throws Exception {
		return jobBuilderFactory.get("job1").incrementer(new RunIdIncrementer()).start(job1Step).build();

	}
	@Bean
	public Job job2(Step job2Step) throws Exception {
		return jobBuilderFactory.get("job2").incrementer(new RunIdIncrementer()).start(job2Step).build();

	}
	@Bean
	public Job job3(Step job3Step) throws Exception {
		return jobBuilderFactory.get("job2").incrementer(new RunIdIncrementer()).start(job3Step).build();

	}
	protected JobRepository createJobRepository() throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
        factory.afterPropertiesSet();
        return  factory.getObject();
    }

	@Bean
	public MyJobs myJobs() throws Exception{
		MyJobs jobs= new MyJobs();
		jobs.getJobs().add(job1(job1Step("job 1")));
		jobs.getJobs().add(job2(job2Step("job 2")));
		jobs.getJobs().add(job2(job3Step("job 3")));
		return jobs;
	}

}
