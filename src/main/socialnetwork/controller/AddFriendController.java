package main.socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.socialnetwork.domain.User;
import main.socialnetwork.service.SocialNetworkService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddFriendController {

    private SocialNetworkService service;


    @FXML TableView<User> tableView = new TableView<User>();
    @FXML TableColumn<User,String> tableColumnFirstName;
    @FXML TableColumn<User,String> tableColumnLastName;
    @FXML TableColumn<List<User>,String> tableColumnFriends;
    @FXML TextField searchField;
    ObservableList<User> model = FXCollections.observableArrayList();
    private User currentUser;
    public void setService(SocialNetworkService service){ this.service=service; }

    public void setCurrentUser(User user){ this.currentUser=user;
        initModel();
    }
    private void initModel() {
        Iterable<User> users = service.getAllUsers();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);
        tableView.setItems(model);
    }

    public void handleAddFriend(ActionEvent actionEvent) {
        User friend = tableView.getSelectionModel().getSelectedItem();
        if(friend!=null)
            service.sendRequest(currentUser,friend);
        else
            MessageAlert.showErrorMessage(null,"You havent selected a user!");

    }

    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        searchField.textProperty().addListener(e -> handleFilter());

    }

    private void handleFilter(){
        String[] datas = searchField.getText().split(" ");
        Predicate<User> nameStartsWith = user -> user.getFirstName().startsWith(datas[0]) || user.getLastName().startsWith(datas[0]);
        Iterable<User> users = service.getAllUsers();
        List<User> usersList = new ArrayList<>();
        if(datas.length>1){
            Predicate<User> lastNameStartWith = user -> user.getLastName().startsWith(datas[1]);
            usersList = StreamSupport.stream(users.spliterator(), false)
                .filter(nameStartsWith.and(lastNameStartWith))
                .collect(Collectors.toList());
        }
        else usersList = StreamSupport.stream(users.spliterator(), false)
                .filter(nameStartsWith)
                .collect(Collectors.toList());
        model.setAll(usersList);
    }
}
