package pl.maciejpajak.api.temp;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.BetDto;
import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.repository.BetRepository;

@Service
public class BetService {
    
    @Autowired
    private BetRepository betRepository;

    public BetDto findOneById(Long betId) throws Exception {
        // TODO exception
        
        return convertToDto.apply(
                betRepository
                    .findOneByIdAndVisible(betId, true)
                    .orElseThrow(() -> new Exception()));
    }
    
    public Collection<BetDto> findAllByGameIdAndVisible(Long gameId) {
        Collection<Bet> bets = betRepository.findAllByGameIdAndVisible(gameId, true);
        return bets.stream().map(convertToDto).collect(Collectors.toList());
    }
    
    private Function<Bet, BetDto> convertToDto = 
            b -> BetDto.builder()
                .id(b.getId())
                .description(b.getDescription())
                .betOptions(b.getBetOptions())
                .gameId(b.getGame().getId())
                .build();
}
