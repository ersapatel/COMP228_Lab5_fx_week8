package application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.geometry.Pos;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		BorderPane pane = new BorderPane();
	    // Place nodes in the pane
	    pane.setTop(new CustomHorizonalTop("Top")); 	    
	    pane.setRight(new CustomPane("Right"));
	    pane.setBottom(new CustomPane("Bottom"));
	    pane.setLeft(new CustomPane("Left"));
	    pane.setCenter(new CustomPane("Center"));
	    
	    // Create a scene and place it in the stage
	    Scene scene = new Scene(pane);
	    primaryStage.setTitle("ShowBorderPane");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    // --- Employment Application Form ---
        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(20));
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setAlignment(Pos.TOP_LEFT);

        // Form Title
        Label titleLabel = new Label("Employment Application Form Example");
        titleLabel.setStyle("-fx-font-weight: bold;");
        formPane.add(titleLabel, 0, 0, 2, 1);

        // First Name Fields
        CreateFormPane(formPane, "First name *", 0, 1);
        
        // Last Name Fields
        CreateFormPane(formPane, "Last name *", 0, 2);

        // Email
        CreateFormPane(formPane, "Email *", 0, 3);

        // Portfolio website
        CreateFormPane(formPane, "Portfolio website", 0, 4);

        // Position applying for
        CreateFormPane(formPane, "Position you are applying for *", 0, 5);

        // Salary
        CreateFormPane(formPane, "Salary requirements", 0, 6);
        
        // Start date 
        CreateFormPane(formPane, "When can you start?", 0, 7);

        // Phone
        CreateFormPane(formPane, "Phone *", 0, 8);
        
        // Fax
        CreateFormPane(formPane, "Fax", 0, 9);

        // Relocation
        formPane.add(new Label("Are you willing to relocate?"), 0, 10);
        ToggleGroup relocateGroup = new ToggleGroup();
        RadioButton yes = new RadioButton("Yes");
        RadioButton no = new RadioButton("No");
        RadioButton unsure = new RadioButton("Not sure");
        yes.setToggleGroup(relocateGroup);
        no.setToggleGroup(relocateGroup);
        unsure.setToggleGroup(relocateGroup);
        HBox relocateBox = new HBox(10, yes, no, unsure);
        formPane.add(relocateBox, 1, 10);

        // Last company
        CreateFormPane(formPane, "Last company you worked for", 0, 11);

        // Comments
        CreateFormPane(formPane, "Reference / Comments / Questions", 0, 12);

        // Submit button
        Button sendBtn = new Button("Send Application");
        sendBtn.setOnAction(e -> System.out.println("Application Sent"));
        formPane.add(sendBtn, 1, 13);

        pane.setCenter(formPane);
	}
	
	public static void CreateFormPane(GridPane formPane, String label, int col, int row) {        
        Label lbl = new Label(label);
        TextField textField = new TextField();
        VBox box = new VBox(5, lbl, textField);
        formPane.add(box, col, row);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}