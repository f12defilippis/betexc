package com.bonde.betbot.service.datacollection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.Sport;
import com.bonde.betbot.model.domain.Team;
import com.bonde.betbot.model.domain.TeamName;
import com.bonde.betbot.repository.TeamNameRepository;
import com.bonde.betbot.repository.TeamRepository;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class TeamDiscoveryService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	TeamNameRepository teamNameRepository;
	
	@Transactional
	public void saveTeamFromXls()
	{
		//Build reader instance
	      CSVReader reader;
		try {
			InputStream in = this.getClass().getClassLoader()
	                .getResourceAsStream("MappingTeam2.csv");
			reader = new CSVReader(new BufferedReader(new InputStreamReader(in,"UTF-8")),';');
			List<String[]> allRows = reader.readAll();
		    for(int i = 1 ; i < allRows.size() ; i++){
		    	String[] row = allRows.get(i);
		        saveRow(row);
		        if(i % 10 == 0)
		        {
				    log.info("PARTIAL: Saved " + i + " teams");
		        }
		     }
		    log.info("Saved " + (allRows.size()-1) + " teams");
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
		}
	       
	      //Read all rows at once
	}

	
	private void saveRow(String[] row)
	{

		Sport sport = new Sport(1);
		List<Team> lt = teamRepository.findByNameAndSport(row[1], sport);
		Team team = new Team();
		if(lt==null || lt.size()==0)
		{
			Date now = new Date();
			team.setName(row[1]);
			team.setSport(sport);
			team.setDateCreated(now);
			team.setDateUpdated(now);
			
			teamRepository.save(team);
		}else
		{
			team = lt.get(0);
		}
		
		
		saveName(team, row[1], Source.LIVESCORE24);
		saveName(team, row[2], Source.BETTING1X2);
		saveName(team, row[3], Source.VITIBET);
		saveName(team, row[4], Source.FOREBET);
		saveName(team, row[5], Source.MYBET);
		saveName(team, row[6], Source.ZULUBET);
		saveName(team, row[7], Source.PICKFORWIN);
		saveName(team, row[8], Source.IAMBETTOR);
		saveName(team, row[9], Source.PROSOCCER);
		saveName(team, row[10], Source.SCIBET);
		
		log.debug(team.getName() + " saved!");
		
	}

	private void saveName(Team team, String name, int source)
	{
		if(name!=null && !name.equals(""))
		{
			List<TeamName> ltn = teamNameRepository.findByNameAndSource(name, new Source(source));
			if(ltn==null || ltn.size()==0)
			{
				Date now = new Date();
				TeamName tn = new TeamName();
				tn.setTeam(team);
				tn.setSource(new Source(source));
				tn.setName(name);
				
				tn.setDateCreated(now);
				tn.setDateUpdated(now);
				
				teamNameRepository.save(tn);
			}
		}
	}
	
	
}
