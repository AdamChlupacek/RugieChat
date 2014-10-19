package com.rugie.chat.client;

import com.rugie.chat.server.Constants;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Created with IntelliJ IDEA.
 * User: adamchlupacek
 * Date: 18/10/14
 * Time: 15:56
 */
public class GuiWindow extends Application {

  @Override
  public void start(final Stage primaryStage) throws Exception {

    //Creating a GridPane container
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(5);
    grid.setHgap(5);
    grid.setAlignment(Pos.CENTER);
//Defining the Name text field
    final TextField ip = new TextField();
    ip.setPromptText("IP");
    grid.add(ip,0,0);
//Defining the Last Name text field
    final TextField port = new TextField();
    port.setPromptText("Port");
    grid.add(port,0,1);
//Defining the Comment text field
    final TextField username = new TextField();
    username.setPrefColumnCount(15);
    username.setPromptText("Enter your comment.");
    grid.add(username,0,2);
//Defining the Submit button
    Button submit = new Button("Submit");
    grid.add(submit,0,3);

//Setting an action for the Submit button
    submit.setOnAction(e -> {
      if (!ip.getText().isEmpty() && !port.getText().isEmpty() && !username.getText().isEmpty()){

        try {

          //TextArea
          GridPane topGrid = new GridPane();

          topGrid.setPadding(new Insets(10, 10, 10, 10));
          topGrid.setVgap(5);
          topGrid.setHgap(5);
          topGrid.setAlignment(Pos.CENTER);

          TextArea textArea = new TextArea();
          textArea.setEditable(false);
          textArea.setPrefColumnCount(50);
          textArea.setPrefRowCount(200);

          GridPane subGrid = new GridPane();

          subGrid.setVgap(5);
          subGrid.setHgap(5);

          final TextField sendField = new TextField();

          sendField.setPrefColumnCount(45);

          Button send = new Button(" Send ");

          final SocketManager manager = new SocketManager(ip.getText(),Integer.parseInt(port.getText()), textArea);
          manager.send(Constants.NEW_CONNECT + username.getText() + Constants.NEW_CONNECT);
          manager.setAction(textArea::appendText);
          manager.stopAction(()->{
            try {
              primaryStage.close();
            } catch (Exception e1) {
              e1.printStackTrace();
            }
          });

          new Thread(manager,"Socket-Manager").start();
          send.setOnAction(event -> {
            if (!sendField.getText().isEmpty()){
              try {
                manager.send(Constants.MESSAGE + sendField.getText() + Constants.MESSAGE);
                sendField.clear();
              } catch (IOException e1) {
                e1.printStackTrace();
              }
            }
          });

          sendField.setOnKeyPressed(event->{
            if (event.getCode().equals(KeyCode.ENTER)){
              if (!sendField.getText().isEmpty()){
                try {
                  manager.send(Constants.MESSAGE + sendField.getText() + Constants.MESSAGE);
                  sendField.clear();
                } catch (IOException e1) {
                  e1.printStackTrace();
                }
              }
            }
          });

          primaryStage.setOnCloseRequest(event -> {
            try {
              manager.stop();
            } catch (Exception e1) {
              e1.printStackTrace();
            }
          });
          subGrid.add(sendField,0,0);
          subGrid.add(send,1,0);

          topGrid.add(textArea,0,0);
          topGrid.add(subGrid,0,1);

          Scene scene = new Scene(topGrid, 600, 550);
          primaryStage.setScene(scene);

          sendField.requestFocus();
        } catch (Exception e1) {
          e1.printStackTrace();
        }

      }
    });

    Scene scene = new Scene(grid, 600, 550);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();

  }
}
