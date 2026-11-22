package org.example.demo1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class BMIController {

    @FXML
    private Label lblWeight;

    @FXML
    private Label lblHeight;

    @FXML
    private TextField tfWeight;

    @FXML
    private TextField tfHeight;

    @FXML
    private Button btnCalculate;

    @FXML
    private Label lblResult;

    @FXML
    private Label lblLocalTime;

    @FXML
    private VBox rootVBox;

    private final BMICalculator calculator = new BMICalculator();

    private Locale currentLocale = Locale.of("en", "US");
    private Map<String, String> localizedStrings;

    public void initialize() {
        setLanguage(Locale.of("en", "US"));
    }

    private void setLanguage(Locale locale) {
        currentLocale = locale;

        lblResult.setText("");
        localizedStrings = LocalizationService.getLocalizedStrings(locale);

        lblWeight.setText(localizedStrings.getOrDefault("weight", "Weight"));
        lblHeight.setText(localizedStrings.getOrDefault("height", "Height"));
        btnCalculate.setText(localizedStrings.getOrDefault("calculate", "Calculate"));

        displayLocalTime(locale);
        applyTextDirection(locale);
    }

    public void onCalculateClick(ActionEvent actionEvent) {
        try {
            double weight = Double.parseDouble(tfWeight.getText());
            double heightCm = Double.parseDouble(tfHeight.getText());

            double bmi = calculator.calculate(weight, heightCm);

            lblResult.setText(localizedStrings.getOrDefault("result", "Your BMI is")
                    + " " + calculator.format(bmi));

            BMIResultService.saveResult(weight, heightCm / 100.0, bmi,
                    currentLocale.getLanguage());
        } catch (IllegalArgumentException e) {
            lblResult.setText(localizedStrings.getOrDefault("invalid", "Invalid input"));
        }
    }

    public void onENClick(ActionEvent actionEvent) {
        setLanguage(Locale.of("en", "US"));
    }

    public void onFRClick(ActionEvent actionEvent) {
        setLanguage(Locale.of("fr", "FR"));
    }

    public void onURClick(ActionEvent actionEvent) {
        setLanguage(Locale.of("ur", "PA"));
    }

    public void onVIClick(ActionEvent actionEvent) {
        setLanguage(Locale.of("vi", "VI"));
    }

    public void onFAClick(ActionEvent actionEvent) {
        setLanguage(Locale.of("fa", "IR"));
    }

    private void displayLocalTime(Locale locale) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss", locale);
        String formattedTime = currentTime.format(formatter);
        lblLocalTime.setText(localizedStrings.getOrDefault("localTime", "Local Time")
                + " " + formattedTime);
    }

    private void applyTextDirection(Locale locale) {
        String lang = locale.getLanguage();
        boolean isRTL = lang.equals("fa") || lang.equals("ur")
                || lang.equals("ar") || lang.equals("he");

        Platform.runLater(() -> {
            if (rootVBox != null) {
                rootVBox.setNodeOrientation(isRTL
                        ? NodeOrientation.RIGHT_TO_LEFT
                        : NodeOrientation.LEFT_TO_RIGHT);
            }
            tfWeight.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
            tfHeight.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
        });
    }
}