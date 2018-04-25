package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        Stage controlStage = primaryStage;
        //Parent root = FXMLLoader.load(getClass().getResource("ControlView.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ControlView.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        //controlStage.initModality(Modality.APPLICATION_MODAL);
        controlStage.setTitle("Cropper");
        controlStage.setScene(new Scene(root));
        controlStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
            }
        });
        controlStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
