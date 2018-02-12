package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.util.GameStatus;
import pl.maciejpajak.repository.GameRepository;

@RestController
@RequestMapping("/games")
public class GameApi {
    
    private final GameRepository gameRepository;
    
    public GameApi(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    
    @GetMapping("/{id}")
    private Game getGameById(@PathVariable(name = "id", required = true) Long id) throws Exception {
        return gameRepository.findOneByIdAndVisible(id, true).orElseThrow(() -> new Exception()); // TODO cusotm exception and handle
    }
    
//    @GetMapping("/live")
//    private Collection<Game> getAllLiveGamesByCompetition(@RequestParam(name = "competitionId") Long competitionId) {
//        
//    }
    
    @GetMapping("/all")
    private Collection<Game> getAllGames(
            @RequestParam(name = "status", required = false) Collection<GameStatus> status,
            @RequestParam(name = "competitionId", required = false) Long competitionId) {
        
        if (competitionId == null) {
            return gameRepository.findAllByVisible(true);
        } else {
            return gameRepository.findAllByCompetitionIdAndVisible(competitionId, true);
        }
    }

}
