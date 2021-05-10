package com.project.ipldashboard.controller;

import com.project.ipldashboard.model.MatchData;
import com.project.ipldashboard.model.Team;
import com.project.ipldashboard.repository.MatchRepository;
import com.project.ipldashboard.repository.TeamRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@CrossOrigin
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;


    public TeamController(TeamRepository teamRepository,MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team")
    public Iterable<Team> getAllTeams()
    {
        return teamRepository.findAll();
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName)
    {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.getLatestMatchesByTeam(teamName,4));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<MatchData> getMatchesForTeam(@PathVariable String teamName , @RequestParam int year)
    {
        LocalDate startDate = LocalDate.of(year,1,1);
        LocalDate endDate = LocalDate.of(year+1,1,1);
        return matchRepository.getMatchesByTeamBetweenDates(teamName,startDate,endDate);
    }
}
