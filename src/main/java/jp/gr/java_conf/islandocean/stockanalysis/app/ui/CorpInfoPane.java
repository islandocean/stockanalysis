package jp.gr.java_conf.islandocean.stockanalysis.app.ui;

import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileRecord;

public class CorpInfoPane extends TitledPane {

	private GridPane grid;
	private CorpViewType corpViewType;
	private ResourceBundle resource;
	private DetailRecord detailRecord;
	private ProfileRecord profileRecord;

	public CorpInfoPane(CorpViewType corpViewType, ResourceBundle resource) {
		super();
		this.corpViewType = corpViewType;
		this.resource = resource;
		initialize();
	}

	private void initialize() {
		String title = "";
		switch (corpViewType) {
		case PRICE_INFO:
			title = resource.getString(MessageKey.PRICE_INFO);
			break;
		case REFERENCE_INFO:
			title = resource.getString(MessageKey.REFERENCE_INFO);
			break;
		case MARGIN_INFO:
			title = resource.getString(MessageKey.MARGIN_INFO);
			break;
		case PROFILE_INFO:
			title = resource.getString(MessageKey.PROFILE_INFO);
			break;
		}
		this.setText(title);

		updateView();
	}

	private void updateView() {
		grid = new GridPane();
		grid.setVgap(4);
		grid.setPadding(new Insets(5, 5, 5, 5));

		int row = 0;
		switch (corpViewType) {
		case PRICE_INFO:
			grid.add(new Label(resource.getString(MessageKey.STOCK_CODE)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.STOCK_CODE)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.MARKET)), 0, row);
			grid.add(new Label(getDetailString(DetailEnum.MARKET)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.STOCK_NAME)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.STOCK_NAME)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.SECTOR)), 0, row);
			grid.add(new Label(getDetailString(DetailEnum.SECTOR)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.REALTIME_PRICE)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.REALTIME_PRICE)), 1,
					row);

			++row;
			grid.add(
					new Label(
							resource.getString(MessageKey.PRICE_COMPARISON_WITH_PREVIOUS_DAY)),
					0, row);
			grid.add(
					new Label(
							getDetailString(DetailEnum.PRICE_COMPARISON_WITH_PREVIOUS_DAY)),
					1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.PREVIOUS_CLOSING_PRICE)), 0,
					row);
			grid.add(new Label(
					getDetailString(DetailEnum.PREVIOUS_CLOSING_PRICE)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.OPENING_PRICE)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.OPENING_PRICE)), 1,
					row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.HIGH_PRICE)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.HIGH_PRICE)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.LOW_PRICE)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.LOW_PRICE)), 1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.TRADING_VOLUME_OF_STOCKS)),
					0, row);
			grid.add(new Label(
					getDetailString(DetailEnum.TRADING_VOLUME_OF_STOCKS)), 1,
					row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.TRADING_VALUE_OF_MONEY)), 0,
					row);
			grid.add(new Label(
					getDetailString(DetailEnum.TRADING_VALUE_OF_MONEY)), 1, row);

			++row;
			grid.add(
					new Label(resource.getString(MessageKey.HIGH_PRICE_LIMIT)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.HIGH_PRICE_LIMIT)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.LOW_PRICE_LIMIT)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.LOW_PRICE_LIMIT)), 1,
					row);
			break;
		case REFERENCE_INFO:
			break;
		case MARGIN_INFO:
			break;
		case PROFILE_INFO:
			grid.add(new Label(resource.getString(MessageKey.STOCK_CODE)), 0,
					row);
			grid.add(new Label(getProfileString(ProfileEnum.STOCK_CODE)), 1,
					row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.STOCK_NAME)), 0,
					row);
			grid.add(new Label(getProfileString(ProfileEnum.STOCK_NAME)), 1,
					row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.FEATURE)), 0, row);
			grid.add(new Label(getProfileString(ProfileEnum.FEATURE)), 1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.CONSOLIDATED_OPERATIONS)), 0,
					row);
			grid.add(new Label(
					getProfileString(ProfileEnum.CONSOLIDATED_OPERATIONS)), 1,
					row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.LOCATION_OF_HEAD_OFFICE)), 0,
					row);
			grid.add(new Label(
					getProfileString(ProfileEnum.LOCATION_OF_HEAD_OFFICE)), 1,
					row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.NEAREST_STATION)),
					0, row);
			grid.add(new Label(getProfileString(ProfileEnum.NEAREST_STATION)),
					1, row);

			++row;
			grid.add(
					new Label(resource.getString(MessageKey.TELEPHONE_NUMBER)),
					0, row);
			grid.add(new Label(getProfileString(ProfileEnum.TELEPHONE_NUMBER)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.SECTOR)), 0, row);
			grid.add(new Label(getProfileString(ProfileEnum.SECTOR)), 1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.STOCK_NAME_IN_ENGLISH)), 0,
					row);
			grid.add(new Label(
					getProfileString(ProfileEnum.STOCK_NAME_IN_ENGLISH)), 1,
					row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.REPRESENTATIVE)),
					0, row);
			grid.add(new Label(getProfileString(ProfileEnum.REPRESENTATIVE)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.FOUNDATION_DATE)),
					0, row);
			grid.add(new Label(getProfileString(ProfileEnum.FOUNDATION_DATE)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.MARKET)), 0, row);
			grid.add(new Label(getProfileString(ProfileEnum.MARKET)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.LISTED_DATE)), 0,
					row);
			grid.add(new Label(getProfileString(ProfileEnum.LISTED_DATE)), 1,
					row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.SETTLING_DATE)),
					0, row);
			grid.add(new Label(getProfileString(ProfileEnum.SETTLING_DATE)), 1,
					row);

			++row;
			grid.add(
					new Label(resource.getString(MessageKey.SHARE_UNIT_NUMBER)),
					0, row);
			grid.add(
					new Label(getProfileString(ProfileEnum.SHARE_UNIT_NUMBER)),
					1, row);

			++row;
			grid.add(
					new Label(
							resource.getString(MessageKey.NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES)),
					0, row);
			grid.add(
					new Label(
							getProfileString(ProfileEnum.NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES)),
					1, row);

			++row;
			grid.add(
					new Label(
							resource.getString(MessageKey.CONSOLIDATED_NUMBER_OF_EMPLOYEES)),
					0, row);
			grid.add(
					new Label(
							getProfileString(ProfileEnum.CONSOLIDATED_NUMBER_OF_EMPLOYEES)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.AVERAGE_AGE)), 0,
					row);
			grid.add(new Label(getProfileString(ProfileEnum.AVERAGE_AGE)), 1,
					row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.AVERAGE_ANNUAL_SALARY)), 0,
					row);
			grid.add(new Label(
					getProfileString(ProfileEnum.AVERAGE_ANNUAL_SALARY)), 1,
					row);

			break;
		}

		this.setContent(grid);
	}

	private String getDetailString(DetailEnum e) {
		if (this.detailRecord == null) {
			return "";
		}
		Object obj = this.detailRecord.get(e);
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	private String getProfileString(ProfileEnum e) {
		if (this.profileRecord == null) {
			return "";
		}
		Object obj = this.profileRecord.get(e);
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	public void setDetailRecord(DetailRecord detailRecord) {
		this.detailRecord = detailRecord;
		updateView();
	}

	public void setProfileRecord(ProfileRecord profileRecord) {
		this.profileRecord = profileRecord;
		updateView();
	}
}
