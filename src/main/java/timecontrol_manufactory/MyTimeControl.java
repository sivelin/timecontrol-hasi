package timecontrol_manufactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

public class MyTimeControl extends Control {

    private static final PseudoClass MANDATORY_CLASS = PseudoClass.getPseudoClass("mandatory");
    private static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");
    private static final PseudoClass CONVERTIBLE_CLASS = PseudoClass.getPseudoClass("convertible");


    private static final String CONVERTIBLE_REGEX = "now|(\\d{1,2}[:]{0,1}\\d{0,2})";
    private static final String TIME_FORMAT_REGEX = "\\d{2}:\\d{2}";

    private static final String FORMATTED_TIME_PATTERN = "HH:mm";

    private static final Pattern CONVERTIBLE_PATTERN = Pattern.compile(CONVERTIBLE_REGEX);
    private static final Pattern TIME_FORMAT_PATTERN = Pattern.compile(TIME_FORMAT_REGEX);

    private final SkinType skinType;

    // all properties
    private final StringProperty userFacingString = new SimpleStringProperty();

    private final ObjectProperty<LocalTime> actualTime = new SimpleObjectProperty<>();
    private final StringProperty caption = new SimpleStringProperty();
    private final BooleanProperty editable = new SimpleBooleanProperty();
    private final BooleanProperty mandatory = new SimpleBooleanProperty(){
        @Override
        protected void invalidated() {
            super.invalidated();
            pseudoClassStateChanged(MANDATORY_CLASS, get());
        }
    };
    private final BooleanProperty invalid = new SimpleBooleanProperty(){
        @Override
        protected void invalidated() {
            super.invalidated();
            pseudoClassStateChanged(INVALID_CLASS, get());
        }
    };
    private final BooleanProperty convertible = new SimpleBooleanProperty(){
        @Override
        protected void invalidated() {
            super.invalidated();
            pseudoClassStateChanged(CONVERTIBLE_CLASS, get());
        }
    };

    public MyTimeControl(SkinType skinType) {
        this.skinType = skinType;

        initializeSelf();

        setupValueChangeListener();
    }

    private void setupValueChangeListener() {
        userFacingString.addListener((observableValue, oldValue, newValue) -> {
            if(newValue.equals("now")){ //Todo: Ergänzen und Code verschönern
                setConvertible(true);
            }else {
                setConvertible(false);
            }
            if(TIME_FORMAT_PATTERN.matcher(newValue).matches()){
                LocalTime newTime = LocalTime.parse(newValue);
                setActualTime(newTime);
                setInvalid(false);
            }else{
                setInvalid(true);
            }

        } );

        actualTime.addListener((observableValue, oldValue, newValue) -> {
            String newTime = newValue.format(DateTimeFormatter.ofPattern(FORMATTED_TIME_PATTERN, Locale.GERMAN));
            setUserFacingString(newTime);
        } );
    }

    private void initializeSelf() {
        getStyleClass().add("my-time-control");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return skinType.getFactory().apply(this);
    }


    public void loadFonts(String... font){
        for(String f : font){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    public void addStylesheetFiles(String... stylesheetFile){
        for(String file : stylesheetFile){
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    public void convert() {
       if(!isConvertible()) return;
        if(getUserFacingString().equals("now")){
            setActualTime(LocalTime.now());
        }
    }

    public void increaseHour() {
            setActualTime(getActualTime().plusHours(1));
    }

    public void decreaseHour() {
        setActualTime(getActualTime().minusHours(1));
    }


    // 2 generate getter and setter

    public boolean isConvertible() {
        return convertible.get();
    }

    public BooleanProperty convertibleProperty() {
        return convertible;
    }

    public void setConvertible(boolean convertible) {
        this.convertible.set(convertible);
    }

    public boolean isInvalid() {
        return invalid.get();
    }

    public BooleanProperty invalidProperty() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid.set(invalid);
    }

    public String getUserFacingString() {
        return userFacingString.get();
    }

    public StringProperty userFacingStringProperty() {
        return userFacingString;
    }

    public void setUserFacingString(String userFacingString) {
        this.userFacingString.set(userFacingString);
    }

    public boolean isMandatory() {
        return mandatory.get();
    }

    public BooleanProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory.set(mandatory);
    }

    public LocalTime getActualTime() {
        return actualTime.get();
    }

    public ObjectProperty<LocalTime> actualTimeProperty() {
        return actualTime;
    }

    public void setActualTime(LocalTime actualTime) {
        this.actualTime.set(actualTime);
    }

    public String getCaption() {
        return caption.get();
    }

    public StringProperty captionProperty() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public boolean isEditable() {
        return editable.get();
    }

    public BooleanProperty editableProperty() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }
}
