package application;

import javafx.geometry.Insets;

import javafx.scene.control.Label;

import javafx.scene.layout.StackPane;

public class CustomPane extends StackPane {
	
  public CustomPane(String title) {
    getChildren().add(new Label(title));
    setStyle("-fx-border-color: red");
    setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
  }
  
  
}