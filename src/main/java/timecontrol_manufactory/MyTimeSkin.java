package timecontrol_manufactory;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;

class MyTimeSkin extends SkinBase<MyTimeControl> {
    // wird spaeter gebraucht
    private static final int ICON_SIZE  = 12;
    private static final int IMG_OFFSET = 4;

    //private static ImageView invalidIcon = new ImageView(new Image(MyTimeSkin.class.getResource("icons/invalid.png").toExternalForm(),
    //                                                               ICON_SIZE, ICON_SIZE,
    //                                                               true, false));

    //private static ImageView validIcon = new ImageView(new Image(MyTimeSkin.class.getResource("icons/valid.png").toExternalForm(),
    //                                                               ICON_SIZE, ICON_SIZE,
    //                                                               true, false));


    private TextField editableTime;
    private Label readonlyTime;
    private Label captionLabel;
    private Rectangle clockFace;
    private Rectangle clockHolder;
    private Popup popup;
    private Pane dropDownChooser;

    MyTimeSkin(MyTimeControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        setUpEventHandlers();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeSelf() {
        getSkinnable().loadFonts("/fonts/Lato/Lato-Reg.ttf", "/fonts/Lato/Lato-Lig.ttf", "/fonts/ds_digital/DS-DIGI.TTF");
        getSkinnable().addStylesheetFiles("style.css");
    }

    private void initializeParts() {
        editableTime = new TextField("To be replaced");
        editableTime.getStyleClass().add("editable-time");
        editableTime.setVisible(getSkinnable().isEditable());


        readonlyTime = new Label();
        readonlyTime.getStyleClass().add("readonly-time");
        readonlyTime.setVisible(!getSkinnable().isEditable());


        captionLabel = new Label("Neuer Alarm");
        captionLabel.getStyleClass().add("caption-label");

        clockFace = new Rectangle(170, 65);
        clockFace.getStyleClass().add("digit-background");

        clockHolder = new Rectangle(75, 6);
        clockHolder.setX(70);
        clockHolder.getStyleClass().add("clock-holder");

    }

    private void layoutParts() {
        VBox background = new VBox(clockFace, clockHolder);

        //position content in the center
        background.setAlignment(Pos.CENTER);
        background.setTranslateY(5);
        editableTime.setAlignment(Pos.CENTER);

        getChildren().addAll(new VBox(captionLabel, new StackPane( background, readonlyTime, editableTime)));
    }

    private void setUpEventHandlers() {
        //handle user input
        editableTime.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch(keyEvent.getCode()) {
                case ENTER:
                    getSkinnable().convert();
                    break;
                case UP:
                    getSkinnable().increaseHour();
                    keyEvent.consume();
                    break;
                case DOWN:
                    getSkinnable().decreaseHour();
                    keyEvent.consume();
                    break;
            }
        });
    }

    private void setupValueChangeListeners() {
        getSkinnable().editableProperty().addListener((observable, oldValue, newValue) -> {
            editableTime.setVisible(newValue);
            readonlyTime.setVisible(!newValue);
        });
    }

    private void setupBindings() {
        editableTime.textProperty().bindBidirectional(getSkinnable().userFacingStringProperty());
        readonlyTime.textProperty().bind(getSkinnable().actualTimeProperty().asString());
        captionLabel.textProperty().bind(getSkinnable().captionProperty());
    }
}
