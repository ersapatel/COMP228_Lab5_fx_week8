package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.geometry.Pos;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private final Map<String, TextInputControl> inputs = new HashMap<>();
    private final ToggleGroup relocateGroup = new ToggleGroup();
    private final TextArea readArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {

        BorderPane pane = new BorderPane();
        // Place nodes in the pane (you can keep your custom panes)
        pane.setTop(new CustomHorizonalTop("Top"));
        pane.setRight(new CustomPane("Right"));
        pane.setBottom(new CustomPane("Bottom"));
        pane.setLeft(new CustomPane("Left"));

        // --- Employment Application Form ---
        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(20));
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setAlignment(Pos.TOP_LEFT);

        // Form Title
        Label titleLabel = new Label("Employment Application Form Example");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        formPane.add(titleLabel, 0, 0, 2, 1);

        int r = 1;

        // First / Last / Email / Portfolio / Position / Salary / Start / Phone / Fax
        addField(formPane, "First name *", 0, r++);
        addField(formPane, "Last name *", 0, r++);
        addField(formPane, "Email *", 0, r++);
        addField(formPane, "Portfolio website", 0, r++);
        addField(formPane, "Position you are applying for *", 0, r++);
        addField(formPane, "Salary requirements", 0, r++);
        addField(formPane, "When can you start?", 0, r++);
        addField(formPane, "Phone *", 0, r++);
        addField(formPane, "Fax", 0, r++);

        // Relocation
        formPane.add(new Label("Are you willing to relocate?"), 0, r);
        HBox relocateBox = new HBox(10);
        RadioButton yes = new RadioButton("Yes");
        RadioButton no = new RadioButton("No");
        RadioButton unsure = new RadioButton("Not sure");
        yes.setToggleGroup(relocateGroup);
        no.setToggleGroup(relocateGroup);
        unsure.setToggleGroup(relocateGroup);
        relocateBox.getChildren().addAll(yes, no, unsure);
        formPane.add(relocateBox, 1, r++);
        
        // Last company
        addField(formPane, "Last company you worked for", 0, r++);

        // TextArea
        Label commentsLbl = new Label("Reference / Comments / Questions");
        TextArea commentsArea = new TextArea();
        commentsArea.setPrefRowCount(4);
        inputs.put("Reference / Comments / Questions", commentsArea);
        VBox commentsBox = new VBox(5, commentsLbl, commentsArea);
        formPane.add(commentsBox, 0, r++, 2, 1);

        // Buttons: Submit + Read
        Button sendBtn = new Button("Submit");
        Button readBtn = new Button("Read");
        sendBtn.setDefaultButton(true);
        sendBtn.setOnAction(e -> insertRecord());
        readBtn.setOnAction(e -> readAll());
        HBox actions = new HBox(10, sendBtn, readBtn);
        formPane.add(actions, 0, r++, 2, 1);

        // Read output area
        readArea.setEditable(false);
        readArea.setPrefRowCount(14);
        readArea.setMinHeight(260); 
        formPane.add(new Label("Database Records:"), 0, r++, 2, 1);
        formPane.add(readArea, 0, r, 2, 1);

        pane.setCenter(formPane);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 950, 700);
        primaryStage.setTitle("ShowBorderPane");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addField(GridPane grid, String label, int col, int row) {
        Label lbl = new Label(label);
        TextField tf = new TextField();
        inputs.put(label, tf);
        VBox box = new VBox(5, lbl, tf);
        grid.add(box, col, row, 2, 1);
    }

    private void insertRecord() {
        String first = text("First name *");
        String last = text("Last name *");
        String email = text("Email *");
        String site = text("Portfolio website");
        String position = text("Position you are applying for *");
        String salary = text("Salary requirements");
        String start = text("When can you start?");
        String phone = text("Phone *");
        String fax = text("Fax");
        String company = text("Last company you worked for");
        String comments = textArea("Reference / Comments / Questions");
        String relocate = (relocateGroup.getSelectedToggle() == null) ? null : ((RadioButton) relocateGroup.getSelectedToggle()).getText();

        if (isBlank(first) || isBlank(last) || isBlank(email) || isBlank(phone) || isBlank(position)) {
            System.out.println("Missing required fields.");
            return;
        }

        final String sql = """
            INSERT INTO applicants
              (first_name, last_name, email, portfolio, position_applied, salary_requirements,
               start_when, phone, fax, willing_relocate, last_company, comments)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
            """;

        try (Connection con = JDBCConnector.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, first);
            ps.setString(2, last);
            ps.setString(3, email);
            ps.setString(4, nullIfBlank(site));
            ps.setString(5, position);
            ps.setString(6, nullIfBlank(salary));
            ps.setString(7, nullIfBlank(start));
            ps.setString(8, phone);
            ps.setString(9, nullIfBlank(fax));
            ps.setString(10, relocate);
            ps.setString(11, nullIfBlank(company));
            ps.setString(12, nullIfBlank(comments));

            int rows = ps.executeUpdate();
            System.out.println("Application Sent");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void readAll() {
        final String sql = """
            SELECT id, first_name, last_name, email, phone, position_applied, willing_relocate, created_at
            FROM applicants
            ORDER BY id DESC
            """;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-12s %-12s %-22s %-14s %-20s %-10s %-19s%n",
                "ID","First","Last","Email","Phone","Position","Relocate","Created"));

        try (Connection con = JDBCConnector.get();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                sb.append(String.format("%-4d %-12s %-12s %-22s %-14s %-20s %-10s %-19s%n",
                        rs.getInt("id"),
                        clip(rs.getString("first_name"), 12),
                        clip(rs.getString("last_name"),  12),
                        clip(rs.getString("email"),      22),
                        clip(rs.getString("phone"),      14),
                        clip(rs.getString("position_applied"), 20),
                        clip(rs.getString("willing_relocate"), 10),
                        rs.getTimestamp("created_at")));
            }
            readArea.setText(sb.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            readArea.setText("DB error: " + ex.getMessage());
        }
    }

    private String text(String label) {
        TextInputControl c = inputs.get(label);
        return (c == null || c.getText() == null) ? "" : c.getText().trim();
    }
    private String textArea(String label) {
        TextInputControl c = inputs.get(label);
        return (c == null) ? "" : c.getText().trim();
    }
    private static boolean isBlank(String s){ return s == null || s.isBlank(); }
    private static String nullIfBlank(String s){ return isBlank(s) ? null : s; }
    private static String clip(String s, int n){ if(s==null) return ""; return s.length()<=n? s : s.substring(0,n-1)+"…"; }

    public static void main(String[] args) {
        launch(args);
    }
}
