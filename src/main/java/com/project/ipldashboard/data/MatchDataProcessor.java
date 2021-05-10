package com.project.ipldashboard.data;
import com.project.ipldashboard.model.MatchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class MatchDataProcessor implements ItemProcessor<MatchInput, MatchData> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public MatchData process(final MatchInput matchInput) throws Exception {

        MatchData matchData = new MatchData();
        matchData.setId(Long.parseLong(matchInput.getId()));
        matchData.setCity(matchInput.getCity());
        matchData.setDate(LocalDate.parse(matchInput.getDate()));
        matchData.setPlayerOfMatch(matchInput.getPlayer_of_match());
        matchData.setVenue(matchInput.getVenue());

        String firstBattingTeam, secondBattingTeam;
        if(matchInput.getToss_decision().equals("bat")) {
            firstBattingTeam = matchInput.getToss_winner();
            if(matchInput.getTeam1().equals(matchInput.getToss_winner()))
                secondBattingTeam = matchInput.getTeam2();
            else
                secondBattingTeam = matchInput.getTeam1();
        }
        else {
            secondBattingTeam = matchInput.getToss_winner();
            if(matchInput.getTeam1().equals(matchInput.getToss_winner()))
                firstBattingTeam = matchInput.getTeam2();
            else
                firstBattingTeam = matchInput.getTeam1();
        }
        matchData.setTeam1(firstBattingTeam);
        matchData.setTeam2(secondBattingTeam);
        matchData.setTossWinner(matchInput.getToss_winner());
        matchData.setTossDecision(matchInput.getToss_decision());
        matchData.setWinner(matchInput.getWinner());
        matchData.setResult(matchInput.getResult());
        matchData.setResultMargin(matchInput.getResult_margin());
        matchData.setUmpire1(matchInput.getUmpire1());
        matchData.setUmpire2(matchInput.getUmpire2());
        return matchData;
    }


}
