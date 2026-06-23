package service;

import Dao.MatchDAO;
import Model.Match;
import java.util.List;

public class MatchService {
    private final MatchDAO matchDAO = new MatchDAO();

    public List<Match> getHistory(String username) {
        return matchDAO.getMatchesByPlayer(username);
    }
}
