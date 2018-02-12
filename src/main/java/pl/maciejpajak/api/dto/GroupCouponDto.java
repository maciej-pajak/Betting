package pl.maciejpajak.api.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupCouponDto extends CouponDto {

    private List<Long> invitedUsersIds;
    
}
