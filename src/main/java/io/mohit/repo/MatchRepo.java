package io.mohit.repo;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import io.mohit.model.Match;



public interface MatchRepo extends CrudRepository<Match, Long>{
	
	public List<Match> findByTeam1OrTeam2OrderByIDDesc(String teamName1,String teamName2,Pageable pageable);

	default List<Match> findLatestMatchesByTeam(String teamName,int count){
		
		return findByTeam1OrTeam2OrderByIDDesc(teamName, teamName, PageRequest.of(0, count));
	}



}
