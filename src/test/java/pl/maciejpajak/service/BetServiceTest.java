package pl.maciejpajak.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.dto.BetDto;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.BetRepository;

@RunWith(MockitoJUnitRunner.class)
public class BetServiceTest {
    
    private BetService betService;
    
    @Mock
    private BetRepository betRepositoryMock;

    @Before
    public void setUp() throws Exception {
        betService = new BetService(betRepositoryMock);
    }

    @Test
    public void findOneById_GivenValidId_ShouldReturnCorrectBetDto() {
        // given
        Game game = new Game();
        game.setId(2L);
        Bet bet = new Bet();
        bet.setId(1L);
        bet.setBetOptions(null);
        bet.setGame(game);
        bet.setDescription("desc");
        Optional<Bet> opt = Optional.of(bet);
        when(betRepositoryMock.findOneByIdAndVisible(bet.getId(), true)).thenReturn(opt);
        // when
        BetDto result = betService.findOneById(bet.getId());
        // then
        assertEquals(result.getId(), bet.getId());
        assertEquals(result.getDescription(), bet.getDescription());
        assertEquals(result.getGameId(), bet.getGame().getId());
        assertEquals(result.getBetOptions(), bet.getBetOptions());
    }
    
    @Test(expected = BaseEntityNotFoundException.class)
    public void findOneById_GivenInvalidId_ShouldThrowException() {
        // given
        Long id = 1L;
        Optional<Bet> opt = Optional.empty();
        when(betRepositoryMock.findOneByIdAndVisible(id, true)).thenReturn(opt);
        // when
        betService.findOneById(id);
    }

}
