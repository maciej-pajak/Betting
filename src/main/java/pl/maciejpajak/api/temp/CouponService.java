package pl.maciejpajak.api.temp;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.BetDto;
import pl.maciejpajak.api.dto.CouponDto;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.CouponRepository;
import pl.maciejpajak.repository.OddRepository;
import pl.maciejpajak.repository.TransactionRepository;
import pl.maciejpajak.repository.UserRepository;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final OddRepository oddRepository;
    private final BetOptionRepository betOptionRepository;
    private final UserRepository userRepository;
//    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    
    @Autowired
    public CouponService(CouponRepository couponRepository,
            OddRepository oddRepository,
            BetOptionRepository betOptionRepository,
            UserRepository userRepository,
//            WalletRepository walletRepository,
            TransactionRepository transactionRepository) {
        this.couponRepository = couponRepository;
        this.oddRepository = oddRepository;
        this.betOptionRepository = betOptionRepository;
        this.userRepository = userRepository;
//        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @Transactional
    public void createCoupon(CouponDto couponDto) throws Exception {
        Long userId = 1L; // TODO change to current user
        
//        Wallet w = walletRepository.findOne(userId);
//        BigDecimal balance = w.getBalance(); 
        
//        if (balance.compareTo(couponDto.getAmount()) < 0) {
////            throw new InsufficientFundsException();   // TODO define and handle this
//        }
        Transaction transaction = new Transaction();
        // TODO add transaction details
        transactionRepository.save(transaction);
//        if (couponDto instanceof GroupCouponDto) {
//            
//        } else {
//            
//        }
        
        for(BetDto b : couponDto.getBets()) {
            b.getBetOptionId();
            b.getOddId();
        }
        couponDto.getAmount();
        // 1. create new transaction for user account
        // 2. 
    }
}
