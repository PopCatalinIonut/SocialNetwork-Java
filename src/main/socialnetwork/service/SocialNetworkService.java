package main.socialnetwork.service;

import main.socialnetwork.domain.*;
import main.socialnetwork.repository.RepositoryException;
import main.socialnetwork.repository.file.FriendRequestFileRepository;
import main.socialnetwork.repository.file.FriendshipFileRepository;
import main.socialnetwork.repository.file.MessageFileRepository;
import main.socialnetwork.repository.file.UserFileRepository;
import main.socialnetwork.utils.Constants;
import main.socialnetwork.utils.Graph;
import main.socialnetwork.utils.Observer;
import main.socialnetwork.utils.Subject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SocialNetworkService implements Subject {
    private UserFileRepository userFileRepository;
    private FriendshipFileRepository friendshipFileRepository;
    private MessageFileRepository messageFileRepository;
    private FriendRequestFileRepository friendRequestFileRepository;

    private List<Observer> observerList = new ArrayList<>();

    public SocialNetworkService(UserFileRepository userFileRepository, FriendshipFileRepository friendshipFileRepository, MessageFileRepository messageFileRepository, FriendRequestFileRepository friendRequestFileRepository) {
        this.userFileRepository = userFileRepository;
        this.friendshipFileRepository = friendshipFileRepository;
        this.userFileRepository=userFileRepository;
        this.messageFileRepository = messageFileRepository;
        this.friendRequestFileRepository = friendRequestFileRepository;
        corespondingFriends();
    }

    private void corespondingFriends() {

        Iterable<Friendship> friendships = friendshipFileRepository.findAll();

        for (Friendship fr : friendships) {
            Tuple<Long, Long> ids = fr.getId();
            addFriendshipinMemo(ids.getLeft(), ids.getRight(), fr.getDate());
        }
    }

    public void addFriendshipinMemo(Long id1, Long id2, LocalDateTime date) {
        Tuple<User, User> userTuple = getUsers(id1, id2);
        userTuple.getLeft().addFriend(userTuple.getRight());
        userTuple.getRight().addFriend(userTuple.getLeft());
    }

    public Iterable<User> getAllUsers() {
        Iterable<User> users = userFileRepository.findAll();
        return users;
    }

    public Iterable<Friendship> getAllFriendships() {
        return friendshipFileRepository.findAll();
    }

    public void addUser(String Id, String firstName, String lastName) {
        User u = new User(Long.parseLong(Id), firstName, lastName);

        Optional<User> user = userFileRepository.add(u);
        if (user.isPresent())
            addUser(firstName, lastName);
    }

    public void addUser(String firstName, String lastName) {
        User u = new User(firstName, lastName);

        Optional<User> user = userFileRepository.add(u);
        if (user.isPresent())
            addUser(firstName, lastName);
    }

    public void removeUser(String firstName, String lastName) {
        List<User> res = userFileRepository.findByName(firstName, lastName);
        if (res.size() == 1) {
            removeUser(res.get(0).getId());
            return;
        }
        throw new ServiceException("Nu s-a gasit utilizatorul cu datele introduse!");
    }

    public void removeUser(Long id) {
        Optional<User> user = userFileRepository.remove(id);
        if (user.isEmpty())
            throw new ServiceException("Nu s-a gasit utilizatorul cu ID-ul dat!");
    }

    public User getUser(String firstName, String lastName) {
        return userFileRepository.findSingleByName(firstName, lastName);
    }

    public void respondToMessage(Long IDmsg, Long IDuser, String message) {
        String errors = "";
        Optional<Message> msgOpt = messageFileRepository.findOne(IDmsg);
        if (!msgOpt.isPresent())
            errors += "Mesajul cu ID-ul " + IDmsg.toString() + " nu exista";
        else if (msgOpt.get().inTo(IDuser) == false)
            errors += "Utilizatorul nu se afla in lista utilizatorilor care au primit mesajul dat";
        Optional<User> userOpt = userFileRepository.findOne(IDuser);
        if (!userOpt.isPresent())
            errors += "Utilizatorul cu ID-ul " + IDuser.toString() + " nu exista\n";
        if (errors.length() > 0)
            throw new ServiceException(errors);
        User from = userOpt.get();
        Message msg = msgOpt.get();
        Message reply = new Message(from.getId(), msg.getTo(), message, LocalDateTime.now(), msg.getId());
        this.messageFileRepository.add(reply);
    }

    public Tuple<User, User> getUsers(String firstName1, String lastName1, String firstName2, String lastName2) {
        List<User> res1 = userFileRepository.findByName(firstName1, lastName1);
        List<User> res2 = userFileRepository.findByName(firstName2, lastName2);
        if (res1.size() == 1 && res2.size() == 1) {
            return new Tuple<>(res1.get(0), res2.get(0));
        }
        String errorMessage = "";
        if (res1.isEmpty())
            errorMessage += "Primul utilizator nu exista!\n";
        if (res2.isEmpty())
            errorMessage += "Al doilea utilizator nu exista!\n";
        throw new ServiceException(errorMessage);
    }

    public List<Message> getMessages(User user1, User user2){
        Iterable<Message> messagesList = messageFileRepository.findAll();
        return StreamSupport.stream(messagesList.spliterator(), false)
                .filter((x -> (x.getTo().contains(user1.getId()) && x.getFrom() == user2.getId()) || (x.getTo().contains(user2.getId()) && x.getFrom() == user1.getId())))
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
    }
    public List<String> getConvos(User user1, User user2){
        Iterable<Message> messagesList = messageFileRepository.findAll();
        List<Message> filteredMessages = StreamSupport.stream(messagesList.spliterator(), false)
                .filter((x -> (x.getTo().contains(user1.getId()) && x.getFrom() == user2.getId()) || (x.getTo().contains(user2.getId()) && x.getFrom() == user1.getId())))
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
        List<String> messagesAsString = new ArrayList<>();
        for (Message msg : filteredMessages) {
            User User1 = this.userFileRepository.findOne(msg.getFrom()).get();
            int User2Poz = msg.getTo().indexOf(user1.getId() + user2.getId() - User1.getId());
            User User2 = this.userFileRepository.findOne(msg.getTo().get(User2Poz)).get();
            String reply="";
            if(msg.getReply()!=null)
                reply=" replied to '" + this.messageFileRepository.findOne(msg.getReply()).get().getMessage()+ "'";
            if (User1.getId() == user1.getId())
                messagesAsString.add(msg.getDate().format(Constants.DATE_TIME_FORMATTER) + " " + User2.getLastName()+ reply + " : " + msg.getMessage());
            else
                messagesAsString.add(msg.getDate().format(Constants.DATE_TIME_FORMATTER) + " me" +reply+ ": " + msg.getMessage());

        }
        return messagesAsString;
    }

    public Tuple<User, User> getUsers(Long id1, Long id2) {
        Optional<User> user1 = userFileRepository.findOne(id1);
        Optional<User> user2 = userFileRepository.findOne(id2);

        if (user1.isPresent() && user2.isPresent())
            return new Tuple<>(user1.get(), user2.get());

        String errorMessage = "";
        if (user1.isEmpty())
            errorMessage += "Primul utilizator nu exista!\n";
        if (user2.isEmpty())
            errorMessage += "Al doilea utilizator nu exista!\n";
        throw new ServiceException(errorMessage);
    }

    public void respondToRequest(Long ID, String newStatus) {

        Optional<FriendRequest> fr = friendRequestFileRepository.findOne(ID);
        if (!fr.isPresent())
            throw new ServiceException("Nu s-a gasit cererea cu ID-ul introdus!");
        FriendRequest friendRequest = fr.get();
        friendRequest.setStatus(newStatus);
        this.friendRequestFileRepository.update(friendRequest);
        if (friendRequest.getStatus().equals("approved")){
            addFriendship(friendRequest.getFrom(), friendRequest.getTo(), LocalDateTime.now());
            notifyAllObs();
        }
    }

    public Iterable<FriendRequest> getAllRequests() {
        return this.friendRequestFileRepository.findAll();
    }

    public Iterable<Message> getAllMessages() {
        return this.messageFileRepository.findAll();
    }

    public void addFriendship(String firstName1, String lastName1, String firstName2, String lastName2, LocalDateTime date) {
        Tuple<User, User> userTuple = getUsers(firstName1, lastName1, firstName2, lastName2);
        addFriendship(userTuple.getLeft(), userTuple.getRight(), date);
    }

    public void addFriendship(Long id1, Long id2, LocalDateTime date) {
        Tuple<User, User> userTuple = getUsers(id1, id2);
        addFriendship(userTuple.getLeft(), userTuple.getRight(), date);
    }

    private void addFriendship(User user1, User user2, LocalDateTime date) {
        user1.getFriends().add(user2);
        user2.getFriends().add(user1);

        Friendship f = new Friendship(date);
        f.setId(new Tuple<>(user1.getId(), user2.getId()));
        Optional<Friendship> fs = friendshipFileRepository.add(f);
        fs.ifPresent(x -> {
            throw new ServiceException("Prietenia deja exista!");
        });
    }

    public void removeFriendship(String firstName1, String lastName1, String firstName2, String lastName2) {
        Tuple<User, User> userTuple = getUsers(firstName1, lastName1, firstName2, lastName2);
        removeFrienship(userTuple.getLeft(), userTuple.getRight());
    }

    public void removeFrienship(User user1, User user2) {
        Optional<Friendship> f = friendshipFileRepository.remove(new Tuple<>(user1.getId(), user2.getId()));
        Optional<Friendship> f2 = friendshipFileRepository.remove(new Tuple<>(user2.getId(), user1.getId()));
        if (f.isPresent() || f2.isPresent()) {
            user1.getFriends().remove(user2);
            user2.getFriends().remove(user1);
            notifyAllObs();
            return;
        }
        throw new ServiceException("Utilizatorii nu sunt prieteni!");
    }

    public List<Friendship> getFriendships(String firstName, String lastName) {
        User user = getUser(firstName, lastName);
        Iterable<Friendship> friendshipsList = friendshipFileRepository.findAll();
        return StreamSupport.stream(friendshipsList.spliterator(), false)
                .filter(x -> x.getId().getLeft() == user.getId() || x.getId().getRight() == user.getId())
                .collect(Collectors.toList());

    }

    public List<String> getFriendList(String firstName, String lastName) {

        List<String> friendsList = new ArrayList<>();
        List<Friendship> rezList = getFriendships(firstName, lastName);
        User user = getUser(firstName, lastName);
        for (Friendship friendship : rezList) {
            Optional<User> other;
            if (friendship.getId().getRight() == user.getId())
                other = userFileRepository.findOne(friendship.getId().getLeft());
            else
                other = userFileRepository.findOne(friendship.getId().getRight());
            friendsList.add(other.get().getFirstName() + " " + other.get().getLastName() + " " + friendship.getDate());
        }
        return friendsList;
    }

    public List<String> getFriendList(String firstName, String lastName, int month) {
        List<String> friendsList = new ArrayList<>();
        List<Friendship> rezList = getFriendships(firstName, lastName);
        User user = getUser(firstName, lastName);
        for (Friendship friendship : rezList) {
            Optional<User> other;
            if (friendship.getDate().getMonthValue() == month) {
                if (friendship.getId().getRight() == user.getId())
                    other = userFileRepository.findOne(friendship.getId().getLeft());
                else
                    other = userFileRepository.findOne(friendship.getId().getRight());
                friendsList.add(other.get().getFirstName() + " " + other.get().getLastName() + " " + friendship.getDate().format(Constants.DATE_TIME_FORMATTER));
            }
        }
        return friendsList;
    }

    public int countCommunities() {
        ArrayList<Friendship> edges = new ArrayList<>();
        friendshipFileRepository.findAll().forEach(edges::add);
        HashMap<Long, ArrayList<Long>> adjList = new HashMap<>();
        for (Friendship node : edges) {
            ArrayList<Long> user1 = adjList.get(node.getId().getLeft());
            ArrayList<Long> user2 = adjList.get(node.getId().getRight());
            if (user1 == null || user2 == null) {
                user1 = new ArrayList<>();
                user1.add(node.getId().getRight());
                user2 = new ArrayList<>();
                user2.add(node.getId().getLeft());
                adjList.put(node.getId().getLeft(), user1);
                adjList.put(node.getId().getRight(), user2);
            } else {
                user1.add(node.getId().getRight());
                user2.add(node.getId().getLeft());
            }
        }

        Graph g = new Graph(adjList.size());
        g.setAdjList(adjList);
        return g.countConnectedComponents();
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyAllObs() {
        for (Observer observer : observerList) {
            observer.update();
        }
    }


    public void sendRequest(User user1, User user2) {
        FriendRequest fr = new FriendRequest(user1.getId(), user2.getId(), "pending",LocalDateTime.now());
        this.friendRequestFileRepository.add(fr);
        notifyAllObs();
    }

    public List<FriendRequest> getUserRequests(User currentUser) {

        Iterable<FriendRequest> fr = this.getAllRequests();
        List<FriendRequest> filtered = StreamSupport.stream(fr.spliterator(), false)
                .filter(x -> x.getTo() == currentUser.getId())
                .collect(Collectors.toList());
        List<User> userList = new ArrayList<>();

        for (FriendRequest freq : filtered) {
            userList.add(this.userFileRepository.findOne(freq.getFrom()).get());
        }
        return filtered;
    }

    public User getUser(Long id) {
        Optional<User> user=this.userFileRepository.findOne(id);
        if(user.isPresent())
            return user.get();
        else throw new RepositoryException("User doesn't exist!");
    }

    public void deleteRequest(FriendRequest fr) {
        this.friendRequestFileRepository.remove(fr.getId());
        notifyAllObs();
    }

    public Message getMessage(Long reply) {
        return this.messageFileRepository.findOne(reply).get();
    }

    public void sendMessage(Message newMsg) {
        this.messageFileRepository.add(newMsg);
        notifyAllObs();
    }
}
