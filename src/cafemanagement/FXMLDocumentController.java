/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package cafemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author KV_II
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private TextField si_username;

    @FXML
    private PasswordField si_password;

    @FXML
    private Button si_loginBtn;

    @FXML
    private Hyperlink si_forgotPass;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    @FXML
    private PasswordField su_password;

    @FXML
    private ComboBox<?> su_question;

    @FXML
    private TextField su_answer;

    @FXML
    private Button su_signupBtn;

    @FXML
    private TextField fp_username;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private Button fp_proceedBtn;

    @FXML
    private ComboBox<?> fp_question;

    @FXML
    private TextField fp_answer;

    @FXML
    private Button fp_back;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField np_newPassword;

    @FXML
    private PasswordField np_confirmPassword;

    @FXML
    private Button np_changePassBtn;

    @FXML
    private Button np_back;

    @FXML
    private AnchorPane side_form;

    @FXML
    private Button side_CreateBtn;

    @FXML
    private Button side_alreadyHave;

    private Connection connect;

    private PreparedStatement prepare;

    private ResultSet result;

    private Alert alert;
    
    public void loginBtn() {
        if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Fill all the empty Fields");
            alert.showAndWait();
        } else {
            String selectData = "SELECT username,password FROM employee where username = ? AND password = ?";
            connect = database.connectDB();
            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, si_username.getText().toString());
                prepare.setString(2, si_password.getText().toString());
                result = prepare.executeQuery();
                if (result.next()) {
                    data.username=si_username.getText().toString();
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Login is Successfull");
                    alert.showAndWait();
                    Parent root=FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                    Stage stage=new Stage();
                    Scene scene=new Scene(root);
                    stage.setTitle("Cafe Management System");
                    stage.setMinWidth(1100);
                    stage.setMaxHeight(600);
                    
                    stage.setScene(scene);
                    stage.show();
                    si_loginBtn.getScene().getWindow().hide();
                    
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Username/Password, try again or click Forgot password");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void switchForgotPass() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);
        forgotPassQuestionList();

    }

    public void regBtn() {
        if (su_username.getText().isEmpty() || su_password.getText().isEmpty()
                || su_question.getSelectionModel().getSelectedItem() == null
                || su_answer.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText(null);
            alert.setContentText("Fill all the empty Fields");
            alert.showAndWait();

        } else {
            String regData = "INSERT INTO employee (username,password,question,answer,date)"
                    + "VALUES(?,?,?,?,?)";
            connect = database.connectDB();
            try {
                String checkUsername = "SELECT username FROM employee WHERE username = '"
                        + su_username.getText().toString() + "'";
                prepare = connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Registration Error");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText().toString() + " is already taken.");
                    alert.showAndWait();
                } else if (su_password.getText().length() < 5) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Registration Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password is too Small! , Atleast 5 characters are needed.");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(regData);
                    prepare.setString(1, su_username.getText().toString());
                    prepare.setString(2, su_password.getText().toString());
                    prepare.setString(3, su_question.getSelectionModel().getSelectedItem().toString());
                    prepare.setString(4, su_answer.getText().toString());

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(5, String.valueOf(sqlDate));
                    prepare.executeUpdate();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Registered User!");
                    alert.showAndWait();

                    su_username.setText("");
                    su_password.setText("");
                    su_question.getSelectionModel().clearSelection();
                    su_answer.setText("");

                    TranslateTransition slider = new TranslateTransition();
                    slider.setNode(side_form);
                    slider.setToX(0);
                    slider.setDuration(Duration.seconds(.5));

                    slider.setOnFinished((ActionEvent e) -> {
                        side_alreadyHave.setVisible(false);
                        side_CreateBtn.setVisible(true);

//                fp_questionForm.setVisible(false);
//                si_loginForm.setVisible(true);
//                np_newPassForm.setVisible(false);
                    });

                    slider.play();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String[] questionList = {"What is your favourite anime?",
        "Who is the best fire character in all anime?",
        "Have you watched high School DXD?"};

    public void regLquestionList() {
        List<String> listQ = new ArrayList<String>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList listdata = FXCollections.observableArrayList(listQ);
        su_question.setItems(listdata);
    }

    @FXML
    public void switchForm(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition();
//        System.out.println(event.getSource().toString());

        if (event.getSource() == side_CreateBtn) {

            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(0.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);
                su_signupForm.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
                regLquestionList();

            });

            slider.play();
        } else if (event.getSource() == side_alreadyHave) {

            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });

            slider.play();
        }

    }

    public void proceedBtn() {
        if (fp_username.getText().isEmpty() || fp_question.getSelectionModel().getSelectedItem() == null
                || fp_answer.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message ");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Question/Answer");
            alert.showAndWait();
        } else {
            String selectData = "SELECT username,question,answer FROM employee WHERE username=? AND question=? AND answer=?";
            connect = database.connectDB();
            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, fp_username.getText());
                prepare.setString(2, fp_question.getSelectionModel().getSelectedItem().toString());
                prepare.setString(3, fp_answer.getText());

                result = prepare.executeQuery();
                if (result.next()) {
                    np_newPassForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message ");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Information");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void changePassBtn() {
        if (np_newPassword.getText().isEmpty() || np_confirmPassword.getText().isBlank()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message ");
            alert.setHeaderText(null);
            alert.setContentText("Fill All the necessary Fields!");
            alert.showAndWait();
        } else {
            if (np_newPassword.getText().equals(np_confirmPassword.getText())) {
                String getDate = "SELECT date FROM employee WHERE username ='" + fp_username.getText() + "'";
                connect = database.connectDB();
                try {
                    prepare = connect.prepareStatement(getDate);
                    result = prepare.executeQuery();
                    String date = "";

                    if (result.next()) {
                        date = result.getString("date");
                    }
                    String updatePass = "UPDATE employee SET password='"
                            + np_newPassword.getText() + "',question ='"
                            + fp_question.getSelectionModel().getSelectedItem().toString() + "',answer ='"
                            + fp_answer.getText() + "',date='" + date
                            + "' WHERE username='" + fp_username.getText() + "'";
                    prepare = connect.prepareStatement(updatePass);
                    prepare.executeUpdate();
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Information Message ");
                    alert.setHeaderText(null);
                    alert.setContentText("Password change successfull!");
                    alert.showAndWait();
                    si_loginForm.setVisible(true);
                    np_newPassForm.setVisible(false);
                    
                    np_newPassword.setText("");
                    fp_answer.setText("");
                    fp_username.setText("");
                    np_confirmPassword.setText("");
                    fp_question.getSelectionModel().clearSelection();
                    

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message ");
                alert.setHeaderText(null);
                alert.setContentText("Password Doesn't match !");
                alert.showAndWait();
            }

        }
    }
    public void backToLoginForm(){
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
    }
    public void backToQuestionForm(){
        
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }
    public void forgotPassQuestionList() {
        List<String> listQ = new ArrayList<String>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList listdata = FXCollections.observableArrayList(listQ);
        fp_question.setItems(listdata);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
