package main.socialnetwork.repository.file;

import main.socialnetwork.domain.Friendship;
import main.socialnetwork.domain.Tuple;
import main.socialnetwork.domain.User;
import main.socialnetwork.domain.validators.Validator;
import main.socialnetwork.utils.Constants;
import main.socialnetwork.utils.Observer;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {
    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        if (attributes.size() == 3) {
            Friendship friendship = new Friendship(LocalDateTime.parse(attributes.get(2), Constants.DATE_TIME_FORMATTER));
            Tuple<Long, Long> friendshipID = new Tuple<>(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1)));
            friendship.setId(friendshipID);
            return friendship;
        }
        return null;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" + entity.getDate().format(Constants.DATE_TIME_FORMATTER);
    }


    public void update(User entity) {
        entities.keySet().removeIf(k -> k.getLeft().equals(entity.getId()) || k.getRight().equals(entity.getId()));
        updateFile();
    }

    public void remove(Long id1, Long id2){
        this.entities.remove(id1,id2);
        this.entities.remove(id2,id1);
    }
}
