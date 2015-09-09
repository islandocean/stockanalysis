package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;

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
		initDialog();
	}

	public void initDialog() {
		setTitle(resource.getString(MessageKey.SCREENING_DIALOG_TITLE));
		setHeaderText(resource
				.getString(MessageKey.SCREENING_DIALOG_HEADER_TEXT));

		//
		// Controls
		//

		executeButtonType = new ButtonType(
				resource.getString(MessageKey.EXECUTE_BUTTON),
				ButtonData.OK_DONE);
		closeButtonType = new ButtonType(
				resource.getString(MessageKey.CLOSE_BUTTON), ButtonData.OK_DONE);
		cancelButtonType = new ButtonType(
				resource.getString(MessageKey.CANCEL_BUTTON),
				ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(executeButtonType,
				closeButtonType, cancelButtonType);

		String min = resource.getString(MessageKey.MIN_PROMPTTEXT);
		String max = resource.getString(MessageKey.MAX_PROMPTTEXT);

		Double org;

		minAnnualInterestRateTextField = new TextField();
		minAnnualInterestRateTextField.setPromptText(min);
		if ((org = screeningParameter.getMinAnnualInterestRate()) != null) {
			minAnnualInterestRateTextField.setText(org.toString());
		}

		maxAnnualInterestRateTextField = new TextField();
		maxAnnualInterestRateTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxAnnualInterestRate()) != null) {
			maxAnnualInterestRateTextField.setText(org.toString());
		}

		minPerTextField = new TextField();
		minPerTextField.setPromptText(min);
		if ((org = screeningParameter.getMinPer()) != null) {
			minPerTextField.setText(org.toString());
		}

		maxPerTextField = new TextField();
		maxPerTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxPer()) != null) {
			maxPerTextField.setText(org.toString());
		}

		minPbrTextField = new TextField();
		minPbrTextField.setPromptText(min);
		if ((org = screeningParameter.getMinPbr()) != null) {
			minPbrTextField.setText(org.toString());
		}

		maxPbrTextField = new TextField();
		maxPbrTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxPbr()) != null) {
			maxPbrTextField.setText(org.toString());
		}

		minEpsTextField = new TextField();
		minEpsTextField.setPromptText(min);
		if ((org = screeningParameter.getMinEps()) != null) {
			minEpsTextField.setText(org.toString());
		}

		maxEpsTextField = new TextField();
		maxEpsTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxEps()) != null) {
			maxEpsTextField.setText(org.toString());
		}

		minBpsTextField = new TextField();
		minBpsTextField.setPromptText(min);
		if ((org = screeningParameter.getMinBps()) != null) {
			minBpsTextField.setText(org.toString());
		}

		maxBpsTextField = new TextField();
		maxBpsTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxBps()) != null) {
			maxBpsTextField.setText(org.toString());
		}

		minRoeTextField = new TextField();
		minRoeTextField.setPromptText(min);
		if ((org = screeningParameter.getMinRoe()) != null) {
			minRoeTextField.setText(org.toString());
		}

		maxRoeTextField = new TextField();
		maxRoeTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxRoe()) != null) {
			maxRoeTextField.setText(org.toString());
		}

		minMarketCapitalizationTextField = new TextField();
		minMarketCapitalizationTextField.setPromptText(min);
		if ((org = screeningParameter.getMinMarketCapitalization()) != null) {
			minMarketCapitalizationTextField.setText(org.toString());
		}

		maxMarketCapitalizationTextField = new TextField();
		maxMarketCapitalizationTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxMarketCapitalization()) != null) {
			maxMarketCapitalizationTextField.setText(org.toString());
		}

		minAverageAnnualSalaryTextField = new TextField();
		minAverageAnnualSalaryTextField.setPromptText(min);
		if ((org = screeningParameter.getMinAverageAnnualSalary()) != null) {
			minAverageAnnualSalaryTextField.setText(org.toString());
		}

		maxAverageAnnualSalaryTextField = new TextField();
		maxAverageAnnualSalaryTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxAverageAnnualSalary()) != null) {
			maxAverageAnnualSalaryTextField.setText(org.toString());
		}

		minAverageAgeTextField = new TextField();
		minAverageAgeTextField.setPromptText(min);
		if ((org = screeningParameter.getMinAverageAge()) != null) {
			minAverageAgeTextField.setText(org.toString());
		}

		maxAverageAgeTextField = new TextField();
		maxAverageAgeTextField.setPromptText(max);
		if ((org = screeningParameter.getMaxAverageAge()) != null) {
			maxAverageAgeTextField.setText(org.toString());
		}

		//
		// Formatter
		//

		minAnnualInterestRateTextField
				.setTextFormatter(createDoubleFormatter());
		maxAnnualInterestRateTextField
				.setTextFormatter(createDoubleFormatter());
		minPerTextField.setTextFormatter(createDoubleFormatter());
		maxPerTextField.setTextFormatter(createDoubleFormatter());
		minPbrTextField.setTextFormatter(createDoubleFormatter());
		maxPbrTextField.setTextFormatter(createDoubleFormatter());
		minEpsTextField.setTextFormatter(createDoubleFormatter());
		maxEpsTextField.setTextFormatter(createDoubleFormatter());
		minBpsTextField.setTextFormatter(createDoubleFormatter());
		maxBpsTextField.setTextFormatter(createDoubleFormatter());
		minRoeTextField.setTextFormatter(createDoubleFormatter());
		maxRoeTextField.setTextFormatter(createDoubleFormatter());
		minMarketCapitalizationTextField
				.setTextFormatter(createDoubleFormatter());
		maxMarketCapitalizationTextField
				.setTextFormatter(createDoubleFormatter());
		minAverageAnnualSalaryTextField
				.setTextFormatter(createDoubleFormatter());
		maxAverageAnnualSalaryTextField
				.setTextFormatter(createDoubleFormatter());
		minAverageAgeTextField.setTextFormatter(createDoubleFormatter());
		maxAverageAgeTextField.setTextFormatter(createDoubleFormatter());

		//
		// Layout
		//

		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		int row = 0;
		int col;

		col = 0;
		grid.add(
				new Label(resource.getString(MessageKey.ANNUAL_INTEREST_RATE)),
				col++, row);
		grid.add(minAnnualInterestRateTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxAnnualInterestRateTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label(resource.getString(MessageKey.PER)), col++, row);
		grid.add(minPerTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxPerTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label(resource.getString(MessageKey.PBR)), col++, row);
		grid.add(minPbrTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxPbrTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label(resource.getString(MessageKey.EPS)), col++, row);
		grid.add(minEpsTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxEpsTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label(resource.getString(MessageKey.BPS)), col++, row);
		grid.add(minBpsTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxBpsTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label(resource.getString(MessageKey.ROE)), col++, row);
		grid.add(minRoeTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxRoeTextField, col++, row);
		++row;

		col = 0;
		grid.add(
				new Label(resource.getString(MessageKey.MARKET_CAPITALIZATION)),
				col++, row);
		grid.add(minMarketCapitalizationTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxMarketCapitalizationTextField, col++, row);
		++row;

		col = 0;
		grid.add(
				new Label(resource.getString(MessageKey.AVERAGE_ANNUAL_SALARY)),
				col++, row);
		grid.add(minAverageAnnualSalaryTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxAverageAnnualSalaryTextField, col++, row);
		++row;

		col = 0;
		grid.add(new Label(resource.getString(MessageKey.AVERAGE_AGE)), col++,
				row);
		grid.add(minAverageAgeTextField, col++, row);
		grid.add(new Label("-"), col++, row);
		grid.add(maxAverageAgeTextField, col++, row);
		++row;

		getDialogPane().setContent(grid);

		//
		// Result
		//

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

				screeningParameter
						.setMinAnnualInterestRate(toDoubleOrNull(minAnnualInterestRate));
				screeningParameter
						.setMaxAnnualInterestRate(toDoubleOrNull(maxAnnualInterestRate));
				screeningParameter.setMinPer(toDoubleOrNull(minPer));
				screeningParameter.setMaxPer(toDoubleOrNull(maxPer));
				screeningParameter.setMinPbr(toDoubleOrNull(minPbr));
				screeningParameter.setMaxPbr(toDoubleOrNull(maxPbr));
				screeningParameter.setMinEps(toDoubleOrNull(minEps));
				screeningParameter.setMaxEps(toDoubleOrNull(maxEps));
				screeningParameter.setMinBps(toDoubleOrNull(minBps));
				screeningParameter.setMaxBps(toDoubleOrNull(maxBps));
				screeningParameter.setMinRoe(toDoubleOrNull(minRoe));
				screeningParameter.setMaxRoe(toDoubleOrNull(maxRoe));
				screeningParameter
						.setMinMarketCapitalization(toDoubleOrNull(minMarketCapitalization));
				screeningParameter
						.setMaxMarketCapitalization(toDoubleOrNull(maxMarketCapitalization));
				screeningParameter
						.setMinAverageAnnualSalary(toDoubleOrNull(minAverageAnnualSalary));
				screeningParameter
						.setMaxAverageAnnualSalary(toDoubleOrNull(maxAverageAnnualSalary));
				screeningParameter
						.setMinAverageAge(toDoubleOrNull(minAverageAge));
				screeningParameter
						.setMaxAverageAge(toDoubleOrNull(maxAverageAge));

				return this.screeningParameter;
			}
			return null;
		});
	}

	private TextFormatter<Double> createDoubleFormatter() {
		DoubleStringConverter converter = new DoubleStringConverter();
		TextFormatter<Double> formatter = new TextFormatter<>(converter);
		return formatter;
	}

	private Double toDoubleOrNull(String s) {
		try {
			return (Double) (Double.parseDouble(s));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
