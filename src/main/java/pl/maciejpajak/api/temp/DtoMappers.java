package pl.maciejpajak.api.temp;

import java.util.function.Function;
import java.util.stream.Collectors;

import pl.maciejpajak.api.dto.CouponShowDto;
import pl.maciejpajak.api.dto.PlacedBetShowDto;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.coupon.UserCoupon;

public final class DtoMappers {

    private DtoMappers() {}
    
    public static final Function<PlacedBet, PlacedBetShowDto> convertPlacedBetToDto = 
            pb -> PlacedBetShowDto.builder()
                .id(pb.getId())
                .betOptionId(pb.getBetOption().getId())
                .betOptionDescription(pb.getBetOption().getDescription())
                .oddId(pb.getOdd().getId())
                .oddValue(pb.getOdd().getValue())
                .build();
                
            
    public static final Function<UserCoupon, CouponShowDto> convertUserCouponToDto = 
            c -> CouponShowDto.builder()
                .id(c.getId())
                .created(c.getCreated())
                .ownerId(c.getOwner().getId())
                .status(c.getStatus())
                .value(c.getValue())
                .placedBets(c.getPlacedBets().stream().map(convertPlacedBetToDto).collect(Collectors.toSet()))
                .bonus(c.getBonus())
                .totalPrize(c.getTotalPrize())
                .build();
}
