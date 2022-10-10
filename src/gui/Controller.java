package gui;

import rules.*;
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
import network.Network;


public class Controller {

    @FXML
    private MenuBar menuBar;


    @FXML
    private TextField netDescTxtFld;

    @FXML
    private Button applyBtn;


    @FXML
    private ChoiceBox<String> learningRuleChbx;


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

    private Rule rule;
    private Network net;

    @FXML
    public void initialize(){

        createMenu();

        cvcontext = cv.getGraphicsContext2D();
        transform = new double[6];
        transform[0] = transform[3] = 0.5;
        transform[1] = transform[2] = transform[4] = transform[5] = 0;
        updateTransform();



        applyBtn.disableProperty().bind(learningRuleChbx.valueProperty().isNull().or(netDescTxtFld.textProperty().isEmpty()));
        applyBtn.setOnAction(event -> {
            String[] netDesc = netDescTxtFld.getText().trim().split(" ");
            int[] neuronsDesc = new int[netDesc.length];
            for(int i = 0; i < netDesc.length; i++){
                neuronsDesc[i] = Integer.parseInt(netDesc[i]);
            }

            switch (learningRuleChbx.getValue()){

                case "HebbRule": rule = new HebbRule(neuronsDesc); break;
                case "DeltaRule": rule = new DeltaRule(neuronsDesc); break;
                case "BackPropagation": rule = new Backpropagation(neuronsDesc); break;
                case "RBFNRule":  rule = new RBFNRule(neuronsDesc); break;

            }

            net = rule.getNetwork();
            clearRect40K();
            net.draw(cvcontext);

        });


        ObservableList<String> rules = FXCollections.observableArrayList();
        rules.addAll(
                "HebbRule",
                "DeltaRule",
                "BackPropagation",
                "RBFNRule"
        );
        learningRuleChbx.getItems().addAll(rules);


        ObservableList<String> datasets = FXCollections.observableArrayList();
        datasets.addAll(
                "/And.txt",
                "/AndZ.txt",
                "/Or.txt",
                "/OrZ.txt",
                "/XOR.txt",
                "/XORZ.txt",
                "/XNOR.txt",
                "/XNORZ.txt"
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
            net.train(datasetChbx.getValue());
            clearRect40K(transform[4],transform[5]);
            net.draw(cvcontext);
        });

        executeBtn.setOnAction(event -> {
            net.simulate(dataInputTxtFld.getText().trim().split(" "));
            clearRect40K(transform[4],transform[5]);
            net.draw(cvcontext);
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

        if(net != null)
        net.draw(cvcontext);

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
