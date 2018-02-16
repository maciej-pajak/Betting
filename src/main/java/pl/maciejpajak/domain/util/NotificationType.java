package pl.maciejpajak.domain.util;

public enum NotificationType {

    COUPON_WON("Your coupon won!"),
    COUPON_LOST("Your coupon lost."),
    NEW_INVITATION("You have been invited to bet with friends."),
    INVITATION_ACCEPTED("Your friend accepted your coupn invitation."),
    COUPON_CANCELLED("Your coupon has been cancelled.");
    
    private String message;
    
    private NotificationType(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
}
