package main.socialnetwork.repository.file;

import main.socialnetwork.domain.User;
import main.socialnetwork.domain.validators.Validator;
import main.socialnetwork.repository.RepositoryException;
import main.socialnetwork.utils.Observer;
import main.socialnetwork.utils.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserFileRepository extends AbstractFileRepository<Long, User> {
    private List<Observer> observers = new ArrayList<>();

    public UserFileRepository(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    public List<User> findByName(String firstName, String lastName) {
       List<User> users=entities.values()
                .stream()
                .filter(u -> u.getFirstName().equals(firstName) && u.getLastName().equals(lastName))
                .collect(Collectors.toList());
        if(users.size()==0)
            throw new RepositoryException("Userul " + firstName + " " + lastName+ " nu exista!");
        return users;
    }

    public User findSingleByName(String firstName, String lastName) {
        List<User> users= entities.values()
                .stream()
                .filter(u -> u.getFirstName().equals(firstName) && u.getLastName().equals(lastName))
                .collect(Collectors.toList());
        if(users.size()==0)
            throw new RepositoryException("Userul " + firstName + " " + lastName+ " nu exista!");
        return users.get(0);
    }

    @Override
    public Optional<User> remove(Long id) {
        Optional<User> user = super.remove(id);
        return user;
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(Long.parseLong(attributes.get(0)),attributes.get(1), attributes.get(2));
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }

}
