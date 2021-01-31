package main.socialnetwork.domain.validators;
import main.socialnetwork.domain.Message;


public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String errorMessage = "";

        if(entity.getFrom()==null || entity.getTo()==null || entity.getDate()==null || entity.getMessage()==null || entity.getMessage().equals(""))
            errorMessage+="Completati toate campurile!\n";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }

}
