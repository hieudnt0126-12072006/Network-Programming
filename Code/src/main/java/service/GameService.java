package service;

import Dao.MatchDAO;
import Dao.UserDAO;
import Model.Match;
import util.DateUtil;

public class GameService {
    private final MatchDAO matchDAO = new MatchDAO();
    private final UserDAO userDAO = new UserDAO();

    public void recordResult(String player1, String player2, String winner, int totalMoves) {
        Match match = new Match(player1, player2);
        match.setWinner(winner);
        match.setTotalMoves(totalMoves);
        match.setPlayedAt(DateUtil.now());
        matchDAO.saveMatch(match);

        if (winner == null) {
            // Hòa
            userDAO.incrementDraws(player1);
            userDAO.incrementDraws(player2);
        } else {
            String loser = winner.equals(player1) ? player2 : player1;
            userDAO.incrementWins(winner);
            userDAO.incrementLosses(loser);
        }
    }
}
