package io.mohit.data;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import io.mohit.model.Match;
import io.mohit.model.Team;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
  
  //
  //private final JdbcTemplate jdbcTemplate;
  
  private final EntityManager em;
  

  @Autowired
  public JobCompletionNotificationListener(EntityManager em) {
    this.em = em;
  }

  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

//      jdbcTemplate.query("SELECT team1, team2 FROM match",
//        (rs, row) -> "Team 1" + rs.getString(1) + "Team 2" + rs.getString(2)
//      ).forEach(str -> System.out.println(str));
      
      //select distinct team1 from Match m union select distinct team2 from Match m;
      Map<String, Team> teamData = new HashMap<>();
       em.createQuery("select m.team1,count(*) from Match m group by m.team1",Object[].class)
          .getResultList()
          .stream()
          .map(e->new Team((String)e[0],(long)e[1]))
          		.forEach(e->teamData.put(e.getTeamName(),e));
        		  
          
      em.createQuery("select m.team2,count(*) from Match m group by m.team2",Object[].class)
              .getResultList()
              .stream()
              
              .forEach(e->{
            	  Team team =teamData.get((String) e[0]);
            	  team.setTotalMatches(team.getTotalMatches() + (long)e[1]);
              });
      
      em.createQuery("select m.matchWinner,count(*) from Match m group by m.matchWinner",Object[].class)
      .getResultList()
      .forEach(e->{
    	  Team team = teamData.get((String) e[0]);
    	  if(team!=null) {
    	  team.setTotalWins((long)e[1]);
    	  }
      });
      
      teamData.values().forEach(team->em.persist(team));
      
      teamData.values().forEach(System.out::println);
              
      
      
      
      
          
      
      
      
      
    }
  }

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		
	}
}
