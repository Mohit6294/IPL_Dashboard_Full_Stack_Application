package io.mohit.data;

import java.io.File;
import java.net.MalformedURLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.PlatformTransactionManager;

import io.mohit.model.Match;



@Configuration
@EnableBatchProcessing
public class BatchConfig{
	
	
	private final String[] FIELD_NAMES=new String[] {"ID","City","Date","Season","MatchNumber","Team1","Team2","Venue","TossWinner","TossDecision","SuperOver","WinningTeam","WonBy","Margin","method","Player_of_Match","Team1Players","Team2Players","Umpire1","Umpire2"};
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	

	
	@Bean
	public FlatFileItemReader<MatchInput> reader() throws MalformedURLException {
	  return new FlatFileItemReaderBuilder<MatchInput>()
	    .name("MatchItemProcessor")
	    .resource(new ClassPathResource("match-data.csv"))
	    .delimited()
	    .names(FIELD_NAMES)
	    .fieldSetMapper(new BeanWrapperFieldSetMapper<MatchInput>() {
	    	{
	           setTargetType(MatchInput.class);
	       }
	    	})
	    .build();
	}

	@Bean
	public MatchDataProcessor processor() {
	  return new MatchDataProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<Match>()
	    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	    .sql("INSERT INTO match(ID, City, date, Season, match_Number, team1, team2, Venue, toss_Winner, toss_Decision, super_Over, match_Winner, result, result_Margin, Player_Of_Match, Umpire1, Umpire2) VALUES (:ID, :City, :date, :Season, :MatchNumber, :team1, :team2, :Venue, :tossWinner, :tossDecision, :superOver, :matchWinner, :result, :resultMargin, :PlayerOfMatch, :Umpire1, :Umpire2)")
	    .dataSource(dataSource)
	    .build();
	}
	
	
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
	  return jobBuilderFactory.get("importUserJob")
			  .incrementer(new RunIdIncrementer())
			  .listener(listener)
			  .flow(step1)
			  .end()
			  .build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Match> writer) throws MalformedURLException {
	  return stepBuilderFactory.get("step1")
			  .<MatchInput,Match>chunk(10)
			  .reader(reader())
			  .processor(processor())
			  .writer(writer)
			  .build();
	}
	
	
	
	
	
	
	
}