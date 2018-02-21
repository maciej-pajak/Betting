package pl.maciejpajak.service;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.dto.BetDto;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.BetRepository;

@Service
public class BetService {
    
    private BetRepository betRepository;
   
    @Autowired
    public BetService(BetRepository betRepository) {
        super();
        this.betRepository = betRepository;
    }

    public BetDto findOneById(Long betId) {
        return convertToDto.apply(
                betRepository
                    .findOneByIdAndVisible(betId, true)
                    .orElseThrow(() -> new BaseEntityNotFoundException(betId)));
    }
    
    public Collection<BetDto> findAllBetableByGameIdAndVisible(Long gameId) {
        Collection<Bet> bets = betRepository.findAllByGameIdAndBetableAndVisible(gameId, true, true);
        return bets.stream().map(convertToDto).collect(Collectors.toList());
    }
    
    private final Function<Bet, BetDto> convertToDto = 
            b -> BetDto.builder()
                .id(b.getId())
                .description(b.getDescription())
                .betOptions(b.getBetOptions())
                .gameId(b.getGame().getId())
                .build();
}
