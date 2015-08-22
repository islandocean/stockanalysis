package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class ScreeningDialog extends Dialog<Pair<String, String>> {

	ButtonType executeButtonType;
	GridPane grid;

	public ScreeningDialog() {
		super();
		initScreeningDialog();
	}

	public void initScreeningDialog() {
		System.out.println("ScreeningDialog#init()");

		setTitle("Screening Dialog");
		setHeaderText("Select screening parameters.");

		executeButtonType = new ButtonType("Execute Screening",
				ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(executeButtonType,
				ButtonType.CANCEL);

		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		getDialogPane().setContent(grid);

		setResultConverter(dialogButton -> {
			if (dialogButton == executeButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});
	}
}
