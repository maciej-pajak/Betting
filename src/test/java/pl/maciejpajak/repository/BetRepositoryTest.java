package pl.maciejpajak.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.game.Game;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BetRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BetRepository betRepository;

    @Test
    public void updateBetableFlag_GivenBetableAndGameIdAndVisibile_ShouldUpdateBetable() {
        // given
        Game game = entityManager.persist(new Game());
        
        Bet bet = new Bet();
        bet.setBetable(true);
        bet.setVisible(true);
        bet.setGame(game);

        entityManager.persist(bet); 
        
        // when
        betRepository.updateBetableFlag(false, game.getId(), true);
        Bet result = (Bet) entityManager.find(Bet.class, bet.getId());

        // then
        assertEquals(false, result.isBetable());
    }

}
