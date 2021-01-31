package main.socialnetwork.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.socialnetwork.domain.FriendRequest;
import main.socialnetwork.domain.User;
import main.socialnetwork.service.SocialNetworkService;
import main.socialnetwork.utils.Observer;

import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestsController implements Observer {

    private SocialNetworkService service;

    @FXML TableView<FriendRequest> receivedTable = new TableView<FriendRequest>();
    @FXML TableColumn<FriendRequest,String> fromColumnReceivedTable;
    @FXML TableColumn<FriendRequest,String> statusColumnReceivedTable;
    @FXML TableColumn<FriendRequest, LocalDateTime> dateColumnReceivedTable;

    @FXML TableView<FriendRequest> sentTable;
    @FXML TableColumn<FriendRequest,String> toColumnSentTable;
    @FXML
    TableColumn<FriendRequest, LocalDateTime> dateColumnSentTable;
    ObservableList<FriendRequest> receivedTableModel = FXCollections.observableArrayList();
    ObservableList<FriendRequest> sentTableModel = FXCollections.observableArrayList();
    private User currentUser;


    public void setCurrentUSer(User user){
        this.currentUser=user;

    }
    public void setService(SocialNetworkService service){ this.service=service;
        initModel();
    }

    private void initModel() {
        Iterable<FriendRequest> received = service.getAllRequests();
        List<FriendRequest> receivedList = StreamSupport.stream(received.spliterator(), false)
                .filter(x->x.getTo()==currentUser.getId())
                .collect(Collectors.toList());
        receivedTableModel.setAll(receivedList);
        receivedTable.setItems(receivedTableModel);

        Iterable<FriendRequest> sent = service.getAllRequests();
        List<FriendRequest> sentList = StreamSupport.stream(received.spliterator(), false)
                .filter(x->x.getFrom()==currentUser.getId() && !x.getStatus().equals("rejected") && !x.getStatus().equals("approved"))
                .collect(Collectors.toList());
        sentTableModel.setAll(sentList);
        sentTable.setItems(sentTableModel);
    }

    public void initialize(){
        fromColumnReceivedTable.setCellValueFactory(param -> {
            FriendRequest value = param.getValue();
            User user = service.getUser(value.getFrom());
            return new SimpleStringProperty(user.getFirstName()+ " " + user.getLastName());
        });
        statusColumnReceivedTable.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("status"));
        dateColumnReceivedTable.setCellValueFactory(new PropertyValueFactory<FriendRequest, LocalDateTime>("date"));
        toColumnSentTable.setCellValueFactory(param ->{
            FriendRequest value=param.getValue();
            User user= service.getUser(value.getTo());
            return  new SimpleStringProperty(user.getFirstName()+ " "+ user.getLastName());
        });
        dateColumnSentTable.setCellValueFactory(new PropertyValueFactory<FriendRequest, LocalDateTime>("date"));
    }
    public void handleAccept(ActionEvent actionEvent) {

        FriendRequest fr = receivedTable.getSelectionModel().getSelectedItem();
        if(fr.getStatus().equals("pending"))
            this.service.respondToRequest(fr.getId(),"approved");
        else
            MessageAlert.showErrorMessage(null,"Request already replied!");
        initModel();
    }

    public void handleReject(ActionEvent actionEvent) {
        FriendRequest fr = receivedTable.getSelectionModel().getSelectedItem();
        if(fr.getStatus().equals("pending"))
            this.service.respondToRequest(fr.getId(),"rejected");
        else MessageAlert.showErrorMessage(null,"Request already replied!");
        initModel();
    }

    @Override
    public void update() {
        initModel();
    }

    public void handleDeleteRequest(ActionEvent actionEvent) {
        FriendRequest fr = sentTable.getSelectionModel().getSelectedItem();
        if(fr==null)
            MessageAlert.showErrorMessage(null,"Select a request!");
        else
            this.service.deleteRequest(fr);
    }
}
