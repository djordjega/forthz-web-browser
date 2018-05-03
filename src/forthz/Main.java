package forthz;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Djordje Gavrilovic 2017
 */
public class Main extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {

        TextField tf = new TextField();  // url field
        ChoiceBox historyBox = new ChoiceBox();  // history list 

        /* 
         * SETTING THE ENGINE
         */
        WebView browser = new WebView();
        browser.prefHeightProperty().bind(primaryStage.heightProperty());  // binding to
        browser.prefWidthProperty().bind(primaryStage.widthProperty());  // fit the stage size

        WebEngine engine = browser.getEngine();
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                tf.setText(engine.getLocation());
                historyBox.getItems().add(engine.getLocation());
            }
        });

        /*
         * HOME PAGE // if ever to be needed
         */
        String homeUrl = "http://google.com/";
        engine.load(homeUrl);

        /*
         * URL BAR
         */
        tf.setPrefColumnCount(100);
        tf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String url = "https://" + tf.getText();
                engine.load(url);
            }
        });
        // refresh button
        Image reImg = new Image("icons/replay.png");
        ImageView reIV = new ImageView(reImg);
        reIV.setFitWidth(20);  // resizing
        reIV.setPreserveRatio(true);
        reIV.setSmooth(true);
        reIV.setCache(true);  // cache to improve performance(?)
        Button reBtn = new Button("", reIV);
        reBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                engine.load(browser.getEngine().getLocation());
            }
        });

        /*
         * SEARCH BUTTON / DIALOG
         */
        Image srImg = new Image("icons/search.png");
        ImageView srIV = new ImageView(srImg);
        srIV.setFitWidth(20);
        srIV.setPreserveRatio(true);
        srIV.setSmooth(true);
        srIV.setCache(true);
        Button srBtn = new Button("", srIV);
        srBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.getDialogPane().getScene().getStylesheets().add("css/MainStyle.css");
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setTitle("Forthz doing Search");
                dialog.setHeaderText("Forthz doing search");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    String[] res = result.get().split(" ");
                    StringBuilder query = new StringBuilder();
                    for (String q : res) {
                        query.append(q);
                        query.append("+");
                    }
                    String zx = query.substring(0, query.length() - 1);
                    engine.load("http://www.google.com/search?q=" + zx);
                }
            }
        });

        /*
         * HISTORY
         */
        historyBox.setId("historyBox");
        historyBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue observable, Object oldValue, Object newValue)
                -> {
            engine.load(newValue.toString());
        });

        /*
         * HOME BUTTON
         */
        Image homeImg = new Image("icons/homeicon2.png");
        ImageView homeIV = new ImageView(homeImg);
        homeIV.setFitWidth(20);
        homeIV.setPreserveRatio(true);
        homeIV.setSmooth(true);
        homeIV.setCache(true);
        Button homeBtn = new Button("", homeIV);
        homeBtn.setId("homeBtn");
        homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                engine.load(homeUrl);
            }
        });

        /*
         * SYSTEM BUTTONS
         */
        // close button
        Image closeImg = new Image("icons/close.png");
        ImageView closeIV = new ImageView(closeImg);
        closeIV.setFitWidth(17);
        closeIV.setPreserveRatio(true);
        closeIV.setSmooth(true);
        closeIV.setCache(true);
        Button closeBtn = new Button("", closeIV);
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        // max button
        Image maxImg = new Image("icons/max.png");
        ImageView maxIV = new ImageView(maxImg);
        maxIV.setFitWidth(17);
        maxIV.setPreserveRatio(true);
        maxIV.setSmooth(true);
        maxIV.setCache(true);
        Button maxBtn = new Button("", maxIV);
        maxBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // primaryStage.setMaximized(true); // not working on Ubuntu/Mint
                ObservableList<Screen> screens = Screen.getScreensForRectangle(new Rectangle2D(
                        primaryStage.getX(), primaryStage.getY(), primaryStage.getWidth(), primaryStage.getHeight()));
                Rectangle2D bounds = screens.get(0).getVisualBounds();
                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setWidth(bounds.getWidth());
                primaryStage.setHeight(bounds.getHeight());
            }
        });
        // min button
        Image minImg = new Image("icons/min.png");
        ImageView minIV = new ImageView(minImg);
        minIV.setFitWidth(17);
        minIV.setPreserveRatio(true);
        minIV.setSmooth(true);
        minIV.setCache(true);
        Button minBtn = new Button("", minIV);
        minBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setIconified(true);
            }
        });
        // Forthz doing site labels
        Label forthzL = new Label("Forthz");
        forthzL.setId("titId");

        Label dots = new Label("doing");
        dots.setId("dots");

        Label siteL = new Label();
        siteL.setId("siteLId");
        siteL.textProperty().bind(browser.getEngine().titleProperty());

        AnchorPane st = new AnchorPane(forthzL, dots, siteL, minBtn, maxBtn, closeBtn);
        st.setLeftAnchor(forthzL, 10.0);
        st.setTopAnchor(forthzL, 5.0);
        st.setLeftAnchor(dots, 60.0);
        st.setTopAnchor(dots, 6.0);
        st.setLeftAnchor(siteL, 95.0);
        st.setTopAnchor(siteL, 6.0);
        st.setRightAnchor(closeBtn, 5.0);
        st.setRightAnchor(maxBtn, closeBtn.getWidth() + 35.0);
        st.setRightAnchor(minBtn, 65.0);       

        /*
         * LAYOUT
         */       
        Label l1 = new Label(" "); // silly quick hacks
        Label l2 = new Label(); // wish I used FXML
        
        HBox tab = new HBox(5, homeBtn, tf, reBtn, srBtn, historyBox, l1);
        tab.setAlignment(Pos.BASELINE_LEFT);
        tab.setBackground(Background.EMPTY);

        VBox root = new VBox(st, tab, l2, browser);
        root.setBackground(Background.EMPTY);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add("css/MainStyle.css");

        Image img2 = new Image("icons/appicon.png");

        primaryStage.getIcons().add(img2);
        primaryStage.setScene(scene);
        primaryStage.titleProperty().bind(browser.getEngine().titleProperty());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }

}
