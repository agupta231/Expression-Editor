import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DragAndDropExercise extends Application {
    public static void main (String[] args) {
        launch(args);
    }

    private static class MouseEventHandler implements EventHandler<MouseEvent> {
        private Label _label;

        /* IMPLEMENT ME */
        double _lastX, _lastY;

        MouseEventHandler (Label label) {
            _label = label;
        }

        public void handle (MouseEvent event) {
            final double sceneX = event.getSceneX();
            final double sceneY = event.getSceneY();

            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                // IMPLEMENT ME
            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                _label.setTranslateX(_label.getTranslateX() + (sceneX - _lastX));
                _label.setTranslateY(_label.getTranslateY() + (sceneY - _lastY));
            } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                _label.setLayoutX(_label.getLayoutX() + _label.getTranslateX());
                _label.setLayoutY(_label.getLayoutY() + _label.getTranslateY());
                _label.setTranslateX(0);
                _label.setTranslateY(0);
            }

            _lastX = sceneX;
            _lastY = sceneY;
        }
    }


    @Override
    public void start (Stage primaryStage) {
        primaryStage.setTitle("EventDrivenProgram");

        final Pane root = new Pane();
        final Label label = new Label("Drag me!");
        root.getChildren().add(label);

        final MouseEventHandler handler = new MouseEventHandler(label);
        label.setOnMousePressed(handler);
        label.setOnMouseDragged(handler);
        label.setOnMouseReleased(handler);
        primaryStage.setScene(new Scene(root, 600, 480));
        primaryStage.show();
    }
}
