package main.socialnetwork.repository.file;
import main.socialnetwork.domain.Message;
import main.socialnetwork.domain.validators.Validator;
import main.socialnetwork.utils.Constants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageFileRepository extends AbstractFileRepository<Long, Message> {


    @Override
    public Message extractEntity(List<String> attributes) {
        if(attributes.size()==5 || attributes.size()==6){
            String[] strIDs= attributes.get(2).split(" ");
            List<Long> to= new ArrayList<>();
            for(String str: strIDs)
                to.add(Long.parseLong(str));
            if(attributes.size()==5)
                return new Message(Long.parseLong(attributes.get(1)),to,attributes.get(3), LocalDateTime.parse(attributes.get(4),Constants.DATE_TIME_FORMATTER));
            else
                return new Message(Long.parseLong(attributes.get(1)),to,attributes.get(3), LocalDateTime.parse(attributes.get(4),Constants.DATE_TIME_FORMATTER),Long.parseLong(attributes.get(5)));
        }
     return null;
    }

    @Override
    protected String createEntityAsString(Message entity) {

        String toIDs="";
        for(Long ID: entity.getTo())
            toIDs+=ID.toString()+" ";
        if(entity.getReply()==null)
            return entity.getId()+";"+ entity.getFrom()+";"+ toIDs+";"+entity.getMessage()+";"+entity.getDate().format(Constants.DATE_TIME_FORMATTER);
        else
            return entity.getId()+";"+ entity.getFrom()+";"+ toIDs+";"+entity.getMessage()+";"+entity.getDate().format(Constants.DATE_TIME_FORMATTER)+";"+entity.getReply();

    }

    public MessageFileRepository(String fileName, Validator<Message> validator) {
        super(fileName, validator);
    }

}
