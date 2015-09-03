package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class CorpInfoPane extends TitledPane {

	private static final NumberFormat numberFormat = NumberFormat
			.getNumberInstance();
	private static final double HEIGHT_DUAL = 40;
	private static final double HEIGHT_MIDDLE = 64;
	private static final double HEIGHT_LARGE = 88;

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
		case FUND_REFERENCE_INFO:
			title = resource.getString(MessageKey.FUND_REFERENCE_INFO);
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
		grid.setVgap(1);
		grid.setPadding(new Insets(2, 2, 2, 2));

		int col1MinWidth = 100;
		int col2MinWidth = 100;
		switch (corpViewType) {
		case PRICE_INFO:
			break;
		case REFERENCE_INFO:
			break;
		case FUND_REFERENCE_INFO:
			break;
		case MARGIN_INFO:
			break;
		case PROFILE_INFO:
			col1MinWidth = 120;
			col2MinWidth = 130;
			break;
		}
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setMinWidth(col1MinWidth);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setMinWidth(col2MinWidth);
		grid.getColumnConstraints().addAll(col1, col2);

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
			grid.add(new Label(resource.getString(MessageKey.STOCK_PRICE)),
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
			grid.add(
					new Label(resource
							.getString(MessageKey.MARKET_CAPITALIZATION)), 0,
					row);
			grid.add(new Label(
					getDetailString(DetailEnum.MARKET_CAPITALIZATION)), 1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.OUTSTANDING_STOCK_VOLUME)),
					0, row);
			grid.add(new Label(
					getDetailString(DetailEnum.OUTSTANDING_STOCK_VOLUME)), 1,
					row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.ANNUAL_INTEREST_RATE)), 0,
					row);
			grid.add(
					new Label(getDetailString(DetailEnum.ANNUAL_INTEREST_RATE)),
					1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.DIVIDENDS_PER_SHARE)), 0, row);
			grid.add(
					new Label(getDetailString(DetailEnum.DIVIDENDS_PER_SHARE)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.PER)), 0, row);
			grid.add(new Label(getDetailString(DetailEnum.PER)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.PBR)), 0, row);
			grid.add(new Label(getDetailString(DetailEnum.PBR)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.EPS)), 0, row);
			grid.add(new Label(getDetailString(DetailEnum.EPS)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.BPS)), 0, row);
			grid.add(new Label(getDetailString(DetailEnum.BPS)), 1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.MINIMUM_PURCHASE_AMOUNT)), 0,
					row);
			grid.add(new Label(
					getDetailString(DetailEnum.MINIMUM_PURCHASE_AMOUNT)), 1,
					row);

			++row;
			grid.add(
					new Label(resource.getString(MessageKey.SHARE_UNIT_NUMBER)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.SHARE_UNIT_NUMBER)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.YEARLY_HIGH)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.YEARLY_HIGH)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.YEARLY_LOW)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.YEARLY_LOW)), 1, row);

			break;

		case FUND_REFERENCE_INFO:
			grid.add(new Label(resource.getString(MessageKey.NET_ASSETS)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.NET_ASSETS)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.UNIT_OF_TRADING)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.UNIT_OF_TRADING)), 1,
					row);

			++row;
			grid.add(
					new Label(resource.getString(MessageKey.MANAGEMENT_COMPANY)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.MANAGEMENT_COMPANY)),
					1, row);

			++row;
			grid.add(
					new Label(
							resource.getString(MessageKey.TYPE_OF_ASSETS_TO_BE_INVESTED)),
					0, row);
			grid.add(new Label(
					getDetailString(DetailEnum.TYPE_OF_ASSETS_TO_BE_INVESTED)),
					1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.REGION_TO_BE_INVESTED)), 0,
					row);
			grid.add(new Label(
					getDetailString(DetailEnum.REGION_TO_BE_INVESTED)), 1, row);

			++row;
			grid.add(
					new Label(resource.getString(MessageKey.UNDERLYING_INDEX)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.UNDERLYING_INDEX)),
					1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.SETTLEMENT_FREQUENCY)), 0,
					row);
			grid.add(
					new Label(getDetailString(DetailEnum.SETTLEMENT_FREQUENCY)),
					1, row);

			++row;
			grid.add(
					new Label(resource.getString(MessageKey.SETTLEMENT_MONTH)),
					0, row);
			grid.add(new Label(getDetailString(DetailEnum.SETTLEMENT_MONTH)),
					1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.LISTED_DATE)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.LISTED_DATE)), 1, row);

			++row;
			grid.add(new Label(resource.getString(MessageKey.TRUST_FEE)), 0,
					row);
			grid.add(new Label(getDetailString(DetailEnum.TRUST_FEE)), 1, row);

			break;

		case MARGIN_INFO:
			grid.add(
					new Label(resource
							.getString(MessageKey.MARGIN_DEBT_BALANCE)), 0, row);
			grid.add(
					new Label(getDetailString(DetailEnum.MARGIN_DEBT_BALANCE)),
					1, row);

			++row;
			grid.add(
					new Label(
							resource.getString(MessageKey.MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK)),
					0, row);
			grid.add(
					new Label(
							getDetailString(DetailEnum.MARGIN_DEBT_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK)),
					1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.MARGIN_SELLING_BALANCE)), 0,
					row);
			grid.add(new Label(
					getDetailString(DetailEnum.MARGIN_SELLING_BALANCE)), 1, row);

			++row;
			grid.add(
					new Label(
							resource.getString(MessageKey.MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK)),
					0, row);
			grid.add(
					new Label(
							getDetailString(DetailEnum.MARGIN_SELLING_BALANCE_RATIO_COMPARISON_WITH_PREVIOUS_WEEK)),
					1, row);

			++row;
			grid.add(
					new Label(resource
							.getString(MessageKey.RATIO_OF_MARGIN_BALANCE)), 0,
					row);
			grid.add(new Label(
					getDetailString(DetailEnum.RATIO_OF_MARGIN_BALANCE)), 1,
					row);

			break;

		case PROFILE_INFO:
			grid.add(new Label(resource.getString(MessageKey.STOCK_NAME)), 0,
					row);
			grid.add(new Label(getProfileString(ProfileEnum.STOCK_NAME)), 1,
					row);

			++row;
			Label featureValue = new Label(
					getProfileString(ProfileEnum.FEATURE));
			featureValue.wrapTextProperty().set(true);
			featureValue.setMinHeight(HEIGHT_LARGE);
			grid.add(new Label(resource.getString(MessageKey.FEATURE)), 0, row);
			grid.add(featureValue, 1, row);

			++row;
			Label consolidatedOperationsCaption = new Label(
					resource.getString(MessageKey.CONSOLIDATED_OPERATIONS));
			consolidatedOperationsCaption.wrapTextProperty().set(true);
			consolidatedOperationsCaption.setMinHeight(HEIGHT_LARGE);
			Label consolidatedOperationsValue = new Label(
					getProfileString(ProfileEnum.CONSOLIDATED_OPERATIONS));
			consolidatedOperationsValue.wrapTextProperty().set(true);
			consolidatedOperationsValue.setMinHeight(HEIGHT_LARGE);
			grid.add(consolidatedOperationsCaption, 0, row);
			grid.add(consolidatedOperationsValue, 1, row);

			++row;
			Label headOfficeCaption = new Label(
					resource.getString(MessageKey.LOCATION_OF_HEAD_OFFICE));
			headOfficeCaption.wrapTextProperty().set(true);
			headOfficeCaption.setMinHeight(HEIGHT_MIDDLE);
			Label headOfficeValue = new Label(
					getProfileString(ProfileEnum.LOCATION_OF_HEAD_OFFICE));
			headOfficeValue.wrapTextProperty().set(true);
			headOfficeValue.setMinHeight(HEIGHT_MIDDLE);
			grid.add(headOfficeCaption, 0, row);
			grid.add(headOfficeValue, 1, row);

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
			String stockNameInEnglish = Normalizer.normalize(
					getProfileString(ProfileEnum.STOCK_NAME_IN_ENGLISH),
					Normalizer.Form.NFKC);
			Label stockNameInEnglishCaption = new Label(
					resource.getString(MessageKey.STOCK_NAME_IN_ENGLISH));
			stockNameInEnglishCaption.wrapTextProperty().set(true);
			stockNameInEnglishCaption.setMinHeight(HEIGHT_MIDDLE);
			Label stockNameInEnglishValue = new Label(stockNameInEnglish);
			stockNameInEnglishValue.wrapTextProperty().set(true);
			stockNameInEnglishValue.setMinHeight(HEIGHT_MIDDLE);
			grid.add(stockNameInEnglishCaption, 0, row);
			grid.add(stockNameInEnglishValue, 1, row);

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
			Label nonConsolidatedNumberOfEmployeesCaption = new Label(
					resource.getString(MessageKey.NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES));
			nonConsolidatedNumberOfEmployeesCaption.wrapTextProperty()
					.set(true);
			nonConsolidatedNumberOfEmployeesCaption.setMinHeight(HEIGHT_DUAL);
			grid.add(nonConsolidatedNumberOfEmployeesCaption, 0, row);
			grid.add(
					new Label(
							getProfileString(ProfileEnum.NON_CONSOLIDATED_NUMBER_OF_EMPLOYEES)),
					1, row);

			++row;
			Label consolidatedNumberOfEmployeesCaption = new Label(
					resource.getString(MessageKey.CONSOLIDATED_NUMBER_OF_EMPLOYEES));
			consolidatedNumberOfEmployeesCaption.wrapTextProperty().set(true);
			consolidatedNumberOfEmployeesCaption.setMinHeight(HEIGHT_DUAL);
			grid.add(consolidatedNumberOfEmployeesCaption, 0, row);
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
		return formatObj(obj);
	}

	private String getProfileString(ProfileEnum e) {
		if (this.profileRecord == null) {
			return "";
		}
		Object obj = this.profileRecord.get(e);
		return formatObj(obj);
	}

	private String formatObj(Object obj) {
		if (obj == null) {
			return "";
		}
		if (obj instanceof Calendar) {
			Calendar cal = (Calendar) obj;
			return CalendarUtil.format_yyyyMMdd(cal);
		}
		if (obj instanceof Double) {
			double d = ((Double) obj).doubleValue();
			String s = numberFormat.format(d);
			return s;
		}
		if (obj instanceof Long) {
			long l = ((Long) obj).longValue();
			String s = numberFormat.format(l);
			return s;
		}
		if (obj instanceof Integer) {
			int i = ((Integer) obj).intValue();
			String s = numberFormat.format(i);
			return s;
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
