package io.mohit.repo;

import org.springframework.data.repository.CrudRepository;

import io.mohit.model.Team;

public interface TeamRepo extends CrudRepository<Team, Long>{
	
	public Team findByTeamName(String teamName);
}
