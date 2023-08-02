package io.mohit.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {
	
	@Id
	private long ID;
	private String City;
	//private LocalDate Date;
	private String date;
	private String Season;
	private String matchNumber;
	private String team1;
	private String team2;
	private String Venue;
	private String tossWinner;
	private String tossDecision;
	private String superOver;
	private String matchWinner;
	private String result;
	private String resultMargin;
	
	private String PlayerOfMatch;
	//private String Team1Players;
	//private String Team2Players;
	private String Umpire1;
	private String Umpire2;
}
