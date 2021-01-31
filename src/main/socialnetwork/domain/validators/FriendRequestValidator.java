package main.socialnetwork.domain.validators;
import main.socialnetwork.domain.FriendRequest;

public class FriendRequestValidator implements Validator<FriendRequest> {
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String errorMessage = "";

        if(entity.getTo()==null || entity.getFrom()==null)
            errorMessage+="Completati toate campurile!\n";
        if(entity.getStatus().compareTo("approved")!=0 && entity.getStatus().compareTo("pending")!=0 && entity.getStatus().compareTo("rejected")!=0)
            errorMessage+="Raspuns invalid, alegeti intre: pending, rejected, approved";
        if (errorMessage.length()!=0)
            throw new ValidationException(errorMessage);
    }

}
