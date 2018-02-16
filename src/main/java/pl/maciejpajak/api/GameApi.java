package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.score.GameScore;
import pl.maciejpajak.domain.game.score.PartScore;
import pl.maciejpajak.domain.util.GameStatus;
import pl.maciejpajak.service.GameService;

@RestController
@RequestMapping("/games")
public class GameApi {
    
    private final GameService gameService;
    
    public GameApi(GameService gameService) {
        this.gameService = gameService;
    }
    
    @GetMapping("/{gameId}")
    private Game getGameById(@PathVariable(name = "gameId", required = true) Long gameId) {
        return gameService.findOneById(gameId);
    }
    
    @GetMapping("/{gameId}/latest-score")
    private GameScore getLatestGameScore(@PathVariable(name = "gameId", required = true) Long gameId) {
        return gameService.findLatestGameScoreByGameId(gameId);
    }
    
    @GetMapping("/{gameId}/latest-part-score")
    private PartScore getLatestPartScore(@PathVariable(name = "gameId", required = true) Long gameId,
            @RequestParam(name = "partId", required =  false) Long partId) {
        return gameService.findLatestPartScore(gameId, partId);
    }
    
    @GetMapping("/live")
    private Collection<Game> getAllLiveGamesByCompetition(@RequestParam(name = "competitionId", required = false) Long competitionId) {
        return gameService.getAllLiveGamesByCompetition(competitionId);
    }
    
    @GetMapping("/all")
    private Collection<Game> getAllGames(
            @RequestParam(name = "status", required = false) Collection<GameStatus> status,
            @RequestParam(name = "competitionId", required = false) Long competitionId) {
        
        if (competitionId == null) {
            return gameService.findAll();
        } else {
            return gameService.findAllByCompetitionId(competitionId);
        }
    }

}
