package main.socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.socialnetwork.domain.*;
import main.socialnetwork.service.ServiceException;
import main.socialnetwork.service.SocialNetworkService;
import main.socialnetwork.utils.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NetworkController implements Observer {

    private SocialNetworkService service;
    ObservableList<User> model = FXCollections.observableArrayList();
    @FXML TableView<User> tableView = new TableView<User>();
    @FXML TableColumn<User,String> tableColumnFirstName;
    @FXML TableColumn<User,String> tableColumnLastName;
    @FXML Label welcomeText;
    private User currentUser;

    public void setCurrentUser(User user){ this.currentUser=user;
        initModel();
    }
    public void setService(SocialNetworkService service){
        this.service=service;
    }

    private void initModel() {
        Iterable<User> users = service.getAllUsers();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .filter(x-> x.getFriends().contains(currentUser))
                .collect(Collectors.toList());
        model.setAll(usersList);
        tableView.setItems(model);
        welcomeText.setText("Welcome back \n"+ currentUser.getFirstName()+ " " + currentUser.getLastName()+ "!");
   
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
    }
    @FXML
    public void handleDeleteFriend(ActionEvent actionEvent) {
        User friend = tableView.getSelectionModel().getSelectedItem();
        if(friend!=null){
            try{
            service.removeFrienship(friend,currentUser);
            model.remove(friend);
            }catch ( ServiceException e){
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null,"You havent selected a user!");

    }

    public void handleAddFriend(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/resources/view/addFriendView.fxml"));

        AnchorPane root=loader.load();

        AddFriendController ctrl=loader.getController();
        ctrl.setService(service);
        ctrl.setCurrentUser(currentUser);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, 300, 350));
        primaryStage.show();

    }

    @Override
    public void update() {
        Iterable<User> users = service.getAllUsers();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .filter(x-> x.getFriends().contains(currentUser))
                .collect(Collectors.toList());
        model.setAll(usersList);
        tableView.setItems(model);
    }

    public void handleRequests(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/resources/view/friendRequestsView.fxml"));

        AnchorPane root=loader.load();
        FriendRequestsController ctrl=loader.getController();
        ctrl.setCurrentUSer(currentUser);
        ctrl.setService(service);
        service.addObserver(ctrl);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public void handleMessages(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/resources/view/messagesView.fxml"));

        AnchorPane root=loader.load();
        MessagesController ctrl=loader.getController();
        ctrl.setCurrentUSer(currentUser);
        ctrl.setService(service);
        service.addObserver(ctrl);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}
