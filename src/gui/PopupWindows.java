package gui;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * Created by: TheNewBoston (YouTube)
 * Modified by: Tobaeas10 (GitHub)
 */
public class PopupWindows {

    //Create variable
    static boolean confirm;

    public static boolean confirmOrCancel(String title, String message) {
    	confirm = false;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        //Create two buttons
        Button yesButton = new Button("Confirm");
        Button noButton = new Button("Cancel");

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            confirm = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            confirm = false;
            window.close();
        });

        VBox layout = new VBox(10);

        //Add buttons
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return confirm;
    }

    //[0]: confirm/cancel?, [...] checkBoxesSelected?
    public static boolean[] confirmOrCancelWithCheckBoxes(String title, String message, ArrayList<CheckBox> checkBoxes) {
    	confirm = false;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        //Create two buttons
        Button yesButton = new Button("Confirm");
        Button noButton = new Button("Cancel");

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            confirm = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            confirm = false;
            window.close();
        });

        VBox layout = new VBox(10);
        HBox layout2 = new HBox(10);
        layout2.getChildren().addAll(yesButton, noButton);
        
        VBox checkBoxesLayout = new VBox(5);
        checkBoxesLayout.getChildren().addAll(checkBoxes);
        
        //Add buttons, checkboxes
        layout.getChildren().add(label);
        layout.getChildren().add(checkBoxesLayout);
        layout.getChildren().add(layout2);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        boolean[] result = new boolean[checkBoxes.size() + 1];
        result[0] = confirm;
        for(int i = 1; i < checkBoxes.size() + 1; i++) {
        	result[i] = checkBoxes.get(i - 1).isSelected();
        }
        return result;
    }
}
