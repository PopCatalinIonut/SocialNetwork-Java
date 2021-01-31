package main.socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.socialnetwork.domain.User;
import main.socialnetwork.repository.RepositoryException;
import main.socialnetwork.service.ServiceException;
import main.socialnetwork.service.SocialNetworkService;

import java.io.IOException;

public class LoginController {
    private SocialNetworkService service;

    @FXML TextField idField;

    public void setService(SocialNetworkService service){
        this.service=service;
    }

    @FXML
    public void handleLogIn(ActionEvent actionEvent) throws IOException {
        if(idField.getText().length()==0)
        MessageAlert.showErrorMessage(null,"ID cannot be empty!");
        else {
            try {
                String Id = idField.getText();
                User currentUser = service.getUser(Long.parseLong(Id));
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/main/resources/view/networkView.fxml"));

                AnchorPane root=loader.load();

                NetworkController ctrl=loader.getController();
                ctrl.setService(service);
                ctrl.setCurrentUser(currentUser);
                this.service.addObserver(ctrl);
                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(root, 650, 350));
                primaryStage.show();
            } catch (RepositoryException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }catch (NumberFormatException e){
                MessageAlert.showErrorMessage(null,"Unrecognized ID!");
            }
        }
    }

    @FXML
    public void handleRegister(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("/main/resources/view/registerView.fxml"));

        AnchorPane root=loader.load();

        RegisterController ctrl=loader.getController();
        ctrl.setService(service);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}
