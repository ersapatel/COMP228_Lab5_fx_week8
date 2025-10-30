package application;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class CustomHorizonalTop extends VBox {

	 public CustomHorizonalTop(String title) {
		    getChildren().add(new Label(title));
		    setStyle("-fx-border-color: red");
		    setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		    setAlignment(Pos.CENTER);
		  }
}