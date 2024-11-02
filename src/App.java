import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class App extends Application {
    private double lastX;
    private double lastY;
    private static final double CIRCLE_RADIUS = 10; // Radius of the circle
    private static final double CIRCLE_OVERLAP = 5; // Overlap distance between circles

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox toolbar = new HBox(10);

        Button brushButton = new Button("Brush");
        Button colorButton = new Button("Color");
        Button shapeButton = new Button("Shape");

        toolbar.getChildren().addAll(brushButton, colorButton, shapeButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);

        Canvas canvas = new Canvas(800, 500);
        borderPane.setCenter(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            lastX = event.getX();
            lastY = event.getY();
            Draw(event.getX(), event.getY(), graphicsContext); // Draw initial circle
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            // Interpolate between last and current positions
            drawContinuousLine(lastX, lastY, event.getX(), event.getY(), graphicsContext);
            lastX = event.getX();
            lastY = event.getY();
        });

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Painting App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void Draw(double x, double y, GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillOval(x - CIRCLE_RADIUS / 2, y - CIRCLE_RADIUS / 2, CIRCLE_RADIUS, CIRCLE_RADIUS); // Draw a
                                                                                                              // filled
                                                                                                              // circle
    }

    private void drawContinuousLine(double startX, double startY, double endX, double endY,
            GraphicsContext graphicsContext) {
        double distance = Math.hypot(endX - startX, endY - startY);
        int steps = (int) Math.ceil(distance / (CIRCLE_RADIUS - CIRCLE_OVERLAP)); // Calculate the number of overlapping
                                                                                  // circles

        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps; // Interpolate between start and end
            double x = startX + t * (endX - startX);
            double y = startY + t * (endY - startY);
            Draw(x, y, graphicsContext); // Draw circle at interpolated position
        }
    }
}
