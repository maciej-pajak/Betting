package pl.maciejpajak.api.temp;

import java.util.function.Function;
import java.util.stream.Collectors;

import pl.maciejpajak.api.dto.CouponInvitationShowDto;
import pl.maciejpajak.api.dto.CouponShowDto;
import pl.maciejpajak.api.dto.PlacedBetShowDto;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.coupon.Coupon;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.coupon.GroupCoupon;

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
        
    public static final Function<CouponInvitation, CouponInvitationShowDto> convertInvitationToDto =
            inv -> CouponInvitationShowDto.builder()
                .invitedUserId(inv.getInvitedUser().getId())
                .userTransactionTime((inv.getBetTransaction() != null) ? inv.getBetTransaction().getOperationTime() : null)
                .userTransactoinAmount((inv.getBetTransaction() != null) ? inv.getBetTransaction().getAmount() : null)
                .build();
            
    public static final Function<Coupon, CouponShowDto> convertUserCouponToDto = 
            c -> CouponShowDto.builder()
                .id(c.getId())
                .created(c.getCreated())
                .ownerId(c.getOwner().getId())
                .ownerTransactionAmount(c.getOwnerTransaction().getAmount())
                .invitations( 
                        (c instanceof GroupCoupon) 
                            ? ((GroupCoupon) c).getIntivations().stream()
                                    .map(convertInvitationToDto).collect(Collectors.toSet())
                            : null)
                .status(c.getStatus())
                .value(c.getValue())
                .placedBets(c.getPlacedBets().stream().map(convertPlacedBetToDto).collect(Collectors.toSet()))
                .bonus(c.getBonus())
                .totalPrize(c.getTotalPrize())
                .build();
            
    
}
