package pl.maciejpajak.api.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlacedGroupCouponDto extends PlacedCouponDto {

    private List<Long> invitedUsersIds;
    
}
