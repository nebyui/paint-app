import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
<<<<<<< HEAD
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
=======
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ColorPicker;
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
    private double lastX; // last x cordinate mouse was at
    private double lastY; // last y cordinate mouse was at
    private double brushSize = 10;
    private Color currentColor = Color.BLACK; // default color of brush is black
    private double canvasScale = 0.5; // default size scale of canvas, used for zooming
    private double translateX = 0; // x value for panning the canvas
    private double translateY = 0; // y value for panning the canvas
    private double mousePressX; // original x cordinate of mouse when panning
    private double mousePressY; // original y cordinate of mouse when panning
>>>>>>> d26d73e2960020c358e17b254963961a3c35f1d2

    public static void main(String[] args) throws Exception {
        launch(args); // starts the app, this format is necessary for JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
<<<<<<< HEAD
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
=======
        BuildApp(primaryStage); // Builds the app and GUI
    }

    public void BuildApp(Stage primaryStage) {
        primaryStage.setTitle("Painting App");
        // primaryStage.setFullScreen(true);

        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // gets computer screen size

        BorderPane borderPane = new BorderPane(); // main container for application, aligns elements in window
        borderPane.setStyle("-fx-background-color: black;");

        // sets default size of window
        Scene scene = new Scene(borderPane, screenBounds.getWidth() - 500, screenBounds.getHeight() - 250);
        primaryStage.setScene(scene); // scene contains the visual elements (UI, canvas, etc)
        primaryStage.show();

        Canvas canvas = new Canvas(1500, 1000); // resolution of Canvas
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D(); // contains the painting contents
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // fills canvas with white

        Group canvasGroup = new Group(canvas); // group allows the canvas to be scaled and translated
        canvasGroup.setScaleX(canvasScale);
        canvasGroup.setScaleY(canvasScale);

        StackPane canvasContainer = new StackPane(canvasGroup); // container that centers the canvas
        StackPane.setAlignment(canvasGroup, Pos.CENTER);
        borderPane.setCenter(canvasContainer);

        HBox toolbar = new HBox(10); // container for toolbar
        toolbar.setAlignment(Pos.CENTER);
        borderPane.setTop(toolbar);

        Slider brushSizeSlider = new Slider(1, 100, brushSize); // creates brush size slider
        brushSizeSlider.setShowTickMarks(true);
        brushSizeSlider.setMajorTickUnit(10);
        brushSizeSlider.setMinorTickCount(10);
        brushSizeSlider.setPrefWidth(500);
        brushSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            brushSize = newValue.doubleValue();
        });

        ToggleButton eraserButton = new ToggleButton(); // creates eraser button
        Image eraserIcon = new Image("icon/eraser_icon.png"); // loads icon for eraser button
        ImageView eraserImageView = new ImageView(eraserIcon);
        eraserImageView.setFitWidth(24);
        eraserImageView.setFitHeight(24);
        eraserButton.setGraphic(eraserImageView);

        ColorPicker colorPicker = new ColorPicker(currentColor); // creates color picker widget
        colorPicker.setOnAction(event -> {
            if (!eraserButton.isSelected()) {
                currentColor = colorPicker.getValue();
            }
        });

        Button saveButton = new Button("Save"); // creates save button
        saveButton.setOnAction(event -> saveDrawing(canvas));

        Button loadButton = new Button("Load"); // creates load button
        loadButton.setOnAction(event -> loadDrawing(canvas));

        // adds each button and widget to the toolbar
        toolbar.getChildren().add(0, brushSizeSlider);
        toolbar.getChildren().add(1, colorPicker);
        toolbar.getChildren().add(2, eraserButton);
        toolbar.getChildren().add(3, saveButton);
        toolbar.getChildren().add(4, loadButton);

        eraserButton.setOnAction(event -> { // handles actions for eraser button
            if (eraserButton.isSelected()) {
                currentColor = Color.WHITE; // for now, the eraser simply sets the brush color to white
                eraserButton.setStyle("-fx-background-color: rgb(120, 150, 200);"); // overlay for when eraser is on
            } else {
                currentColor = colorPicker.getValue();
                eraserButton.setStyle("-fx-background-color: lightgray;");
            }

        });

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> { // draws point when mouse button is pressed
            if (!event.isShiftDown()) { // the shift key is used for panning, so check if its pressed
                lastX = event.getX();
                lastY = event.getY();
                DrawBrush(event.getX(), event.getY(), graphicsContext);
            }
        });

        // draws line when mouse button is held and dragged
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (!event.isShiftDown()) { // the shift key is used for panning, so check if its pressed
                DrawLine(lastX, lastY, event.getX(), event.getY(), graphicsContext);
                lastX = event.getX();
                lastY = event.getY();
            }
        });

        // for panning, retrieves the x and y cordinate of mouse
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isShiftDown()) {
                mousePressX = event.getSceneX();
                mousePressY = event.getSceneY();
            }
        });

        // pans canvas when shift is press and mouse is dragged
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {

            if (event.isShiftDown()) {
                double differenceX = event.getSceneX() - mousePressX; // Calculates change in x
                double differenceY = event.getSceneY() - mousePressY; // Calculates change in y

                translateX += differenceX;
                translateY += differenceY;

                canvasGroup.setTranslateX(translateX); // moves canvas in x axis
                canvasGroup.setTranslateY(translateY); // moves canvas in y axis

                mousePressX = event.getSceneX();
                mousePressY = event.getSceneY();
            }
        });

        // Handles zooming in and out
        scene.addEventFilter(ScrollEvent.SCROLL, event -> {
            double scrollValue = event.getDeltaY(); // gets direction mouse wheel is scrolled
            if (scrollValue > 0) {
                canvasScale *= 1.1;
            } else {
                canvasScale /= 1.1;
            }

            canvasGroup.setScaleX(canvasScale); // Applies the scale change
            canvasGroup.setScaleY(canvasScale);
        });

    }

    // Brush Tip
    private void DrawBrush(double x, double y, GraphicsContext graphicsContext) {
        graphicsContext.setFill(currentColor);
        graphicsContext.fillOval(x - (brushSize / 2), y - (brushSize / 2), brushSize, brushSize);
        // paints circle on canvas. ^ ensures mouse is at center of circle ^ width and
        // height of circle
    }

    // Stroke rendering
    private void DrawLine(double startX, double startY, double endX, double endY, GraphicsContext graphicsContext) {

        // calculates distance between two mouse points between polling rate, then draws
        // line between points
        double distance = Math.ceil(Math.hypot(endX - startX, endY - startY));
        for (int i = 0; i <= distance; i++) {
            double interpolatedPosition = (double) i / distance;
            double x = startX + interpolatedPosition * (endX - startX);
            double y = startY + interpolatedPosition * (endY - startY);
            DrawBrush(x, y, graphicsContext);
        }
    }

    private void saveDrawing(Canvas canvas) { // saves drawing as png
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);

        File directory = new File("src/drawings");
        if (!directory.exists()) {// checks if folder exists, if not, it makes one
            directory.mkdirs();
        }

        File file = new File(directory, "drawing.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file); // compiles image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDrawing(Canvas canvas) { // leads drawing from png
        File file = new File("src/drawings/drawing.png");
        if (file.exists()) {
            try {
                Image image = new Image(file.toURI().toString());
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D(); // gets painting contents of canvas
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // clears that contents
                graphicsContext.drawImage(image, 0, 0); // replaces it with contents of image
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no drawing found");
            ;
        }
    }
}
>>>>>>> d26d73e2960020c358e17b254963961a3c35f1d2
