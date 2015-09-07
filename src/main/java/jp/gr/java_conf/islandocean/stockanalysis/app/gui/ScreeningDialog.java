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
	TextField minEpsTextField;
	TextField maxEpsTextField;
	TextField minBpsTextField;
	TextField maxBpsTextField;
	TextField minRoeTextField;
	TextField maxRoeTextField;
	TextField minMarketCapitalizationTextField;
	TextField maxMarketCapitalizationTextField;
	TextField minAverageAnnualSalaryTextField;
	TextField maxAverageAnnualSalaryTextField;
	TextField minAverageAgeTextField;
	TextField maxAverageAgeTextField;

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

		minEpsTextField = new TextField();
		minEpsTextField.setPromptText("minEps");
		if ((org = screeningParameter.getMinEps()) != null) {
			minEpsTextField.setText(org.toString());
		}

		maxEpsTextField = new TextField();
		maxEpsTextField.setPromptText("maxEps");
		if ((org = screeningParameter.getMaxEps()) != null) {
			maxEpsTextField.setText(org.toString());
		}

		minBpsTextField = new TextField();
		minBpsTextField.setPromptText("minBps");
		if ((org = screeningParameter.getMinBps()) != null) {
			minBpsTextField.setText(org.toString());
		}

		maxBpsTextField = new TextField();
		maxBpsTextField.setPromptText("maxBps");
		if ((org = screeningParameter.getMaxBps()) != null) {
			maxBpsTextField.setText(org.toString());
		}

		minRoeTextField = new TextField();
		minRoeTextField.setPromptText("minRoe");
		if ((org = screeningParameter.getMinRoe()) != null) {
			minRoeTextField.setText(org.toString());
		}

		maxRoeTextField = new TextField();
		maxRoeTextField.setPromptText("maxRoe");
		if ((org = screeningParameter.getMaxRoe()) != null) {
			maxRoeTextField.setText(org.toString());
		}

		minMarketCapitalizationTextField = new TextField();
		minMarketCapitalizationTextField
				.setPromptText("minMarketCapitalization");
		if ((org = screeningParameter.getMinMarketCapitalization()) != null) {
			minMarketCapitalizationTextField.setText(org.toString());
		}

		maxMarketCapitalizationTextField = new TextField();
		maxMarketCapitalizationTextField
				.setPromptText("maxMarketCapitalization");
		if ((org = screeningParameter.getMaxMarketCapitalization()) != null) {
			maxMarketCapitalizationTextField.setText(org.toString());
		}

		minAverageAnnualSalaryTextField = new TextField();
		minAverageAnnualSalaryTextField.setPromptText("minAverageAnnualSalary");
		if ((org = screeningParameter.getMinAverageAnnualSalary()) != null) {
			minAverageAnnualSalaryTextField.setText(org.toString());
		}

		maxAverageAnnualSalaryTextField = new TextField();
		maxAverageAnnualSalaryTextField.setPromptText("maxAverageAnnualSalary");
		if ((org = screeningParameter.getMaxAverageAnnualSalary()) != null) {
			maxAverageAnnualSalaryTextField.setText(org.toString());
		}

		minAverageAgeTextField = new TextField();
		minAverageAgeTextField.setPromptText("minAverageAge");
		if ((org = screeningParameter.getMinAverageAge()) != null) {
			minAverageAgeTextField.setText(org.toString());
		}

		maxAverageAgeTextField = new TextField();
		maxAverageAgeTextField.setPromptText("maxAverageAge");
		if ((org = screeningParameter.getMaxAverageAge()) != null) {
			maxAverageAgeTextField.setText(org.toString());
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

		col = 0;
		grid.add(new Label("Eps:"), col++, row);
		grid.add(minEpsTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxEpsTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label("Bps:"), col++, row);
		grid.add(minBpsTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxBpsTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label("Roe:"), col++, row);
		grid.add(minRoeTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxRoeTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label("Market Capitalization:"), col++, row);
		grid.add(minMarketCapitalizationTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxMarketCapitalizationTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label("Average Annual Salary:"), col++, row);
		grid.add(minAverageAnnualSalaryTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxAverageAnnualSalaryTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label("Average Age:"), col++, row);
		grid.add(minAverageAgeTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxAverageAgeTextField, col++, row);
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
				String minEps = minEpsTextField.getText();
				String maxEps = maxEpsTextField.getText();
				String minBps = minBpsTextField.getText();
				String maxBps = maxBpsTextField.getText();
				String minRoe = minRoeTextField.getText();
				String maxRoe = maxRoeTextField.getText();
				String minMarketCapitalization = minMarketCapitalizationTextField
						.getText();
				String maxMarketCapitalization = maxMarketCapitalizationTextField
						.getText();
				String minAverageAnnualSalary = minAverageAnnualSalaryTextField
						.getText();
				String maxAverageAnnualSalary = maxAverageAnnualSalaryTextField
						.getText();
				String minAverageAge = minAverageAgeTextField.getText();
				String maxAverageAge = maxAverageAgeTextField.getText();

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

				try {
					d = Double.parseDouble(minEps);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinEps(d);

				try {
					d = Double.parseDouble(maxEps);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxEps(d);

				try {
					d = Double.parseDouble(minBps);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinBps(d);

				try {
					d = Double.parseDouble(maxBps);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxBps(d);

				try {
					d = Double.parseDouble(minRoe);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinRoe(d);

				try {
					d = Double.parseDouble(maxRoe);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxRoe(d);

				try {
					d = Double.parseDouble(minMarketCapitalization);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinMarketCapitalization(d);

				try {
					d = Double.parseDouble(maxMarketCapitalization);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxMarketCapitalization(d);

				try {
					d = Double.parseDouble(minAverageAnnualSalary);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinAverageAnnualSalary(d);

				try {
					d = Double.parseDouble(maxAverageAnnualSalary);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxAverageAnnualSalary(d);

				try {
					d = Double.parseDouble(minAverageAge);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMinAverageAge(d);

				try {
					d = Double.parseDouble(maxAverageAge);
				} catch (NumberFormatException e) {
					d = null;
				}
				screeningParameter.setMaxAverageAge(d);

				return this.screeningParameter;
			}
			return null;
		});
	}
}
