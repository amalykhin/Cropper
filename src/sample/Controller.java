/*
TODO:

Kinda fixed it. Just removed the cropping rectangle from FXML and
now add and remove it every time the crop needs to be redrawn.
-Why shadingShape applies padding on second creation?

fixme.MOUSE_EXITED_TARGET fires on cropRectangle even when cursor is still in the rectangle boundaries.
todo.Connect width and height fields to cropRectangle width and height properties.
todo.Ratio lock.
todo.Actual cropping.
*/

package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller {
    private Rectangle cropRectangle;
    private Rectangle cropRectangleView;
    private Stage imageStage;
    private Rectangle shadingRectangle;
    private Shape shadingShape;
    @FXML
    private TextField filePathField;
    @FXML
    private Pane imagePane;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;

    private class CropDragHandler {
        private double cropOldPosX;
        private double cropOldPosY;
        private double mousePosX;
        private double mousePosY;
        private boolean isPressed = false;

        public void onPress(MouseEvent e) {
            mousePosX = e.getX();
            mousePosY = e.getY();
            cropOldPosX = cropRectangleView.getX();
            cropOldPosY = cropRectangleView.getY();
            isPressed = true;
        }

        public void onDrag(MouseEvent e) {
            cropRectangleView.setX(cropOldPosX+e.getX()-mousePosX);
            cropRectangleView.setY(cropOldPosY+e.getY()-mousePosY);
            if (cropRectangleView.getX() < 0)
                cropRectangleView.setX(0);
            if (cropRectangleView.getY() < 0)
                cropRectangleView.setY(0);
            if (cropRectangleView.getX()+cropRectangleView.getWidth() > imagePane.getWidth())
                cropRectangleView.setX(imagePane.getWidth()-cropRectangleView.getWidth());
            if (cropRectangleView.getY()+cropRectangleView.getHeight() > imagePane.getHeight())
                cropRectangleView.setY(imagePane.getHeight()-cropRectangleView.getHeight());
            //cropRectangleViewView.setX(cropOldPosX+e.getX()-mousePosX);
            //cropRectangleViewView.setY(cropOldPosY+e.getY()-mousePosY);
            //if (cropRectangleViewView.getX() < 0)
            //    cropRectangleViewView.setX(0);
            //if (cropRectangleViewView.getY() < 0)
            //    cropRectangleViewView.setY(0);
            //if (cropRectangleViewView.getX()+cropRectangleViewView.getWidth() > imagePane.getWidth())
            //    cropRectangleViewView.setX(imagePane.getWidth()-cropRectangleViewView.getWidth());
            //if (cropRectangleViewView.getY()+cropRectangleViewView.getHeight() > imagePane.getHeight())
            //    cropRectangleViewView.setY(imagePane.getHeight()-cropRectangleViewView.getHeight());
            drawCrop();
        }

        public void onRelease(MouseEvent e) {
            isPressed = false;
        }
    }

    public Controller() {
        CropDragHandler cropMouseHandler = new CropDragHandler();
        imageStage = new Stage();
        imageStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                imageStage.close();
            }
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageView.fxml"));
        loader.setController(this);
        Parent imageSceneRoot;
        try {
            imageSceneRoot = loader.load();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
        //imageSceneRoot.getChildren().addAll(shadingRectangle, cropRectangle);
        imageStage.setScene(new Scene(imageSceneRoot));
        imageStage.setResizable(false);
        imageStage.setX(0);
        imageStage.setY(0);
        //cropRectangle.setX(0);
        //cropRectangle.setY(0);
        cropRectangle = new Rectangle();
        cropRectangleView = new Rectangle();
        cropRectangleView.addEventHandler(MouseEvent.MOUSE_PRESSED, cropMouseHandler::onPress);
        cropRectangleView.addEventHandler(MouseEvent.MOUSE_DRAGGED, cropMouseHandler::onDrag);
        //cropRectangle.setOnDragOver(cropMouseHandler::onDrag);
        /*cropRectangle.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            private double cropOldPosX;
            private double cropOldPosY;
            private double mousePosX;
            private double mousePosY;
            private boolean isPressed = false;

            @Override
            public void handle(MouseEvent e) {
                EventType<?> et = e.getEventType();
                if (et.equals(MouseEvent.MOUSE_PRESSED)) {
                    mousePosX = e.getX();
                    mousePosY = e.getY();
                    cropOldPosX = cropRectangle.getX();
                    cropOldPosY = cropRectangle.getY();
                    isPressed = true;

                    System.out.println("onPress");
                } else if (et.equals(MouseEvent.MOUSE_DRA)) {
                    cropRectangle.setX(cropOldPosX+e.getX()-mousePosX);
                    cropRectangle.setY(cropOldPosY+e.getY()-mousePosY);
                    drawCrop();

                    System.out.println("onDrag");
                } else if (et.equals(MouseEvent.MOUSE_RELEASED)) {
                    isPressed = false;

                    System.out.println("onRelease");
                }
            }
        });
        */
        //cropRectangle.addEventHandler(MouseEvent.ANY, e -> System.out.println(e.getTarget().getClass().getSimpleName()+" "+e.getEventType()+" X="+e.getX()+" Y="+e.getY()));
        //imageStage.addEventHandler(MouseEvent.ANY, e -> System.out.println(e.getTarget().getClass().getSimpleName()+" "+e.getEventType()+" X="+e.getX()+" Y="+e.getY()));
        cropRectangleView.addEventHandler(MouseEvent.MOUSE_RELEASED, cropMouseHandler::onRelease);
        //cropRectangleView.setFill(Color.TRANSPARENT);
        cropRectangleView.setFill(Color.TRANSPARENT);
        //cropRectangleView.setOpacity(0.1);
        cropRectangleView.setStroke(Color.BLUE.desaturate());
        cropRectangleView.setStrokeType(StrokeType.OUTSIDE);
        //cropRectangleView.setStrokeLineJoin(StrokeLineJoin.ROUND);
        cropRectangleView.setStrokeWidth(2);
        cropRectangle.xProperty().bind(cropRectangleView.xProperty());
        cropRectangle.yProperty().bind(cropRectangleView.yProperty());
        cropRectangle.widthProperty().bind(cropRectangleView.widthProperty());
        cropRectangle.heightProperty().bind(cropRectangleView.heightProperty());
        cropRectangle.strokeWidthProperty().bind(cropRectangleView.strokeWidthProperty());
        imagePane.getChildren().add(cropRectangleView);
        shadingRectangle = new Rectangle();
        //shadingRectangle.setX(0);
        //shadingRectangle.setY(0);
    }

    @FXML
    public void openImage(ActionEvent ae) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open an image...");
        fc.getExtensionFilters().add(new ExtensionFilter("Image files", "*.png",
                                                                                   "*.jpg",
                                                                                   "*.jpeg",
                                                                                   "*.bmp"));
        Image image;
        Stage controlStage = (Stage)filePathField.getScene().getWindow();
        File imageFile = fc.showOpenDialog(controlStage);
        if (imageFile == null) {
            if (filePathField.getLength() > 0) {
                imageStage.show();
                controlStage.requestFocus();
            }
            return;
        }
        filePathField.setText(imageFile.getPath());
        try {
            image = new Image(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return;
        }

        shadingRectangle.setHeight(image.getHeight());
        shadingRectangle.setWidth(image.getWidth());
        //cropRectangle.setHeight(image.getHeight());
        //cropRectangle.setWidth(image.getWidth());
        heightField.setText(""+(int)image.getHeight());
        widthField.setText(""+(int)image.getWidth());
        cropRectangleView.setHeight(300);
        cropRectangleView.setWidth(300);
        drawCrop();
        imageView.setImage(image);

        imageStage.setTitle("Cropper image preview ("+imageFile.getName()+")");
        imageStage.sizeToScene();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        if (imageStage.getWidth()+imageStage.getX() > screenBounds.getMaxX()) {
            double requestedWidth = imageStage.getWidth();
            imageStage.setWidth(screenBounds.getMaxX() - imageStage.getX());
            requestedWidth -= screenBounds.getMaxX() - imageStage.getX();
            if (requestedWidth > imageStage.getX()-screenBounds.getMinX()) {
                imageStage.setX(screenBounds.getMinY());
                imageStage.setWidth(screenBounds.getWidth());
            } else {
                imageStage.setX(imageStage.getX() - requestedWidth);
                imageStage.setWidth(imageStage.getWidth() + requestedWidth);
            }
        }
        if (imageStage.getHeight()+imageStage.getY() > screenBounds.getMaxY()) {
            double requestedHeight = imageStage.getHeight();
            imageStage.setHeight(screenBounds.getMaxY() - imageStage.getY());
            requestedHeight -= screenBounds.getMaxY() - imageStage.getY();
            if (requestedHeight > imageStage.getY()-screenBounds.getMinY()) {
                imageStage.setY(screenBounds.getMinY());
                imageStage.setHeight(screenBounds.getHeight());
            } else {
                imageStage.setY(imageStage.getY() - requestedHeight);
                imageStage.setHeight(imageStage.getHeight() + requestedHeight);
            }
        }
        imageStage.show();
        controlStage.requestFocus();
    }

    private void drawCrop () {
        imagePane.getChildren().removeAll(shadingShape);
        shadingShape = Shape.subtract(shadingRectangle, cropRectangle);
        shadingShape.setFill(Color.BLACK);
        shadingShape.setOpacity(0.5);
        cropRectangle.getWidth();
        imagePane.getChildren().addAll(shadingShape);
    }
}
