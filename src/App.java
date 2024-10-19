import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class App extends Application {
    private double lastX;
    private double lastY;
    private double brushSize = 10;
    private Color currentColor = Color.BLACK;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox toolbar = new HBox(10);

        Button brushButton = new Button("Brush");
        Button colorButton = new Button("Color");
        Button shapeButton = new Button("Shape");

        Slider brushSizeSlider = new Slider(1, 100, 10);
        brushSizeSlider.setShowTickMarks(true);
        brushSizeSlider.setShowTickLabels(false);
        brushSizeSlider.setMajorTickUnit(10);
        brushSizeSlider.setMinorTickCount(1);
        brushSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            brushSize = newValue.doubleValue();
        });

        ColorPicker colorPicker = new ColorPicker(currentColor);

        toolbar.getChildren().add(0, brushButton);
        toolbar.getChildren().add(1, colorButton);
        toolbar.getChildren().add(2, shapeButton);
        toolbar.getChildren().add(3, brushSizeSlider);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);

        Canvas canvas = new Canvas(800, 500);
        borderPane.setCenter(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            lastX = event.getX();
            lastY = event.getY();
            DrawCircle(event.getX(), event.getY(), graphicsContext);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            DrawLine(lastX, lastY, event.getX(), event.getY(), graphicsContext);
            lastX = event.getX();
            lastY = event.getY();
        });

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Painting App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void DrawCircle(double x, double y, GraphicsContext graphicsContext) {
        graphicsContext.setFill(currentColor);
        graphicsContext.fillOval(x - (brushSize / 2), y - (brushSize / 2), brushSize, brushSize);

    }

    // Stroke rendering
    private void DrawLine(double startX, double startY, double endX, double endY, GraphicsContext graphicsContext) {
        double distance = Math.ceil(Math.hypot(endX - startX, endY - startY));
        for (int i = 0; i <= distance; i++) {
            double interpolatedPosition = (double) i / distance;
            double x = startX + interpolatedPosition * (endX - startX);
            double y = startY + interpolatedPosition * (endY - startY);
            DrawCircle(x, y, graphicsContext);
        }
    }
}