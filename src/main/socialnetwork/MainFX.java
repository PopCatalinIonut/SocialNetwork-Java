package main.socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.socialnetwork.controller.LoginController;
import main.socialnetwork.domain.validators.FriendRequestValidator;
import main.socialnetwork.domain.validators.FriendshipValidator;
import main.socialnetwork.domain.validators.MessageValidator;
import main.socialnetwork.domain.validators.UserValidator;
import main.socialnetwork.repository.file.FriendRequestFileRepository;
import main.socialnetwork.repository.file.FriendshipFileRepository;
import main.socialnetwork.repository.file.MessageFileRepository;
import main.socialnetwork.repository.file.UserFileRepository;
import main.socialnetwork.service.SocialNetworkService;
import main.socialnetwork.ui.Console;

import java.io.IOException;
import java.net.URL;

public class MainFX extends Application {

    private SocialNetworkService socialNetworkService;
    @Override
    public void start(Stage primaryStage) throws Exception{

        UserFileRepository userRepository = new UserFileRepository("data/users.csv", new UserValidator());
        FriendshipFileRepository friendshipRepository = new FriendshipFileRepository("data/friendships.csv", new FriendshipValidator());
        MessageFileRepository messageFileRepository= new MessageFileRepository("data/messages.csv",new MessageValidator());
        FriendRequestFileRepository friendRequestFileRepository= new FriendRequestFileRepository("data/friendRequests.csv",new FriendRequestValidator());
        socialNetworkService = new SocialNetworkService(userRepository, friendshipRepository,messageFileRepository,friendRequestFileRepository);

        initView(primaryStage);
        primaryStage.setTitle("Welcome!");
        primaryStage.show();

    }

    private void initView(Stage primaryStage) throws IOException {
      FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/resources/view/loginView.fxml"));

        AnchorPane root=loader.load();

        LoginController ctrl=loader.getController();
        ctrl.setService(socialNetworkService);
        primaryStage.setScene(new Scene(root, 600, 400));
    }
    public static void main(String[] args) {
        launch(args);
    }

}

