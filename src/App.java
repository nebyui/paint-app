import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.stage.Screen;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class App extends Application {
    private double lastX;
    private double lastY;
    private double brushSize = 10;
    private Color currentColor = Color.BLACK;
    private double canvasScale = 0.5;
    private double translateX = 0;
    private double translateY = 0;
    private double mousePressX;
    private double mousePressY;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BuildApp(primaryStage);

    }

    public void BuildApp(Stage primaryStage) {
        primaryStage.setTitle("Painting App");
        primaryStage.setFullScreen(true);

        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(borderPane, screenBounds.getWidth() - 500, screenBounds.getHeight() - 250);
        primaryStage.setScene(scene);
        primaryStage.show();

        Canvas canvas = new Canvas(1500, 1000);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Group canvasGroup = new Group(canvas);
        canvasGroup.setScaleX(canvasScale);
        canvasGroup.setScaleY(canvasScale);

        StackPane canvasContainer = new StackPane(canvasGroup);
        StackPane.setAlignment(canvasGroup, Pos.CENTER);
        borderPane.setCenter(canvasContainer);

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER);
        borderPane.setTop(toolbar);

        Slider brushSizeSlider = new Slider(1, 100, brushSize);
        brushSizeSlider.setShowTickMarks(true);
        // brushSizeSlider.setShowTickLabels(true); Add numbers along slider
        brushSizeSlider.setMajorTickUnit(10);
        brushSizeSlider.setMinorTickCount(1);
        brushSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            brushSize = newValue.doubleValue();
        });

        ToggleButton eraserButton = new ToggleButton();
        Image eraserIcon = new Image("icon/eraser_icon.png");
        ImageView eraserImageView = new ImageView(eraserIcon);
        eraserImageView.setFitWidth(24);
        eraserImageView.setFitHeight(24);
        eraserButton.setGraphic(eraserImageView);

        ColorPicker colorPicker = new ColorPicker(currentColor);
        colorPicker.setOnAction(event -> {
            if (!eraserButton.isSelected()) {
                currentColor = colorPicker.getValue();
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveDrawing(canvas));

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> loadDrawing(canvas));

        toolbar.getChildren().add(0, brushSizeSlider);
        toolbar.getChildren().add(1, colorPicker);
        toolbar.getChildren().add(2, eraserButton);
        toolbar.getChildren().add(3, saveButton);
        toolbar.getChildren().add(4, loadButton);

        eraserButton.setOnAction(event -> {
            if (eraserButton.isSelected()) {
                currentColor = Color.WHITE;
                eraserButton.setStyle("-fx-background-color: rgb(120, 150, 200);");
            } else {
                currentColor = colorPicker.getValue();
                eraserButton.setStyle("-fx-background-color: lightgray;");
            }

        });

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (!event.isShiftDown()) {
                lastX = event.getX();
                lastY = event.getY();
                DrawBrush(event.getX(), event.getY(), graphicsContext);
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (!event.isShiftDown()) {
                DrawLine(lastX, lastY, event.getX(), event.getY(), graphicsContext);
                lastX = event.getX();
                lastY = event.getY();
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isShiftDown()) {
                mousePressX = event.getSceneX();
                mousePressY = event.getSceneY();
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isShiftDown()) {
                double deltaX = event.getSceneX() - mousePressX;
                double deltaY = event.getScreenY() - mousePressY;

                translateX += deltaX;
                translateY += deltaY;

                canvasGroup.setTranslateX(translateX);
                canvasGroup.setTranslateY(translateY);

                mousePressX = event.getSceneX();
                mousePressY = event.getSceneY();
            }
        });

        scene.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY();
            if (deltaY > 0) {
                canvasScale *= 1.1;
            } else {
                canvasScale /= 1.1;
            }

            canvasGroup.setScaleX(canvasScale);
            canvasGroup.setScaleY(canvasScale);
        });

    }

    // Brush Tip
    private void DrawBrush(double x, double y, GraphicsContext graphicsContext) {
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
            DrawBrush(x, y, graphicsContext);
        }
    }

    private void saveDrawing(Canvas canvas) {
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);

        File directory = new File("src/drawings");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, "drawing.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDrawing(Canvas canvas) {
        File file = new File("src/drawings/drawing.png");
        if (file.exists()) {
            try {
                Image image = new Image(file.toURI().toString());
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                graphicsContext.drawImage(image, 0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no drawing found");
            ;
        }
    }
}