package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ScreeningDialog extends Dialog<ScreeningParameter> {

	ResourceBundle resource;
	ScreeningParameter screeningParameter;
	ButtonType executeButtonType;
	GridPane grid;
	TextField minPerTextField;
	TextField maxPerTextField;
	TextField minPbrTextField;
	TextField maxPbrTextField;
	TextField minAnnualInterestRateTextField;
	TextField maxAnnualInterestRateTextField;

	public ScreeningDialog(ResourceBundle resource,
			ScreeningParameter screeningParameter) {
		super();
		this.resource = resource;
		this.screeningParameter = screeningParameter;
		initScreeningDialog();
	}

	public void initScreeningDialog() {
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

		Double org;

		minPerTextField = new TextField();
		minPerTextField.setPromptText("minPer");
		if ((org = screeningParameter.getMinPer()) != null) {
			minPerTextField.setText(org.toString());
		}

		maxPerTextField = new TextField();
		maxPerTextField.setPromptText("maxPer");
		if ((org = screeningParameter.getMaxPer()) != null) {
			maxPerTextField.setText(org.toString());
		}

		minPbrTextField = new TextField();
		minPbrTextField.setPromptText("minPbr");
		if ((org = screeningParameter.getMinPbr()) != null) {
			minPbrTextField.setText(org.toString());
		}

		maxPbrTextField = new TextField();
		maxPbrTextField.setPromptText("maxPbr");
		if ((org = screeningParameter.getMaxPbr()) != null) {
			maxPbrTextField.setText(org.toString());
		}

		minAnnualInterestRateTextField = new TextField();
		minAnnualInterestRateTextField.setPromptText("minAnnualInterestRate");
		if ((org = screeningParameter.getMinAnnualInterestRate()) != null) {
			minAnnualInterestRateTextField.setText(org.toString());
		}

		maxAnnualInterestRateTextField = new TextField();
		maxAnnualInterestRateTextField.setPromptText("maxAnnualInterestRate");
		if ((org = screeningParameter.getMaxAnnualInterestRate()) != null) {
			maxAnnualInterestRateTextField.setText(org.toString());
		}

		grid.add(new Label("minPer:"), 0, 0);
		grid.add(minPerTextField, 1, 0);

		grid.add(new Label("maxPer:"), 0, 1);
		grid.add(maxPerTextField, 1, 1);

		grid.add(new Label("minPbr:"), 0, 2);
		grid.add(minPbrTextField, 1, 2);

		grid.add(new Label("maxPbr:"), 0, 3);
		grid.add(maxPbrTextField, 1, 3);

		grid.add(new Label("minAnnualInterestRate:"), 0, 4);
		grid.add(minAnnualInterestRateTextField, 1, 4);

		grid.add(new Label("maxAnnualInterestRate:"), 0, 5);
		grid.add(maxAnnualInterestRateTextField, 1, 5);

		getDialogPane().setContent(grid);

		setResultConverter(dialogButton -> {
			if (dialogButton == executeButtonType) {
				String minPer = minPerTextField.getText();
				String maxPer = maxPerTextField.getText();
				String minPbr = minPbrTextField.getText();
				String maxPbr = maxPbrTextField.getText();
				String minAnnualInterestRate = minAnnualInterestRateTextField
						.getText();
				String maxAnnualInterestRate = maxAnnualInterestRateTextField
						.getText();

				double d;

				try {
					d = Double.parseDouble(minPer);
					screeningParameter.setMinPer(d);
				} catch (NumberFormatException e) {
				}

				try {
					d = Double.parseDouble(maxPer);
					screeningParameter.setMaxPer(d);
				} catch (NumberFormatException e) {
				}

				try {
					d = Double.parseDouble(minPbr);
					screeningParameter.setMinPbr(d);
				} catch (NumberFormatException e) {
				}

				try {
					d = Double.parseDouble(maxPbr);
					screeningParameter.setMaxPbr(d);
				} catch (NumberFormatException e) {
				}

				try {
					d = Double.parseDouble(minAnnualInterestRate);
					screeningParameter.setMinAnnualInterestRate(d);
				} catch (NumberFormatException e) {
				}

				try {
					d = Double.parseDouble(maxAnnualInterestRate);
					screeningParameter.setMaxAnnualInterestRate(d);
				} catch (NumberFormatException e) {
				}

				return this.screeningParameter;
			}
			return null;
		});
	}
}
