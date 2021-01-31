package main.socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.socialnetwork.domain.validators.ValidationException;
import main.socialnetwork.repository.RepositoryException;
import main.socialnetwork.service.ServiceException;
import main.socialnetwork.service.SocialNetworkService;


public class RegisterController {

    SocialNetworkService service;

    @FXML TextField idField;
    @FXML TextField firstNameField;
    @FXML TextField lastNameField;
    @FXML Button registerButton;


    public void setService(SocialNetworkService service) {
        this.service = service;
    }

    public void handleRegister(ActionEvent actionEvent) {
        String id = idField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        if(id.length()==0)
            MessageAlert.showErrorMessage(null,"ID cannot be empty!");

        try {
            service.addUser(id, firstName, lastName);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Register","Registration done! You can nou log in.");
        }catch (RepositoryException | ServiceException | ValidationException e ){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }
}
