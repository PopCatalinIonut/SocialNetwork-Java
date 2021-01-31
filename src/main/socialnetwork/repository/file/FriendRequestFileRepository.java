package main.socialnetwork.repository.file;

import main.socialnetwork.domain.FriendRequest;
import main.socialnetwork.domain.validators.Validator;
import main.socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class FriendRequestFileRepository extends  AbstractFileRepository<Long, FriendRequest> {
    public FriendRequestFileRepository(String fileName, Validator<FriendRequest> validator) {
        super(fileName, validator);
    }

    @Override
    public FriendRequest extractEntity(List<String> attributes) {
        if(attributes.size()==5){
            FriendRequest friendRequest= new FriendRequest(Long.parseLong(attributes.get(1)),Long.parseLong(attributes.get(2)),attributes.get(3), LocalDateTime.parse(attributes.get(4), Constants.DATE_TIME_FORMATTER));
            friendRequest.setId(Long.parseLong(attributes.get(0)));
            return friendRequest;
        }
        return null;
    }

    @Override
    protected String createEntityAsString(FriendRequest entity) {
        return entity.getId()+";"+entity.getFrom()+";"+entity.getTo()+";"+entity.getStatus()+";"+entity.getDate().format(Constants.DATE_TIME_FORMATTER);
    }

}
