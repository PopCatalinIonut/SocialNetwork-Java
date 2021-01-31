package main.socialnetwork.controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import main.socialnetwork.domain.Message;
import main.socialnetwork.domain.User;
import main.socialnetwork.service.SocialNetworkService;
import main.socialnetwork.utils.Constants;
import main.socialnetwork.utils.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessagesController implements Observer {

    private SocialNetworkService service;
    private User currentUser;
    @FXML ListView<User> friendsListView = new ListView<>();
    @FXML ListView<Message> messagesListView = new ListView<>();
    @FXML TextField textToSend;
    ObservableList<User> friendsModel = FXCollections.observableArrayList();
    ObservableList<Message> messagesModel = FXCollections.observableArrayList();
    public void setService(SocialNetworkService service) { this.service = service;
    initModel();
    }

    public void initModel(){
        Iterable<User> friends = service.getAllUsers();
        List<User> friendsList = StreamSupport.stream(friends.spliterator(), false)
                .filter(x-> x.getFriends().contains(currentUser))
                .collect(Collectors.toList());
        friendsModel.setAll(friendsList);
        friendsListView.setItems(friendsModel);
    }
    public void initialize(){
        friendsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        friendsListView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<User>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends User> c) {
                List<User> usr = friendsListView.getSelectionModel().getSelectedItems();
                if(usr.size()==1){
                List<Message> messages = service.getMessages(currentUser,usr.get(0));
                messagesModel.setAll(messages);
                messagesListView.setItems(messagesModel);
                }
                else messagesModel.setAll();
            }
            });
        messagesListView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null)
                    setText(null);
                 else {
                    User usr = friendsListView.getSelectionModel().getSelectedItem();
                    if(usr!=null){
                    String messageAsString="";
                    String reply="";
                    if(item.getReply()!=null)
                        reply=" replied to '" + service.getMessage(item.getReply()).getMessage()+ "'";
                    if (item.getTo().contains(currentUser.getId()))
                        messageAsString=item.getDate().format(Constants.DATE_TIME_FORMATTER) + " " + usr.getLastName()+ reply + " : " + item.getMessage();
                    else
                        messageAsString=item.getDate().format(Constants.DATE_TIME_FORMATTER) + " me" +reply+ ": " + item.getMessage();
                    setText(messageAsString);
            }}
        }});
    }

    public void setCurrentUSer(User currentUser) { this.currentUser=currentUser; }


    public void update() {
        initModel();
    }

    public void handleSend(ActionEvent actionEvent) {
        String messageToSend = textToSend.getText();
        List<User> users = friendsListView.getSelectionModel().getSelectedItems().stream().collect(Collectors.toList());
        List<Long> usersIds= new ArrayList<>();
        users.forEach(x-> usersIds.add(x.getId()));

        Message newMsg = new Message(currentUser.getId(), usersIds,messageToSend, LocalDateTime.now());
        Message replied = messagesListView.getSelectionModel().getSelectedItem();
        if(replied!=null && users.size()==1)
            newMsg.setReply(replied.getId());

        this.service.sendMessage(newMsg);
    }
}
