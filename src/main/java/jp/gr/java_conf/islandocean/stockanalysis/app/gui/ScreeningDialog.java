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
	ButtonType closeButtonType;
	ButtonType cancelButtonType;
	GridPane grid;
	TextField minAnnualInterestRateTextField;
	TextField maxAnnualInterestRateTextField;
	TextField minPerTextField;
	TextField maxPerTextField;
	TextField minPbrTextField;
	TextField maxPbrTextField;

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
		closeButtonType = new ButtonType("Close", ButtonData.OK_DONE);
		cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(executeButtonType,
				closeButtonType, cancelButtonType);

		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		Double org;

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

		int row = 0;
		int col;

		col = 0;
		grid.add(new Label("Annual Interest Rate:"), col++, row);
		grid.add(minAnnualInterestRateTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxAnnualInterestRateTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label("Per:"), col++, row);
		grid.add(minPerTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxPerTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label("Pbr:"), col++, row);
		grid.add(minPbrTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxPbrTextField, col++, row);
		++row;

		getDialogPane().setContent(grid);

		setResultConverter(dialogButton -> {
			if (dialogButton == executeButtonType
					|| dialogButton == closeButtonType) {

				boolean execute = false;
				if (dialogButton == executeButtonType) {
					execute = true;
				}
				screeningParameter.setExecute(execute);

				String minAnnualInterestRate = minAnnualInterestRateTextField
						.getText();
				String maxAnnualInterestRate = maxAnnualInterestRateTextField
						.getText();
				String minPer = minPerTextField.getText();
				String maxPer = maxPerTextField.getText();
				String minPbr = minPbrTextField.getText();
				String maxPbr = maxPbrTextField.getText();

				Double d;

				try {
					d = Double.parseDouble(minAnnualInterestRate);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinAnnualInterestRate(d);

				try {
					d = Double.parseDouble(maxAnnualInterestRate);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxAnnualInterestRate(d);

				try {
					d = Double.parseDouble(minPer);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinPer(d);

				try {
					d = Double.parseDouble(maxPer);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxPer(d);

				try {
					d = Double.parseDouble(minPbr);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinPbr(d);

				try {
					d = Double.parseDouble(maxPbr);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxPbr(d);

				return this.screeningParameter;
			}
			return null;
		});
	}
}
