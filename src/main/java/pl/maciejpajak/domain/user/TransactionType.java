package pl.maciejpajak.domain.user;

import java.math.BigDecimal;

public enum TransactionType {

    PLACE_BET(false), WIN(true), RECHARGE(true), WITHDRAW(false), CANCEL_BET(true);
    
    private boolean isPositive;
    
    private TransactionType(boolean isPositive) {
        this.isPositive = isPositive;
    }
    
    public BigDecimal addSign(BigDecimal value) {
        return isPositive ? value : value.negate();
    }
    
}
