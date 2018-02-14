package pl.maciejpajak.testing.event.handler;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.GamePart;
import pl.maciejpajak.domain.game.util.GameStatus;
import pl.maciejpajak.repository.GamePartRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.testing.event.event.GameStartEvent;

public class GameStartHandler {
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private GamePartRepository gamePartRepository;
    
    @EventListener
    public void handleGameStartEvent(GameStartEvent gameStartEvent) {
        Game game = gameRepository.findOneByIdAndVisible(gameStartEvent.getEventDto().getGameId(), true)
                .orElseThrow(() -> new RuntimeException()); // TODO this requires new exception
        game.setStatus(GameStatus.LIVE);
        gameRepository.save(game);
      
        
        GamePart gamePart = GamePart.builder()
                .game(game)
                .duration(Duration.ofMinutes(gameStartEvent.getEventDto().getValue()))
                .startTime(gameStartEvent.getEventDto().getTime())
                .visible(true)
                .status(GameStatus.LIVE)
                .build();
        gamePartRepository.save(gamePart);
    }

}
