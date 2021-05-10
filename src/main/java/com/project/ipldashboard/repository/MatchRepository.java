package com.project.ipldashboard.repository;

import com.project.ipldashboard.model.MatchData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends CrudRepository<MatchData,Long> {

    List<MatchData> getByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2, Pageable pageable);

    @Query("select m from MatchData m where(m.team1 = :teamName or m.team2 = :teamName) and m.date between :startDate and :endDate order by date desc")
    List<MatchData> getMatchesByTeamBetweenDates(
            @Param("teamName") String teamName,
            @Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);
    default List<MatchData> getLatestMatchesByTeam(String teamName, int count){
        return getByTeam1OrTeam2OrderByDateDesc(teamName,teamName, PageRequest.of(0,count));

    }
}
