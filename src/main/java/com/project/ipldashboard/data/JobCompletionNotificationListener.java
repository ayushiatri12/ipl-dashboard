package com.project.ipldashboard.data;

import com.project.ipldashboard.model.MatchData;
import com.project.ipldashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager entityManager;

    @Autowired
    public JobCompletionNotificationListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            /*jdbcTemplate.query("SELECT team1, team2, date FROM matchData",
                    (rs, row) -> "Team 1 "+rs.getString(1)+"Team 2 "+rs.getString(2)+
                            "Date " +rs.getString(3)
            ).forEach(obj -> System.out.println(obj));*/
            Map<String, Team> teamData = new HashMap<>();
            entityManager.createQuery("select m.team1, count(*) from MatchData m group by m.team1",
                    Object[].class).getResultList().stream()
                    .map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(),team));

            entityManager.createQuery("select m.team2, count(*) from MatchData m group by m.team2",
                    Object[].class).getResultList().stream().
                    forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
            });

            entityManager.createQuery("select m.winner, count(*) from MatchData m group by m.winner",
                    Object[].class).getResultList().stream()
                    .forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        if(team!=null) team.setTotalWins((long) e[1]);
            });

            teamData.values().forEach(team -> entityManager.persist(team));
            teamData.values().forEach(team -> System.out.println(team));
        }
    }
}