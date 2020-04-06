package timecontrol_manufactory;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.TimeStringConverter;

// default layout von skinbase wie stickpane
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


    // 4
    private TextField editableTime;
    // auf labenl kann text und icon sein, text kann nicht mit icon gemacht werden
    private Label captionLabel;

    MyTimeSkin(MyTimeControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeSelf() {
        getSkinnable().loadFonts("/fonts/Lato/Lato-Reg.ttf", "/fonts/Lato/Lato-Lig.ttf");
        getSkinnable().addStylesheetFiles("style.css");
    }

    private void initializeParts() {
        editableTime = new TextField("To be replaced");
        editableTime.getStyleClass().add("editable-time");

        captionLabel = new Label("Neuer Alarm");
        captionLabel.getStyleClass().add("caption-label");
    }

    private void layoutParts() {
        getChildren().addAll(new VBox(captionLabel,editableTime));
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {

        // todo forgiving format
        // getSkinnable wichtig, new LocalTimeStringConverter, damits mit textProperty funktioniert
        editableTime.textProperty().bindBidirectional(getSkinnable().actualTimeProperty(), new LocalTimeStringConverter());

        captionLabel.textProperty().bind(getSkinnable().captionProperty());
    }
}
