package pl.maciejpajak.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupCouponPlaceDto extends CouponPlaceDto {

    private List<Long> invitedUsersIds;
    
}
