package io.mohit.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.mohit.model.Match;
import io.mohit.model.Team;
import io.mohit.repo.MatchRepo;
import io.mohit.repo.TeamRepo;

@RestController
public class TeamController {
	
	private TeamRepo teamRepo;
	
	private MatchRepo matchRepo;
	
	@Autowired
	public TeamController(TeamRepo teamRepo,MatchRepo matchRepo) {
		this.teamRepo = teamRepo;
		this.matchRepo=matchRepo;
	}




	@GetMapping(value = "/teams/{teamName}")
	public Team getTeam(@PathVariable("teamName") String teamName) {
		Team response = teamRepo.findByTeamName(teamName);
		
		List<Match> matches= this.matchRepo.findLatestMatchesByTeam(teamName, 4);
		response.setMatches(matches);
		return response;
		
	}
}
