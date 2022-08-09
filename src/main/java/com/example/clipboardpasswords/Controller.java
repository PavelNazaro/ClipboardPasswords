package com.example.clipboardpasswords;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Controller {
    private static final String JSON_FILE = "passwords.json";
    public Button buttonJira;
    public Button buttonAem;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passField;

    private Stage stage;
    private Clipboard clipboard;
    private String jiraLoginField = "";
    private String jiraPassField = "";
    private String aemLoginField = "";
    private String aemPassField = "";

    public void setStage(Stage stage){
        this.stage = stage;

        init();
    }

    private void init() {
        getStringsFromJsonFile();

        buttonAem.fire();
        buttonAem.requestFocus();

        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    private void getStringsFromJsonFile() {
        try(InputStream is = getClass().getResourceAsStream(JSON_FILE)){
            if (is != null) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse(
                        new InputStreamReader(is, StandardCharsets.UTF_8));
                System.out.println(jsonObject);

                JSONArray jsonObject1 = (JSONArray) jsonObject.get("Jira");
                JSONObject jsonObject2 = (JSONObject) jsonObject1.get(0);

                jiraLoginField = jsonObject2.get("login").toString();
                jiraPassField = jsonObject2.get("pass").toString();

                jsonObject1 = (JSONArray) jsonObject.get("Aem");
                jsonObject2 = (JSONObject) jsonObject1.get(0);

                aemLoginField = jsonObject2.get("login").toString();
                aemPassField = jsonObject2.get("pass").toString();
            } else {
                showAlert("Url to file NULL!");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
//            System.out.println(jsonObject);
//
//            for (Object j : jsonObject.keySet()){
//                System.out.println(j);
//                JSONArray jsonObject1 = (JSONArray) jsonObject.get(j);
//                Iterator<JSONObject> iterator = jsonObject1.iterator();
//                while (iterator.hasNext()) {
//                    JSONObject jsonObject2 = iterator.next();
//
//                    System.out.println("login: " + jsonObject2.get("login"));
//                    System.out.println("pass: " + jsonObject2.get("pass"));
//                }
//            }

    }

    @FXML
    protected void buttonCopyPressed() {
        setAlwaysOnTop(stage, true);

        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String oldClipboardValue = getClipboardValue(clipboard);

        String login = loginField.getText();
        String pass = passField.getText();

        setClipboardValue(login);
        delay(2000);

        setClipboardValue(pass);

        if (oldClipboardValue != null) {
            delay(1000);
            setClipboardValue(oldClipboardValue);
        }

        setAlwaysOnTop(stage, false);
    }

    private String getClipboardValue(Clipboard clipboard){
        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    private void setAlwaysOnTop(Stage stage, boolean b) {
        stage.setAlwaysOnTop(b);
    }

    private void setClipboardValue(String text) {
        clipboard.setContents(new StringSelection(text), null);
    }

    private void delay(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String mes) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setContentText(mes);
        alert.showAndWait();
    }

    public void pressJiraButton(ActionEvent actionEvent) {
        loginField.setText(jiraLoginField);
        passField.setText(jiraPassField);
    }

    public void pressAemButton(ActionEvent actionEvent) {
        loginField.setText(aemLoginField);
        passField.setText(aemPassField);
    }
}