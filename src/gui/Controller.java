package gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import networks.Network;
import networks.hebbnet.DeltaNet;
import networks.hebbnet.HebbNet;

import java.io.File;
import java.net.URL;
import java.util.Arrays;


public class Controller {

    @FXML
    private MenuBar menuBar;


    @FXML
    private ChoiceBox<Network> learningRuleChbx;


    @FXML
    private ChoiceBox<String> datasetChbx;

    @FXML
    private Button trainBtn;


    @FXML
    private TextField dataInputTxtFld;

    @FXML
    private Button executeBtn;


    @FXML
    private AnchorPane cvRoot;
    @FXML
    private Canvas cv;
    private GraphicsContext cvcontext;
    private double[] transform;
    private double dragScreenX, dragScreenY;


    @FXML
    public void initialize(){

        createMenu();

        cvcontext = cv.getGraphicsContext2D();
        transform = new double[6];
        transform[0] = transform[3] = 0.5;
        transform[1] = transform[2] = transform[4] = transform[5] = 0;
        updateTransform();


        ObservableList<Network> networks = FXCollections.observableArrayList();
        networks.addAll(
                new HebbNet(new int[]{2,1}),
                new DeltaNet(new int[]{2,1})

        );
        learningRuleChbx.setConverter(new StringConverter<>() {
            @Override
            public String toString(Network network) {
                return network.toString();
            }

            @Override
            public Network fromString(String s) {
                return null;
            }
        });
        learningRuleChbx.getItems().addAll(networks);
        learningRuleChbx.setOnAction(event -> learningRuleChbx.getValue().draw(cvcontext));


        ObservableList<String> datasets = FXCollections.observableArrayList();
        datasets.addAll(
                "/And.txt",
                "/Or.txt",
                "/XOR.txt"
        );
        datasetChbx.setConverter(new StringConverter<>() {
            @Override
            public String toString(String s) {
                return s.replace("/", "").replace(".txt", "");
            }

            @Override
            public String fromString(String s) {
                return null;
            }
        });
        datasetChbx.getItems().addAll(datasets);
        datasetChbx.setValue(datasets.get(0));

        trainBtn.setOnAction(event -> {
            learningRuleChbx.getValue().train(datasetChbx.getValue());
            clearRect40K(transform[4],transform[5]);
            learningRuleChbx.getValue().draw(cvcontext);
        });

        executeBtn.setOnAction(event -> {
            learningRuleChbx.getValue().simulate(dataInputTxtFld.getText());
            clearRect40K(transform[4],transform[5]);
            learningRuleChbx.getValue().draw(cvcontext);
        });



        cvRoot.widthProperty().addListener((observable, oldValue, newValue) ->{
            setCanvasSize();
            clearRect40K(transform[4],transform[5]);
            updateTransform();
        });
        cvRoot.heightProperty().addListener((observable, oldValue, newValue) ->{
            setCanvasSize();
            clearRect40K(transform[4],transform[5]);
            updateTransform();
        });

        cv.setOnMousePressed(event -> {
            dragScreenX = event.getX();
            dragScreenY = event.getY();
        });

        cv.setOnMouseDragged(event -> {

            double dx = event.getX() - dragScreenX;
            double dy = event.getY() - dragScreenY;
            if (dx == 0 && dy == 0) {
                return;
            }
            clearRect40K(transform[4],transform[5]);

            transform[4] += dx;
            transform[5] += dy;
            dragScreenX = event.getX();
            dragScreenY = event.getY();

            updateTransform();

        });

        cv.setOnScroll(event -> {
            clearRect40K();
            zoom(event.getDeltaY());
            updateTransform();
        });

        setCanvasSize();
        clearRect40K();

    }

    public void createMenu(){

        Menu file = new Menu();
        file.setText("File");

        MenuItem closeItem = new MenuItem();
        closeItem.setText("Close");
        closeItem.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        file.getItems().addAll(
                closeItem
        );


        Menu help = new Menu();
        help.setText("Help");

        MenuItem aboutItem = new MenuItem();
        aboutItem.setText("About");
        aboutItem.setOnAction(event -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neural Network Course");
            alert.setHeaderText("Developers:");
            alert.setContentText("Main programmers are:" +
                    "\n" +
                    "Pe3aTeJlb" +
                    "\n" +
                    "TexHoMa|\u203E" +
                    "\n" +
                    "Kolhozniy punk");

            alert.showAndWait();

        });

        help.getItems().addAll(
                aboutItem
        );

        menuBar.getMenus().addAll(
                file,
                help
        );

    }


    private void updateTransform(){

        cvcontext.setTransform(transform[0], transform[1], transform[2],
                transform[3], transform[4], transform[5]
        );

        if(learningRuleChbx.getValue() != null)
        learningRuleChbx.getValue().draw(cvcontext);

    }

    private void setCanvasSize(){

        //костыль для обхода бесконечного fps
        //Почему-то, если высота холста 0, то fps улетает в космос и греет проц,
        //а буфер canvas начинает забивать память
        cv.setWidth(cvRoot.getWidth());
        if(cvRoot.getHeight() > 0) {
            cv.setHeight(cvRoot.getHeight());
        }

    }

    private void clearRect40K() {
        cvcontext.setFill(Color.WHITESMOKE);
        cvcontext.fillRect(0, 0, (cv.getWidth() / transform[0]) * 2, (cv.getHeight() / transform[0]) * 2);
    }

    private void clearRect40K(double prevX, double prevY) {
        cvcontext.setFill(Color.WHITESMOKE);
        cvcontext.fillRect(-prevX/transform[0],-prevY/transform[0],cv.getWidth()/transform[0],cv.getHeight()/transform[0]);
    }

    // convert screen coordinates to grid coordinates by inverting circuit transform
    private int inverseTransformX(double x) {
        return (int) ((x-transform[4])/transform[0]);
    }

    private int inverseTransformY(double y) {
        return (int) ((y-transform[5])/transform[3]);
    }

    private void zoom(double dy) {

        double newScale;
        double oldScale = transform[0];
        double val = dy*.005;
        newScale = Math.max(oldScale+val, .2);
        newScale = Math.min(newScale, 2.5);

        int cx = inverseTransformX(cv.getWidth() / 2);
        int cy = inverseTransformY(cv.getHeight() / 2);

        transform[0] = newScale;
        transform[3] = newScale;

        // adjust translation to keep center of screen constant
        // inverse transform = (x-t4)/t0

        transform[4] = cv.getWidth() / 2 - cx * newScale;
        transform[5] = cv.getHeight() / 2 - cy * newScale;


    }

}
