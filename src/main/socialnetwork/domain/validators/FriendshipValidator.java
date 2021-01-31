package main.socialnetwork.domain.validators;

import main.socialnetwork.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorMessage = "";
        if (entity.getId().getLeft().equals(entity.getId().getRight()))
            errorMessage += "Un utilizator nu poate fi prieten cu el insusi!\n";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
