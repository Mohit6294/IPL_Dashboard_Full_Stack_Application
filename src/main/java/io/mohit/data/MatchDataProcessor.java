package io.mohit.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import io.mohit.model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match>{
	
	private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

	  @Override
	  public Match process(final MatchInput matchInput) throws Exception {
	    
		  Match match = new Match();
		  match.setID(Long.parseLong(matchInput.getID()));
		  match.setCity(matchInput.getCity());
		  
		  //match.setDate(LocalDate.parse(matchInput.getDate(),df));
		  
		  
		 
		  
		  
		  
		  match.setDate(matchInput.getDate());
		  match.setPlayerOfMatch(matchInput.getPlayer_of_Match());
		  match.setVenue(matchInput.getVenue());
		  
		  //SEt team1 and team2 based on the innings order
		  String firstInningTeam, secondInningTeam;
		  
		  if("bat".equals(matchInput.getTossDecision())) {
			  firstInningTeam = matchInput.getTossWinner();
			  secondInningTeam = matchInput.getTossWinner().equals(matchInput.getTeam1())?matchInput.getTeam2():matchInput.getTeam1();
		  }else {
			  secondInningTeam = matchInput.getTossWinner();
			  firstInningTeam = matchInput.getTossWinner().equals(matchInput.getTeam1())? matchInput.getTeam2():matchInput.getTeam1();
		  }
		  
		  match.setTeam1(firstInningTeam);
		  match.setTeam2(secondInningTeam);
		  
		  match.setTossDecision(matchInput.getTossDecision());
		  match.setTossWinner(matchInput.getTossWinner());
		  match.setMatchNumber(matchInput.getMatchNumber());
		  match.setMatchWinner(matchInput.getWinningTeam());
		  match.setResult(matchInput.getWinningTeam());
		  match.setResultMargin(matchInput.getMargin());
		  match.setSuperOver(matchInput.getSuperOver());
		  match.setSeason(matchInput.getSeason());
		  match.setUmpire1(matchInput.getUmpire1());
		  match.setUmpire2(matchInput.getUmpire2());
		  

	    return match;
	  }

	private int lengthOfchar(String string) {
		// TODO Auto-generated method stub
		int x=Integer.parseInt(string);
		int count =0;
		while(x!=0) {
			int rem = x%10;
			x = x/10;
			count++;
		}
		return count;
	}
}
