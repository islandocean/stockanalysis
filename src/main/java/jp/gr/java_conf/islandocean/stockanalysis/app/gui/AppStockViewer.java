package jp.gr.java_conf.islandocean.stockanalysis.app.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.gr.java_conf.islandocean.stockanalysis.app.CorpsAllData;
import jp.gr.java_conf.islandocean.stockanalysis.app.CorpsScannerTemplate;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.finance.MarketUtil;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileEnum;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.SectorUtil;
import jp.gr.java_conf.islandocean.stockanalysis.finance.StockSplitInfo;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;
import jp.gr.java_conf.islandocean.stockanalysis.util.Util;

public class AppStockViewer extends Application implements CorpsScannerTemplate {

	//
	private static final String PREFKEY_LOCALE = "LOCALE";
	private static final String PREFKEY_REGISTERED_STOCKS_ = "REGISTERED_STOCKS_";
	private static final String PREFKEY_SELECTED_REGISTERED_STOCKS_INDEX = "SELECTED_REGISTERED_STOCKS_INDEX";
	private static final String PREFKEY_CURRENT_SCREENING_PARAMETERS = "CURRENT_SCREENING_PARAMETERS";

	private static final int NUM_REGISTERED_STOCKS = 10;
	private static final int HISTORY_SIZE = 10;

	private static final double DEFAULT_SCENE_WIDTH = 1230d;
	private static final double DEFAULT_SCENE_HEIGHT = 870d;
	private static final double ALL_STOCKS_TREEVIEW_MIN_HEIGHT = 680d;
	private static final double REGISTERED_STOCKS_TREEVIEW_MIN_HEIGHT = 654d;
	private static final double SEARCH_TEXT_FIELD_MIN_WIDTH = 140d;
	private static final double TABLE_CONTROL_PANE_MAX_HEIGHT = 50d;
	private static final double TABLE_CONTENT_PANE_MIN_HEIGHT = 700d;
	private static final double TABLE_VIEW_MIN_HEIGHT = 700d;
	private static final double TABLE_STOCK_CODE_COLUMN_MAX_WIDTH = 56d;
	private static final double TABLE_STOCK_NAME_COLUMN_MIN_WIDTH = 180d;

	//
	// Data
	//
	private CorpsAllData allData;
	private StockManager stockManager;
	private LinkedHashMap<String, List<StockRecord>> corpDataListInCodeMap;
	private FinanceManager financeManager;
	private List<StockRecord> lastData;
	private String[] stockCodes;
	private Map stockCodeToDetailRecordMap;
	private Map stockCodeToProfileRecordMap;

	private ResourceBundle resource;
	private Pref pref;
	private Locale appLocale;
	private String[] registeredStocksPrefStrs;
	private LinkedHashSet<String>[] registeredStockSets;
	private String rightPaneStockCode;

	private List<History> historyList;
	private int currentHistoryIdx;

	//
	// Controls
	//

	private Stage primaryStage;

	// Pane
	private VBox rootPane;
	private HBox topPane;
	private VBox leftPane1;
	private VBox leftPane2;
	private SplitPane centerPane;
	private VBox centerContentPane;
	private VBox rightPane1;
	private VBox rightPane2;
	private SplitPane middlePane;
	private HBox bottomPane;

	// Menu
	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem ExitMenu;
	private Menu viewMenu;
	private Menu languageMenu;
	private MenuItem japaneseMenu;
	private MenuItem englishMenu;
	private Menu toolMenu;
	private MenuItem optionMenu;
	private Menu helpMenu;
	private MenuItem aboutMenu;

	// All Stocks
	private VBox allStocksContent;
	private Label allStocksTitleLabel;
	private HBox allStocksControlPane;
	private Button allStocksTreeCollapseButton;
	private Button allStocksTreeExpandButton;
	private TreeView<Object> allStocksTreeView;
	private TreeItem<Object> allStocksRootItem;

	// Registered Stocks
	private VBox registeredStocksContent;
	private Label registeredStocksTitleLabel;
	private GridPane registeredStocksControlPane;
	private ToggleGroup toggleGroup;
	int selectedToggleIdx;
	private ToggleButton[] toggleButtons;
	private TreeView<Object>[] registeredStocksTreeViews;
	private TreeItem<Object>[] registeredStocksRootItems;

	// Search
	private TextField searchTextField;
	private Button searchButton;
	private Button screeningButton;

	// History
	private Button backButton;
	private Button forwardButton;

	// Table
	private HBox tableControlPane;
	private TableView<TableStockData> stockTableView;
	private TableColumn stockTableStockCodeColumn;
	private TableColumn stockTableStockNameColumn;
	private TableColumn stockTableMarketColumn;
	private TableColumn stockTableSectorColumn;
	private TableColumn stockTableAnnualInterestRateColumn;
	private TableColumn stockTablePerColumn;
	private TableColumn stockTablePbrColumn;
	private TableColumn stockTableEpsColumn;
	private TableColumn stockTableBpsColumn;
	private TableColumn stockTableRoeColumn;
	private TableColumn stockTableMarketCapitalizationColumn;
	private TableColumn stockTableAverageAnnualSalaryColumn;
	private TableColumn stockTableAverageAgeColumn;

	private TextArea consoleTextArea;
	private Button updateButton;
	private Button getButton;
	private Button clearButton;

	// Corp Info
	private Accordion priceInfoAccordion;
	private CorpInfoPane priceInfoPane;

	private Accordion referenceInfoAccordion;
	private CorpInfoPane referenceInfoPane;

	private Accordion fundReferenceInfoAccordion;
	private CorpInfoPane fundReferenceInfoPane;

	private Accordion marginInfoAccordion;
	private CorpInfoPane marginInfoPane;

	private Accordion profileInfoAccordion;
	private CorpInfoPane profileInfoPane;

	public AppStockViewer() {
	}

	public DataStore selectDataStore() {
		DataStore store = new DataStoreKdb();
		return store;
	}

	public CalendarRange selectCalendarRange() {
		return CalendarUtil.createCalendarRangeRecentDays(30);
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		this.primaryStage = stage;

		// Initialize pref
		loadPref();

		// Initialize resource bundle
		initResource();

		// Initialize corp data
		scanInit();

		// Initialize history
		initHistory();

		// Build UI
		buildUi(stage); // Calls scanMain();

		// Empty table, and add one history.
		ObservableList tableStockDataList = beforeUpdateTableStockDataList(true);
		afterUpdateTableStockDataList(tableStockDataList, this.stockTableView);
	}

	private void initResource() {
		resource = ResourceBundle.getBundle("resources",
				ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL);
	}

	private void loadPref() {
		pref = new Pref(AppStockViewer.class);

		appLocale = null;
		String localeStr = (String) pref.getProperty(PREFKEY_LOCALE);
		if (localeStr != null) {
			String[] ar = localeStr.split("_");
			switch (ar.length) {
			case 1:
				appLocale = new Locale(ar[0]);
				break;
			case 2:
				appLocale = new Locale(ar[0], ar[1]);
				break;
			case 3:
				appLocale = new Locale(ar[0], ar[1], ar[2]);
				break;
			default:
				break;
			}
		}
		if (appLocale == null) {
			appLocale = Locale.JAPAN;
		}
		Locale.setDefault(appLocale);

		registeredStocksPrefStrs = new String[NUM_REGISTERED_STOCKS];

		for (int i = 0; i < NUM_REGISTERED_STOCKS; ++i) {
			registeredStocksPrefStrs[i] = (String) pref
					.getProperty(PREFKEY_REGISTERED_STOCKS_ + i);
		}
		registeredStockSets = new LinkedHashSet[NUM_REGISTERED_STOCKS];
		for (int idxList = 0; idxList < NUM_REGISTERED_STOCKS; ++idxList) {
			registeredStockSets[idxList] = new LinkedHashSet();
			String s = registeredStocksPrefStrs[idxList];
			if (s != null && s.length() > 0) {
				String[] codes = s.split(",");
				for (int idxCodes = 0; idxCodes < codes.length; ++idxCodes) {
					registeredStockSets[idxList].add(codes[idxCodes]);
				}
			}
		}
	}

	private boolean isEnglish() {
		return Locale.ENGLISH.equals(appLocale);
	}

	private void savePref() {
		try {
			pref.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void scanInit() {
		boolean useStockPrice = true;
		boolean useDetailInfo = true;
		boolean useProfileInfo = true;

		// Initialize
		try {
			allData = initializeCorpsAllData(useStockPrice, selectDataStore(),
					selectCalendarRange(), useDetailInfo, useProfileInfo);
		} catch (IOException | InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		// Save to reuse
		this.stockManager = allData.getStockManager();
		this.stockManager.generateCorpDataListInCodeMap();
		this.corpDataListInCodeMap = stockManager.getCorpDataListInCodeMap();
		this.financeManager = allData.getFinanceManager();
		this.lastData = allData.getLastData();
		this.stockCodes = allData.getStockCodes();
		this.stockCodeToDetailRecordMap = financeManager
				.getStockCodeToDetailRecordMap();
		this.stockCodeToProfileRecordMap = financeManager
				.getStockCodeToProfileRecordMap();
	}

	private void initHistory() {
		historyList = new ArrayList();
		currentHistoryIdx = -1;
	}

	private void buildUi(Stage stage) {

		//
		// Tree items
		//

		// Create all stocks root item.
		allStocksRootItem = new TreeItem<Object>(new RootItemValue(
				resource.getString(MessageKey.ALL_MARKETS_TREEITEM)));
		allStocksRootItem.setExpanded(true);

		// Create registered stocks root item.
		registeredStocksRootItems = new TreeItem[NUM_REGISTERED_STOCKS];
		for (int i = 0; i < NUM_REGISTERED_STOCKS; ++i) {
			registeredStocksRootItems[i] = new TreeItem<Object>(
					new RootItemValue(resource
							.getString(MessageKey.REGISTERED_STOCKS_TREEITEM)
							+ (i + 1)));
			registeredStocksRootItems[i].setExpanded(true);
		}

		//
		// Add tree items.
		//
		scanMain();

		// Sort tree by market and sector, and set captions of tree item.
		sortAllTreesAndSetCaptions();

		//
		// GUI Parts
		//

		// All stocks tree view
		allStocksTreeView = new TreeView<Object>(allStocksRootItem);
		allStocksTreeView.setMinHeight(ALL_STOCKS_TREEVIEW_MIN_HEIGHT);
		allStocksTreeView
				.setOnMouseClicked(createTreeMouseEventHandler(allStocksTreeView));
		allStocksTreeView.getSelectionModel().selectedItemProperty()
				.addListener(createTreeChangeListener());
		allStocksTreeView.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		allStocksTreeView.setContextMenu(new ContextMenu(
				createAllStocksTreeContextMenuContents()));

		// All Stocks tree controls
		allStocksControlPane = new HBox();

		allStocksTreeExpandButton = new Button(
				resource.getString(MessageKey.EXPAND_BUTTON));
		allStocksTreeExpandButton.setOnAction((ActionEvent e) -> {
			allStocksRootItem.setExpanded(true);
			allStocksRootItem.getChildren().forEach(market -> {
				((TreeItem) market).setExpanded(true);
			});
		});

		allStocksTreeCollapseButton = new Button(
				resource.getString(MessageKey.COLLAPSE_BUTTON));
		allStocksTreeCollapseButton.setOnAction((ActionEvent e) -> {
			allStocksRootItem.getChildren().forEach(market -> {
				((TreeItem) market).setExpanded(false);
			});
		});

		allStocksControlPane.getChildren().addAll(allStocksTreeExpandButton,
				allStocksTreeCollapseButton);
		allStocksControlPane.setSpacing(10);
		allStocksControlPane.setAlignment(Pos.CENTER_LEFT);
		allStocksControlPane.setPadding(new Insets(10, 10, 10, 10));

		// All stocks content
		allStocksContent = new VBox();
		allStocksContent.setPadding(new Insets(10, 0, 0, 10));
		allStocksContent.setMinWidth(0d);
		allStocksTitleLabel = new Label(
				resource.getString(MessageKey.ALL_STOCKS_TITLE_LABEL));
		allStocksContent.getChildren().addAll(allStocksTitleLabel,
				allStocksControlPane, allStocksTreeView);

		// Registered stocks tree view
		registeredStocksTreeViews = new TreeView[NUM_REGISTERED_STOCKS];
		for (int i = 0; i < NUM_REGISTERED_STOCKS; ++i) {
			registeredStocksTreeViews[i] = new TreeView<Object>(
					registeredStocksRootItems[i]);
			registeredStocksTreeViews[i]
					.setOnMouseClicked(createTreeMouseEventHandler(registeredStocksTreeViews[i]));
			registeredStocksTreeViews[i].getSelectionModel()
					.selectedItemProperty()
					.addListener(createTreeChangeListener());
			registeredStocksTreeViews[i].getSelectionModel().setSelectionMode(
					SelectionMode.MULTIPLE);
			registeredStocksTreeViews[i].setContextMenu(new ContextMenu(
					createRegisteredStocksTreeContextMenuContents(i)));
			registeredStocksTreeViews[i]
					.setMinHeight(REGISTERED_STOCKS_TREEVIEW_MIN_HEIGHT);
		}

		// Registered stocks tree controls
		registeredStocksControlPane = new GridPane();
		toggleGroup = new ToggleGroup();
		toggleButtons = new ToggleButton[NUM_REGISTERED_STOCKS];
		for (int i = 0; i < NUM_REGISTERED_STOCKS; ++i) {
			toggleButtons[i] = new ToggleButton();
			toggleButtons[i].setText(Integer.toString(i + 1));
			toggleButtons[i].setToggleGroup(toggleGroup);
			toggleButtons[i].setMinWidth(28d);
			toggleButtons[i].setOnAction((ActionEvent e) -> {
				String text = ((ToggleButton) e.getSource()).getText();
				selectedToggleIdx = Integer.parseInt(text) - 1;
				registeredStocksContent.getChildren().clear();
				registeredStocksContent.getChildren().addAll(
						registeredStocksTitleLabel,
						registeredStocksControlPane,
						registeredStocksTreeViews[selectedToggleIdx]);

				pref.setProperty(PREFKEY_SELECTED_REGISTERED_STOCKS_INDEX,
						String.valueOf(selectedToggleIdx));
				try {
					pref.save();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			registeredStocksControlPane.add(toggleButtons[i], i % 5, i / 5);
		}
		String selectedStr = (String) pref
				.getProperty(PREFKEY_SELECTED_REGISTERED_STOCKS_INDEX);
		selectedToggleIdx = 0;
		if (selectedStr != null) {
			selectedToggleIdx = Integer.parseInt(selectedStr);
		}
		toggleGroup.selectToggle(toggleButtons[selectedToggleIdx]);

		// registeredStocksControlPane.setSpacing(2);
		registeredStocksControlPane.setVgap(4d);
		registeredStocksControlPane.setHgap(2d);
		registeredStocksControlPane.setAlignment(Pos.CENTER_LEFT);
		registeredStocksControlPane.setPadding(new Insets(10, 0, 10, 0));

		// Registered stocks content
		registeredStocksContent = new VBox();
		registeredStocksContent.setPadding(new Insets(10, 0, 0, 10));
		registeredStocksContent.setMinWidth(0d);
		registeredStocksTitleLabel = new Label(
				resource.getString(MessageKey.REGISTERED_STOCKS_TITLE_LABEL));
		registeredStocksContent.getChildren().addAll(
				registeredStocksTitleLabel, registeredStocksControlPane,
				registeredStocksTreeViews[selectedToggleIdx]);

		// Table controls
		tableControlPane = new HBox();
		searchTextField = new TextField();
		searchTextField.setPromptText(resource
				.getString(MessageKey.SEARCH_PROMPT_TEXT));
		searchTextField.setMinWidth(SEARCH_TEXT_FIELD_MIN_WIDTH);
		searchButton = new Button(resource.getString(MessageKey.SEARCH_BUTTON));
		searchButton.setOnAction((ActionEvent e) -> {
			String text = searchTextField.getText();
			if (text != null && text.length() >= 1) {
				searchCorps(text);
			}
		});
		screeningButton = new Button(
				resource.getString(MessageKey.SCREENING_BUTTON));
		screeningButton.setOnAction((ActionEvent e) -> {
			ScreeningParameter screeningParameter = null;
			String str = (String) pref
					.getProperty(PREFKEY_CURRENT_SCREENING_PARAMETERS);
			if (str != null) {
				try {
					screeningParameter = (ScreeningParameter) Util
							.deserializeHexToObj(str);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (screeningParameter == null) {
			screeningParameter = new ScreeningParameter();
		}
		ScreeningDialog dialog = new ScreeningDialog(resource,
				screeningParameter);
		Optional<ScreeningParameter> result = dialog.showAndWait();
		boolean execute = dialog.getExecute();
		result.ifPresent(ret -> {
			executeScreening(ret, execute);
		});
	})	;

		backButton = new Button(resource.getString(MessageKey.BACK_BUTTON));
		backButton.setOnAction(createHistoryBackEventHandler());
		forwardButton = new Button(
				resource.getString(MessageKey.FORWARD_BUTTON));
		forwardButton.setOnAction(createHistoryForwardEventHandler());
		updateHistoryButtonsStatus();

		tableControlPane.getChildren().addAll(backButton, forwardButton,
				new Label(" "), searchTextField, searchButton, new Label(" "),
				screeningButton);
		tableControlPane.setSpacing(10);
		tableControlPane.setAlignment(Pos.CENTER_LEFT);
		tableControlPane.setPadding(new Insets(10, 10, 10, 10));
		tableControlPane.setMaxHeight(TABLE_CONTROL_PANE_MAX_HEIGHT);

		// Table view
		buildStockTableView();

		// Center content pane
		centerContentPane = new VBox();
		centerContentPane.setMinHeight(TABLE_CONTENT_PANE_MIN_HEIGHT);

		// Corp info accordion
		priceInfoAccordion = new Accordion();
		priceInfoPane = new CorpInfoPane(CorpViewType.PRICE_INFO, resource);
		priceInfoAccordion.getPanes().addAll(priceInfoPane);
		priceInfoAccordion.setExpandedPane(priceInfoPane);

		referenceInfoAccordion = new Accordion();
		referenceInfoPane = new CorpInfoPane(CorpViewType.REFERENCE_INFO,
				resource);
		referenceInfoAccordion.getPanes().addAll(referenceInfoPane);
		referenceInfoAccordion.setExpandedPane(referenceInfoPane);

		fundReferenceInfoAccordion = new Accordion();
		fundReferenceInfoPane = new CorpInfoPane(
				CorpViewType.FUND_REFERENCE_INFO, resource);
		fundReferenceInfoAccordion.getPanes().addAll(fundReferenceInfoPane);

		marginInfoAccordion = new Accordion();
		marginInfoPane = new CorpInfoPane(CorpViewType.MARGIN_INFO, resource);
		marginInfoAccordion.getPanes().addAll(marginInfoPane);
		marginInfoAccordion.setExpandedPane(marginInfoPane);

		profileInfoAccordion = new Accordion();
		profileInfoPane = new CorpInfoPane(CorpViewType.PROFILE_INFO, resource);
		profileInfoAccordion.getPanes().addAll(profileInfoPane);
		profileInfoAccordion.setExpandedPane(profileInfoPane);

		//
		// Layout
		//

		// Top
		topPane = new HBox();
		topPane.setSpacing(10);
		topPane.setAlignment(Pos.CENTER_LEFT);
		topPane.setPadding(new Insets(10, 10, 10, 10));
		createMenu();
		topPane.getChildren().addAll(menuBar);

		// left
		leftPane1 = allStocksContent;
		leftPane2 = registeredStocksContent;

		// center
		centerPane = new SplitPane();
		centerPane.setOrientation(Orientation.VERTICAL);
		centerPane.getItems().addAll(tableControlPane, centerContentPane);
		centerPane.setDividerPositions(0.33f, 0.66f, 1.0f);

		// right
		rightPane1 = new VBox();
		rightPane2 = new VBox();
		if (this.rightPaneStockCode != null) {
			reloadRightPane(this.rightPaneStockCode);
		}

		// middle = left + center + right
		middlePane = new SplitPane();
		middlePane.setOrientation(Orientation.HORIZONTAL);
		middlePane.getItems().addAll(leftPane1, leftPane2, centerPane,
				rightPane1, rightPane2);
		middlePane.setDividerPositions(0.14f, 0.28f, 0.64f, 0.82f, 1.0f);
		middlePane.setMinSize(600d, 735d);

		// Bottom
		bottomPane = new HBox();
		consoleTextArea = new TextArea();
		consoleTextArea.setMinSize(200d, 50d);
		consoleTextArea.setMaxSize(200d, 50d);
		consoleTextArea.setPromptText("");
		updateButton = new Button("Set to Table");
		updateButton.setOnAction((ActionEvent e) -> {
			updateTable();
		});
		getButton = new Button("Get from Table");
		getButton.setOnAction((ActionEvent e) -> {
			getFromTable();
		});
		clearButton = new Button("Clear");
		clearButton.setOnAction((ActionEvent e) -> {
			consoleTextArea.clear();
		});
		bottomPane.setSpacing(10);
		bottomPane.setAlignment(Pos.CENTER_LEFT);
		bottomPane.setPadding(new Insets(10, 10, 10, 10));
		bottomPane.getChildren().addAll(consoleTextArea, updateButton,
				getButton, clearButton);

		// root = top + middle + bottom
		rootPane = new VBox();
		rootPane.getChildren().addAll(topPane, middlePane, bottomPane);

		// stage
		stage.setTitle(resource.getString(MessageKey.STAGE_TITLE));
		stage.setScene(new Scene(rootPane, DEFAULT_SCENE_WIDTH,
				DEFAULT_SCENE_HEIGHT));
		stage.show();
	}

	private void buildStockTableView() {
		// Table View
		stockTableView = new TableView<TableStockData>();

		stockTableStockCodeColumn = new TableColumn(
				resource.getString(MessageKey.STOCK_CODE));
		stockTableStockCodeColumn
				.setMaxWidth(TABLE_STOCK_CODE_COLUMN_MAX_WIDTH);
		stockTableStockNameColumn = new TableColumn(
				resource.getString(MessageKey.STOCK_NAME));
		stockTableStockNameColumn
				.setMinWidth(TABLE_STOCK_NAME_COLUMN_MIN_WIDTH);
		stockTableMarketColumn = new TableColumn(
				resource.getString(MessageKey.MARKET));
		stockTableSectorColumn = new TableColumn(
				resource.getString(MessageKey.SECTOR));
		stockTableAnnualInterestRateColumn = new TableColumn(
				resource.getString(MessageKey.ANNUAL_INTEREST_RATE));
		stockTablePerColumn = new TableColumn(
				resource.getString(MessageKey.PER));
		stockTablePbrColumn = new TableColumn(
				resource.getString(MessageKey.PBR));
		stockTableEpsColumn = new TableColumn(
				resource.getString(MessageKey.EPS));
		stockTableBpsColumn = new TableColumn(
				resource.getString(MessageKey.BPS));
		stockTableRoeColumn = new TableColumn(
				resource.getString(MessageKey.ROE));
		stockTableMarketCapitalizationColumn = new TableColumn(
				resource.getString(MessageKey.MARKET_CAPITALIZATION));
		stockTableAverageAnnualSalaryColumn = new TableColumn(
				resource.getString(MessageKey.AVERAGE_ANNUAL_SALARY));
		stockTableAverageAgeColumn = new TableColumn(
				resource.getString(MessageKey.AVERAGE_AGE));

		stockTableStockCodeColumn
				.setCellValueFactory(new PropertyValueFactory<>("stockCode"));
		stockTableStockNameColumn
				.setCellValueFactory(new PropertyValueFactory<>("stockName"));
		stockTableMarketColumn.setCellValueFactory(new PropertyValueFactory<>(
				"market"));
		stockTableSectorColumn.setCellValueFactory(new PropertyValueFactory<>(
				"sector"));
		stockTableAnnualInterestRateColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"annualInterestRate"));
		stockTablePerColumn.setCellValueFactory(new PropertyValueFactory<>(
				"per"));
		stockTablePbrColumn.setCellValueFactory(new PropertyValueFactory<>(
				"pbr"));
		stockTableEpsColumn.setCellValueFactory(new PropertyValueFactory<>(
				"eps"));
		stockTableBpsColumn.setCellValueFactory(new PropertyValueFactory<>(
				"bps"));
		stockTableRoeColumn.setCellValueFactory(new PropertyValueFactory<>(
				"roe"));
		stockTableMarketCapitalizationColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"marketCapitalization"));
		stockTableAverageAnnualSalaryColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"averageAnnualSalary"));
		stockTableAverageAgeColumn
				.setCellValueFactory(new PropertyValueFactory<>("averageAge"));

		stockTableView.getColumns()
				.addAll(stockTableStockCodeColumn, stockTableStockNameColumn,
						stockTableMarketColumn, stockTableSectorColumn,
						stockTableAnnualInterestRateColumn,
						stockTablePerColumn, stockTablePbrColumn,
						stockTableEpsColumn, stockTableBpsColumn,
						stockTableRoeColumn,
						stockTableMarketCapitalizationColumn,
						stockTableAverageAnnualSalaryColumn,
						stockTableAverageAgeColumn);
		stockTableView.setPlaceholder(new Label(""));
		stockTableView.setMinHeight(TABLE_VIEW_MIN_HEIGHT);
		stockTableView.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		stockTableView.getSelectionModel().selectedItemProperty()
				.addListener(new TableChangeListener(stockTableView));
		stockTableView.setContextMenu(new ContextMenu(
				createTableContextMenuContents(stockTableView)));
	}

	private TableView buildScreeningTableView() {
		TableView tableView;

		// Table View
		tableView = new TableView<TableStockData>();

		TableColumn stockCodeColumn;
		TableColumn stockNameColumn;
		TableColumn marketColumn;
		TableColumn sectorColumn;
		TableColumn annualInterestRateColumn;
		TableColumn perColumn;
		TableColumn pbrColumn;
		TableColumn epsColumn;
		TableColumn bpsColumn;
		TableColumn roeColumn;
		TableColumn marketCapitalizationColumn;
		TableColumn averageAnnualSalaryColumn;
		TableColumn averageAgeColumn;

		stockCodeColumn = new TableColumn(
				resource.getString(MessageKey.STOCK_CODE));
		stockCodeColumn.setMaxWidth(TABLE_STOCK_CODE_COLUMN_MAX_WIDTH);
		stockNameColumn = new TableColumn(
				resource.getString(MessageKey.STOCK_NAME));
		stockNameColumn.setMinWidth(TABLE_STOCK_NAME_COLUMN_MIN_WIDTH);
		marketColumn = new TableColumn(resource.getString(MessageKey.MARKET));
		sectorColumn = new TableColumn(resource.getString(MessageKey.SECTOR));
		annualInterestRateColumn = new TableColumn(
				resource.getString(MessageKey.ANNUAL_INTEREST_RATE));
		perColumn = new TableColumn(resource.getString(MessageKey.PER));
		pbrColumn = new TableColumn(resource.getString(MessageKey.PBR));
		epsColumn = new TableColumn(resource.getString(MessageKey.EPS));
		bpsColumn = new TableColumn(resource.getString(MessageKey.BPS));
		roeColumn = new TableColumn(resource.getString(MessageKey.ROE));
		marketCapitalizationColumn = new TableColumn(
				resource.getString(MessageKey.MARKET_CAPITALIZATION));
		averageAnnualSalaryColumn = new TableColumn(
				resource.getString(MessageKey.AVERAGE_ANNUAL_SALARY));
		averageAgeColumn = new TableColumn(
				resource.getString(MessageKey.AVERAGE_AGE));

		stockCodeColumn.setCellValueFactory(new PropertyValueFactory<>(
				"stockCode"));
		stockNameColumn.setCellValueFactory(new PropertyValueFactory<>(
				"stockName"));
		marketColumn.setCellValueFactory(new PropertyValueFactory<>("market"));
		sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sector"));
		annualInterestRateColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"annualInterestRate"));
		perColumn.setCellValueFactory(new PropertyValueFactory<>("per"));
		pbrColumn.setCellValueFactory(new PropertyValueFactory<>("pbr"));
		epsColumn.setCellValueFactory(new PropertyValueFactory<>("eps"));
		bpsColumn.setCellValueFactory(new PropertyValueFactory<>("bps"));
		roeColumn.setCellValueFactory(new PropertyValueFactory<>("roe"));
		marketCapitalizationColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"marketCapitalization"));
		averageAnnualSalaryColumn
				.setCellValueFactory(new PropertyValueFactory<>(
						"averageAnnualSalary"));
		averageAgeColumn.setCellValueFactory(new PropertyValueFactory<>(
				"averageAge"));

		tableView.getColumns().addAll(stockCodeColumn, stockNameColumn,
				marketColumn, sectorColumn, annualInterestRateColumn,
				perColumn, pbrColumn, epsColumn, bpsColumn, roeColumn,
				marketCapitalizationColumn, averageAnnualSalaryColumn,
				averageAgeColumn);
		tableView.setPlaceholder(new Label(""));
		tableView.setMinHeight(TABLE_VIEW_MIN_HEIGHT);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.getSelectionModel().selectedItemProperty()
				.addListener(new TableChangeListener(tableView));
		tableView.setContextMenu(new ContextMenu(
				createTableContextMenuContents(tableView)));

		return tableView;
	}

	private void scanMain() {
		// Scan corps
		try {
			doScanCorps(allData);
		} catch (IOException | InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String[] doSelectCorps(StockManager stockManager,
			List<StockRecord> list, FinanceManager financeManager) {
		return stockManager.toStockCodeArray(list);
	}

	@Override
	public boolean doScanOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager) {

		boolean hit = false;
		for (StockRecord record : oneCorpRecords) {
			hit = true;

			// market
			String market = (String) record.get(StockEnum.MARKET);
			if (market == null || market.length() == 0) {
				market = "(none)";
			}

			// sector
			String sector = (String) record.get(StockEnum.SECTOR);
			if (sector == null || sector.length() == 0) {
				sector = "(none)";
			}

			// stock name
			String stockName = (String) record.get(StockEnum.STOCK_NAME);
			if (stockName == null || stockName.length() == 0) {
				stockName = "(none)";
			}

			// All stocks tree
			addItemToTree(allStocksRootItem, record, market, sector, stockName,
					false);

			// Registered stocks tree
			for (int idxList = 0; idxList < NUM_REGISTERED_STOCKS; ++idxList) {
				TreeItem<Object> rootItem = registeredStocksRootItems[idxList];
				Set<String> set = registeredStockSets[idxList];
				if (set.contains(stockCode)) {
					addItemToTree(rootItem, record, market, sector, stockName,
							true);
				}
			}

			break;
		}
		return hit;
	}

	@Override
	public void printHeader() {
	}

	@Override
	public void printFooter(int count) {
	}

	private void createMenu() {
		menuBar = new MenuBar();

		fileMenu = new Menu(resource.getString(MessageKey.FILE_MENU));
		ExitMenu = new MenuItem(resource.getString(MessageKey.EXIT_MENU));
		fileMenu.getItems().add(ExitMenu);
		ExitMenu.setOnAction(value -> {
			System.exit(0);
		});

		viewMenu = new Menu(resource.getString(MessageKey.VIEW_MENU));
		languageMenu = new Menu(resource.getString(MessageKey.LANGUAGE_MENU));
		japaneseMenu = new MenuItem(
				resource.getString(MessageKey.JAPANESE_MENU));
		englishMenu = new MenuItem(resource.getString(MessageKey.ENGLISH_MENU));
		languageMenu.getItems().addAll(japaneseMenu, englishMenu);
		viewMenu.getItems().add(languageMenu);
		japaneseMenu
				.setOnAction(value -> {
					appLocale = Locale.JAPAN;
					pref.setProperty(PREFKEY_LOCALE, appLocale.toString());
					savePref();
					Locale.setDefault(appLocale);
					initResource();
					initHistory();
					buildUi(this.primaryStage);
					ObservableList tableStockDataList = beforeUpdateTableStockDataList(true);
					afterUpdateTableStockDataList(tableStockDataList,
							this.stockTableView);
				});
		englishMenu
				.setOnAction(value -> {
					appLocale = Locale.ENGLISH;
					pref.setProperty(PREFKEY_LOCALE, appLocale.toString());
					savePref();
					Locale.setDefault(appLocale);
					initResource();
					initHistory();
					buildUi(this.primaryStage);
					ObservableList tableStockDataList = beforeUpdateTableStockDataList(true);
					afterUpdateTableStockDataList(tableStockDataList,
							this.stockTableView);
				});

		toolMenu = new Menu(resource.getString(MessageKey.TOOL_MENU));
		optionMenu = new MenuItem(resource.getString(MessageKey.OPTION_MENU));
		toolMenu.getItems().add(optionMenu);
		optionMenu.setDisable(true); //

		helpMenu = new Menu(resource.getString(MessageKey.HELP_MENU));
		aboutMenu = new MenuItem(resource.getString(MessageKey.ABOUT_MENU));
		helpMenu.getItems().add(aboutMenu);
		aboutMenu.setOnAction(value -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(resource.getString(MessageKey.ABOUT_MENU));
			alert.setHeaderText(resource
					.getString(MessageKey.ABOUT_DIALOG_HEADER_TEXT));
			alert.setContentText(resource
					.getString(MessageKey.ABOUT_DIALOG_CONTENT_TEXT));
			alert.showAndWait();
		});

		menuBar.getMenus().addAll(fileMenu, viewMenu, toolMenu, helpMenu);
	}

	private void addItemToTree(TreeItem<Object> rootItem, StockRecord record,
			String market, String sector, String stockName, boolean expandAll) {

		// market
		TreeItem<Object> currentMarketItem = null;
		boolean foundMarket = false;
		for (TreeItem<Object> oldMarketItem : rootItem.getChildren()) {
			if (((ItemValue) oldMarketItem.getValue()).getName().equals(market)) {
				currentMarketItem = oldMarketItem;
				foundMarket = true;
				break;
			}
		}
		if (!foundMarket) {
			TreeItem<Object> newMarketItem = new TreeItem<Object>(
					new MarketItemValue(market));
			newMarketItem.setExpanded(expandAll ? true : false);
			rootItem.getChildren().add(newMarketItem);
			currentMarketItem = newMarketItem;
		}

		// sector
		TreeItem<Object> currentSectorItem = null;
		boolean foundSector = false;
		for (TreeItem<Object> oldSectorItem : currentMarketItem.getChildren()) {
			if (((ItemValue) oldSectorItem.getValue()).getName().equals(sector)) {
				currentSectorItem = oldSectorItem;
				foundSector = true;
				break;
			}
		}
		if (!foundSector) {
			TreeItem<Object> newSectorItem = new TreeItem<Object>(
					new SectorItemValue(sector));
			newSectorItem.setExpanded(expandAll ? true : false);
			currentMarketItem.getChildren().add(newSectorItem);
			currentSectorItem = newSectorItem;
		}

		// stock name
		TreeItem<Object> stockNameItem = new TreeItem<Object>(record);
		currentSectorItem.getChildren().add(stockNameItem);
	}

	private MenuItem[] createAllStocksTreeContextMenuContents() {
		List<MenuItem> menuItems = new ArrayList<>();

		MenuItem menuTest = new MenuItem("Test");
		menuTest.setOnAction(createTestEventHandler());
		menuItems.add(menuTest);

		return menuItems.toArray(new MenuItem[menuItems.size()]);
	}

	private EventHandler<ActionEvent> createTestEventHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				alertSample();
			}
		};
	}

	private MenuItem[] createRegisteredStocksTreeContextMenuContents(
			int idxRegisteredStocks) {
		List<MenuItem> menuItems = new ArrayList<>();

		MenuItem menuUnregister = new MenuItem(
				resource.getString(MessageKey.UNREGISTER_CONTEXT_MENU));
		menuUnregister.setUserData(Integer.valueOf(idxRegisteredStocks));
		menuUnregister
				.setOnAction(createUnregisterStocksFromTreeEventHandler());

		menuItems.add(menuUnregister);
		return menuItems.toArray(new MenuItem[menuItems.size()]);
	}

	private EventHandler<ActionEvent> createUnregisterStocksFromTreeEventHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MenuItem selectedMenu = (MenuItem) (e.getSource());
				int idxRegisteredStocks = (Integer) (selectedMenu.getUserData());
				TreeView tView = registeredStocksTreeViews[idxRegisteredStocks];
				ObservableList selectedItems = tView.getSelectionModel()
						.getSelectedItems();
				for (int i = 0; i < NUM_REGISTERED_STOCKS; ++i) {
					if (tView == registeredStocksTreeViews[i]) {
						idxRegisteredStocks = i;
						break;
					}
				}
				Set registeredStockSet = registeredStockSets[idxRegisteredStocks];

				selectedItems.forEach(item -> {
					Object obj = ((TreeItem) item).getValue();
					if (obj instanceof RootItemValue) {
						TreeItem rootItem = (TreeItem) item;
						removeRootStocksFromSetForUnregister(rootItem,
								registeredStockSet);
					} else if (obj instanceof MarketItemValue) {
						TreeItem marketItem = (TreeItem) item;
						removeMarketStocksFromSetForUnregister(marketItem,
								registeredStockSet);
					} else if (obj instanceof SectorItemValue) {
						TreeItem sectorItem = (TreeItem) item;
						removeSectorStocksFromSetForUnregister(sectorItem,
								registeredStockSet);
					} else if (obj instanceof StockRecord) {
						StockRecord record = (StockRecord) obj;
						removeStockFromSetForUnregister((TreeItem) item,
								registeredStockSet);
					}
				});

				TreeItem rootItem = registeredStocksRootItems[idxRegisteredStocks];

				// Update registered stocks tree
				updateRegisteredStocksTree(rootItem, registeredStockSet);

				// Update pref
				updatePrefForRegisteredStocks(idxRegisteredStocks,
						registeredStockSet);

				// Save pref.
				savePref();
			}
		};
	}

	private void removeRootStocksFromSetForUnregister(TreeItem rootItem,
			Set registerSet) {
		rootItem.getChildren().forEach(
				marketItem -> {
					removeMarketStocksFromSetForUnregister(
							(TreeItem) marketItem, registerSet);
				});
	}

	private void removeMarketStocksFromSetForUnregister(TreeItem marketItem,
			Set registerSet) {
		marketItem.getChildren().forEach(
				sectorItem -> {
					removeSectorStocksFromSetForUnregister(
							(TreeItem) sectorItem, registerSet);
				});
	}

	private void removeSectorStocksFromSetForUnregister(TreeItem sectorItem,
			Set registerSet) {
		sectorItem.getChildren().forEach(stockItem -> {
			removeStockFromSetForUnregister((TreeItem) stockItem, registerSet);
		});
	}

	private void removeStockFromSetForUnregister(TreeItem stockItem,
			Set registerSet) {
		StockRecord record = (StockRecord) (stockItem.getValue());
		String stockCode = (String) record.get(StockEnum.STOCK_CODE);
		registerSet.remove(stockCode);
	}

	private void sortAllTreesAndSetCaptions() {
		// Sort tree items of all stocks.
		allStocksRootItem.getChildren().sort(MarketUtil.marketTreeComparator());
		allStocksRootItem.getChildren().forEach(
				marketTreeItem -> {
					marketTreeItem.getChildren().sort(
							SectorUtil.sectorTreeComparator());
				});

		// Set tree captions
		setTreeCaptions(allStocksRootItem);

		for (int idxList = 0; idxList < NUM_REGISTERED_STOCKS; ++idxList) {
			TreeItem<Object> rootItem = registeredStocksRootItems[idxList];

			// Sort tree items of registered stocks.
			rootItem.getChildren().sort(MarketUtil.marketTreeComparator());
			rootItem.getChildren().forEach(
					marketTreeItem -> {
						marketTreeItem.getChildren().sort(
								SectorUtil.sectorTreeComparator());
					});

			// Set tree captions of registered stocks.
			setTreeCaptions(rootItem);
		}
	}

	private EventHandler<MouseEvent> createTreeMouseEventHandler(
			TreeView<Object> tree) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 1) {
					// TreeItem item = (TreeItem) tree.getSelectionModel()
					// .getSelectedItem();
					// reloadTableByItemMulti(item);
				} else if (mouseEvent.getClickCount() == 2) {
				}
			}
		};
	}

	private ChangeListener<TreeItem<Object>> createTreeChangeListener() {
		return new ChangeListener<TreeItem<Object>>() {
			@Override
			public void changed(
					ObservableValue<? extends TreeItem<Object>> observable,
					TreeItem<Object> oldValue, TreeItem<Object> newValue) {
				TreeItem<Object> item = (TreeItem<Object>) newValue;
				reloadTableByItemMulti(item);
			}
		};
	}

	private void setTreeCaptions(TreeItem<Object> root) {
		ItemValue rootItemValue = (ItemValue) (root.getValue());
		rootItemValue.setNumChildren(0);
		root.getChildren().forEach(marketItem -> {
			ItemValue marketItemValue = (ItemValue) (marketItem.getValue());
			marketItemValue.setNumChildren(0);
			marketItem.getChildren().forEach(item -> {
				ItemValue sectorItemValue = (ItemValue) (item.getValue());

				int num = item.getChildren().size();
				sectorItemValue.setNumChildren(num);
				constructTreeItemCaption(sectorItemValue);

				marketItemValue.addNumChildren(num);
				rootItemValue.addNumChildren(num);
			});
			constructTreeItemCaption(marketItemValue);
		});
		constructTreeItemCaption(rootItemValue);
	}

	private void constructTreeItemCaption(ItemValue itemValue) {
		String caption = itemValue.getName() + " ("
				+ itemValue.getNumChildren() + ")";
		itemValue.setCaption(caption);
	}

	private void updateRegisteredStocksTree(TreeItem<Object> rootItem,
			Set registeredStockSet) {
		rootItem.getChildren().clear();
		for (StockRecord record : lastData) {
			// market
			String market = (String) record.get(StockEnum.MARKET);
			if (market == null || market.length() == 0) {
				market = "(none)";
			}

			// sector
			String sector = (String) record.get(StockEnum.SECTOR);
			if (sector == null || sector.length() == 0) {
				sector = "(none)";
			}

			// stock name
			String stockName = (String) record.get(StockEnum.STOCK_NAME);
			if (stockName == null || stockName.length() == 0) {
				stockName = "(none)";
			}

			// stock code
			String stockCode = (String) record.get(StockEnum.STOCK_CODE);

			if (registeredStockSet.contains(stockCode)) {
				addItemToTree(rootItem, record, market, sector, stockName, true);
			}
		}

		rootItem.getChildren().sort(MarketUtil.marketTreeComparator());
		rootItem.getChildren().forEach(
				marketTreeItem -> {
					marketTreeItem.getChildren().sort(
							SectorUtil.sectorTreeComparator());
				});

		setTreeCaptions(rootItem);
	}

	private TreeView getTreeViewFromTreeItem(TreeItem treeItem) {
		while (treeItem.getParent() != null) {
			treeItem = treeItem.getParent();
		}
		if (allStocksRootItem == treeItem) {
			return allStocksTreeView;
		}
		for (int idx = 0; idx < NUM_REGISTERED_STOCKS; ++idx) {
			if (registeredStocksRootItems[idx] == treeItem) {
				return registeredStocksTreeViews[idx];
			}
		}
		return null;
	}

	private MenuItem[] createTableContextMenuContents(TableView view) {
		List<MenuItem> menuItems = new ArrayList<>();

		Menu menuRegister = new Menu(
				resource.getString(MessageKey.REGISTER_CONTEXT_MENU));
		for (int idx = 0; idx < NUM_REGISTERED_STOCKS; ++idx) {
			String subMenuTitleHead = resource
					.getString(MessageKey.REGISTERED_STOCKS_CONTEXT_MENU);
			MenuItem menuRegisterStocks = new MenuItem(subMenuTitleHead
					+ (idx + 1));
			menuRegisterStocks
					.setOnAction((ActionEvent e) -> {
						MenuItem menu = (MenuItem) e.getSource();
						String text = menu.getText();
						String onlyDigitText = Util.toDigitOnly(text);
						int idxRegisteredStocks = Integer
								.parseInt(onlyDigitText) - 1;
						Set registeredStockSet = registeredStockSets[idxRegisteredStocks];

						ObservableList<TableStockData> selectedItems = view
								.getSelectionModel().getSelectedItems();
						selectedItems.forEach(item -> {
							TableStockData data = (TableStockData) item;
							String stockCode = data.getStockCode();
							registeredStockSet.add(stockCode);
							System.out.println("stockCode=" + stockCode);
						});

						TreeItem rootItem = registeredStocksRootItems[idxRegisteredStocks];

						// Update registered stocks tree
						updateRegisteredStocksTree(rootItem, registeredStockSet);

						// Update pref
						updatePrefForRegisteredStocks(idxRegisteredStocks,
								registeredStockSet);

						// Save pref.
						savePref();
					});
			menuRegister.getItems().add(menuRegisterStocks);
		}
		menuItems.add(menuRegister);

		MenuItem menuDeleteSelectedRows = new MenuItem(
				resource.getString(MessageKey.DELETE_SELECTED_ROWS_CONTEXT_MENU));
		menuDeleteSelectedRows
				.setOnAction((ActionEvent e) -> {
					ObservableList<TableStockData> selectedItems = view
							.getSelectionModel().getSelectedItems();
					ArrayList copy = new ArrayList();
					copy.addAll(selectedItems);
					if (copy.size() != 0) {
						ObservableList tableStockDataList = beforeUpdateTableStockDataList(false);
						tableStockDataList.removeAll(copy);
						afterUpdateTableStockDataList(tableStockDataList, view);
					}
					view.getSelectionModel().clearSelection();
				});
		menuItems.add(menuDeleteSelectedRows);

		MenuItem menuDeleteUnselectedRows = new MenuItem(
				resource.getString(MessageKey.DELETE_UNSELECTED_ROWS_CONTEXT_MENU));
		menuDeleteUnselectedRows
				.setOnAction((ActionEvent e) -> {
					ObservableList<TableStockData> selectedItems = view
							.getSelectionModel().getSelectedItems();
					ArrayList copy = new ArrayList();
					copy.addAll(selectedItems);
					if (copy.size() != 0) {
						ObservableList tableStockDataList = beforeUpdateTableStockDataList(true);
						tableStockDataList.addAll(copy);
						afterUpdateTableStockDataList(tableStockDataList, view);
					}
					view.getSelectionModel().clearSelection();
				});
		menuItems.add(menuDeleteUnselectedRows);

		MenuItem menuOpenYahooFinance = new MenuItem(
				resource.getString(MessageKey.OPEN_YAHOO_FINANCE_HTML_PAGE));
		menuOpenYahooFinance.setOnAction((ActionEvent e) -> {
			ObservableList<TableStockData> selectedItems = view
					.getSelectionModel().getSelectedItems();
			selectedItems.forEach(item -> {
				TableStockData data = (TableStockData) item;
				String stockCode = data.getStockCode();
				if (stockCode == null) {
					return;
				}
				startBrowser(financeManager.getHtmlChartPageSpec(financeManager
						.toDetailSearchStockCode(stockCode)));
			});
		});
		menuItems.add(menuOpenYahooFinance);

		return menuItems.toArray(new MenuItem[menuItems.size()]);
	}

	private void reloadTableByItemMulti(TreeItem<Object> item) {
		if (item == null) {
			return;
		}
		Object value = item.getValue();
		TreeView treeView = getTreeViewFromTreeItem(item);
		ObservableList<TreeItem> list = treeView.getSelectionModel()
				.getSelectedItems();
		ObservableList tableStockDataList = beforeUpdateTableStockDataList(true);
		if (value instanceof StockRecord) {
			reloadTableByItem(item, tableStockDataList);
			return;
		}
		HashSet dupCheckSet = new HashSet();
		list.forEach(selectedItem -> {
			TreeItem checkParentItem = selectedItem;
			boolean found = false;
			while (checkParentItem.getParent() != null) {
				checkParentItem = checkParentItem.getParent();
				if (dupCheckSet.contains(checkParentItem)) {
					found = true;
				}
			}
			dupCheckSet.add(selectedItem);
			if (!found) {
				reloadTableByItem((TreeItem) selectedItem, tableStockDataList);
			}
		});
		afterUpdateTableStockDataList(tableStockDataList, this.stockTableView);
	}

	private void reloadTableByItem(TreeItem<Object> item,
			ObservableList tableStockDataList) {
		Object value = item.getValue();
		if (value instanceof RootItemValue) {
			reloadTableByRoot(item, tableStockDataList);
		} else if (value instanceof MarketItemValue) {
			reloadTableByMarket(item, tableStockDataList);
		} else if (value instanceof SectorItemValue) {
			reloadTableBySector(item, tableStockDataList);
		} else if (value instanceof StockRecord) {
			String stockCode = (String) ((StockRecord) value)
					.get(StockEnum.STOCK_CODE);
			reloadRightPane(stockCode);
		}
	}

	private void reloadTableByRoot(TreeItem<Object> root,
			ObservableList tableStockDataList) {
		root.getChildren().forEach(marketItem -> {
			reloadTableByMarket(marketItem, tableStockDataList);
		});
	}

	private void reloadTableByMarket(TreeItem<Object> marketItem,
			ObservableList tableStockDataList) {
		marketItem.getChildren().forEach(sectorItem -> {
			reloadTableBySector(sectorItem, tableStockDataList);
		});
	}

	private void reloadTableBySector(TreeItem<Object> sectorItem,
			ObservableList tableStockDataList) {
		sectorItem
				.getChildren()
				.forEach(stockItem -> {
					if (!(stockItem.getValue() instanceof StockRecord)) {
						return; // continue
					}
					StockRecord record = (StockRecord) stockItem.getValue();
					String stockCode = (String) record
							.get(StockEnum.STOCK_CODE);
					DetailRecord detail = (DetailRecord) stockCodeToDetailRecordMap
							.get(stockCode);
					ProfileRecord profile = (ProfileRecord) stockCodeToProfileRecordMap
							.get(stockCode);
					tableStockDataList.add(new TableStockData(record, detail,
							profile));
				});
	}

	private ObservableList beforeUpdateTableStockDataList(boolean regenerateData) {
		if (regenerateData) {
			return FXCollections.observableArrayList();
		}
		return (ObservableList) getHistoryContent();
	}

	private void afterUpdateTableStockDataList(
			ObservableList tableStockDataList, TableView view) {

		// Prepare
		ObservableList<TableStockData> save = FXCollections
				.observableArrayList();
		save.addAll(tableStockDataList);

		// Create history
		History history = new History();
		history.setHistoryType(HistoryType.STOCK_LIST);
		history.setView(view);
		history.setContent(save);

		// Add history
		addHistory(history);

		// Update view
		updateCenterContentPane();
		updateHistoryButtonsStatus();
	}

	private void afterUpdateTableScreeningDataList(
			ObservableList tableScreeningDataList, TableView view) {
		// Prepare
		ObservableList<TableStockData> save = FXCollections
				.observableArrayList();
		save.addAll(tableScreeningDataList);

		// Create history
		History history = new History();
		history.setHistoryType(HistoryType.SCREENING_RESULT);
		history.setView(view);
		history.setContent(save);

		// Add history
		addHistory(history);

		// Update view
		updateCenterContentPane();
		updateHistoryButtonsStatus();
	}

	private void addHistory(History history) {
		// Update history
		if (currentHistoryIdx < 0) {
			historyList.add(history);
			currentHistoryIdx = 0;
		} else {
			historyList.add(currentHistoryIdx + 1, history);
			++currentHistoryIdx;
			while (historyList.size() > currentHistoryIdx + 1) {
				historyList.remove(currentHistoryIdx + 1);
			}
		}
		if (currentHistoryIdx > HISTORY_SIZE - 1) {
			historyList.remove(0);
			--currentHistoryIdx;
		}
	}

	private ObservableList getHistoryContent() {
		if (currentHistoryIdx < 0) {
			return null;
		}
		History history = historyList.get(currentHistoryIdx);
		ObservableList list = FXCollections.observableArrayList();
		ObservableList content = (ObservableList) history.getContent();
		list.addAll(content);
		return list;
	}

	private EventHandler<ActionEvent> createHistoryBackEventHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				--currentHistoryIdx;
				updateCenterContentPane();
				updateHistoryButtonsStatus();
			}
		};
	}

	private EventHandler<ActionEvent> createHistoryForwardEventHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				++currentHistoryIdx;
				updateCenterContentPane();
				updateHistoryButtonsStatus();
			}
		};
	}

	private void updateCenterContentPane() {
		History history = historyList.get(currentHistoryIdx);
		TableView view = (TableView) history.getView();
		ObservableList tableStockDataList = (ObservableList) history
				.getContent();
		view.setItems(tableStockDataList);
		centerContentPane.getChildren().clear();
		centerContentPane.getChildren().add(view);
	}

	private void updateHistoryButtonsStatus() {
		if (currentHistoryIdx < 0) {
			backButton.setDisable(true);
			forwardButton.setDisable(true);
		} else {
			if (currentHistoryIdx == 0) {
				backButton.setDisable(true);
			} else {
				backButton.setDisable(false);
			}
			if (currentHistoryIdx >= historyList.size() - 1) {
				forwardButton.setDisable(true);
			} else {
				forwardButton.setDisable(false);
			}
		}
	}

	private class TableChangeListener implements ChangeListener {
		private TableView view;

		public TableChangeListener(TableView view) {
			super();
			this.view = view;
		}

		@Override
		public void changed(ObservableValue observable, Object oldValue,
				Object newValue) {
			TableViewSelectionModel<TableStockData> selectionModel = view
					.getSelectionModel();
			if (selectionModel.getSelectedItem() != null) {
				// ObservableList selectedCells = selectionModel
				// .getSelectedCells();
				// TablePosition tablePosition = (TablePosition)
				// selectedCells
				// .get(0);
				// Object val = tablePosition.getTableColumn().getCellData(
				// newValue);
				// System.out.println("Selected Value" + val);

				int index = selectionModel.getSelectedIndex();
				if (index < 0) {
					return;
				}

				ObservableList tableStockDataList = view.getItems();
				if (tableStockDataList == null) {
					return;
				}

				TableStockData tableStockData = (TableStockData) tableStockDataList
						.get(index);
				if (tableStockData == null) {
					return;
				}

				String stockCode = tableStockData.getStockCode();
				reloadRightPane(stockCode);
			}
		}
	}

	private void reloadRightPane(String stockCode) {
		this.rightPaneStockCode = stockCode;

		ProfileRecord profileRecord = null;
		DetailRecord detailRecord = null;
		if (stockCode != null) {
			profileRecord = (ProfileRecord) stockCodeToProfileRecordMap
					.get(stockCode);
			detailRecord = (DetailRecord) stockCodeToDetailRecordMap
					.get(stockCode);
		}

		priceInfoPane.setDetailRecord(detailRecord);
		referenceInfoPane.setDetailRecord(detailRecord);
		fundReferenceInfoPane.setDetailRecord(detailRecord);
		marginInfoPane.setDetailRecord(detailRecord);
		profileInfoPane.setProfileRecord(profileRecord);

		rightPane1.getChildren().clear();
		rightPane1.getChildren().addAll(priceInfoAccordion,
				referenceInfoAccordion, fundReferenceInfoAccordion);

		rightPane2.getChildren().clear();
		rightPane2.getChildren().addAll(profileInfoAccordion,
				marginInfoAccordion);
	}

	private void searchCorps(String text) {
		ObservableList tableStockDataList = beforeUpdateTableStockDataList(true);
		lastData.forEach(record -> {
			String stockCode = (String) record.get(StockEnum.STOCK_CODE);
			String stockName = (String) record.get(StockEnum.STOCK_NAME);
			String market = (String) record.get(StockEnum.MARKET);
			String sector = (String) record.get(StockEnum.SECTOR);
			if ((stockCode != null && stockCode.length() > 0 && stockCode
					.contains(text))
					|| (stockName != null && stockName.length() > 0 && stockName
							.contains(text))
					|| (market != null && market.length() > 0 && market
							.contains(text))
					|| (sector != null && sector.length() > 0 && sector
							.contains(text))) {
				DetailRecord detail = (DetailRecord) stockCodeToDetailRecordMap
						.get(stockCode);
				ProfileRecord profile = (ProfileRecord) stockCodeToProfileRecordMap
						.get(stockCode);
				tableStockDataList.add(new TableStockData(record, detail,
						profile));
			}
		});
		afterUpdateTableStockDataList(tableStockDataList, this.stockTableView);
	}

	private void updateTable() {
		ObservableList tableStockDataList = beforeUpdateTableStockDataList(true);
		String text = consoleTextArea.getText();
		text = text.trim();
		text = text.replace("\r\n", ",");
		text = text.replace("\r", ",");
		text = text.replace("\n", ",");
		String[] codes = text.split(",");
		for (String code : codes) {
			if (code == null) {
				continue;
			}
			String stockCode = code.trim();
			if (stockCode.length() == 0) {
				continue;
			}
			List<StockRecord> list = corpDataListInCodeMap.get(stockCode);
			if (list == null || list.size() == 0) {
				continue;
			}
			for (StockRecord record : list) {
				DetailRecord detail = (DetailRecord) stockCodeToDetailRecordMap
						.get(code);
				ProfileRecord profile = (ProfileRecord) stockCodeToProfileRecordMap
						.get(stockCode);
				tableStockDataList.add(new TableStockData(record, detail,
						profile));
				break;
			}
		}
		afterUpdateTableStockDataList(tableStockDataList, this.stockTableView);
	}

	private void getFromTable() {
		ObservableList<TableStockData> tableStockDataList = getHistoryContent();
		if (tableStockDataList == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (TableStockData stockData : tableStockDataList) {
			if (sb.length() != 0) {
				sb.append(System.lineSeparator());
			}
			sb.append(stockData.getStockCode());
		}
		consoleTextArea.setText(sb.toString());
	}

	private void updatePrefForRegisteredStocks(int idxRegisteredStocks,
			Set<String> registeredStockSet) {
		String key = null;

		key = PREFKEY_REGISTERED_STOCKS_ + idxRegisteredStocks;
		StringBuilder sb = new StringBuilder();
		registeredStockSet.forEach(stockCode -> {
			if (sb.length() != 0) {
				sb.append(',');
			}
			sb.append(stockCode);
		});
		String value = sb.toString();
		pref.setProperty(key, value);
	}

	private void startBrowser(String spec) {
		getHostServices().showDocument(spec);
	}

	private void alertSample() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Look, an Information Dialog");
		alert.setContentText("I have a great message for you!");
		alert.showAndWait();
	}

	private void executeScreening(ScreeningParameter screeningParameter,
			boolean execute) {
		System.out.println("minPer=" + screeningParameter.getMinPer()
				+ ", maxPer=" + screeningParameter.getMaxPer() + ", minPbr="
				+ screeningParameter.getMinPbr() + ", maxPbr="
				+ screeningParameter.getMaxPbr() + ", minAnnualInterestRate="
				+ screeningParameter.getMinAnnualInterestRate()
				+ ", maxAnnualInterestRate="
				+ screeningParameter.getMaxAnnualInterestRate());

		try {
			pref.setProperty(PREFKEY_CURRENT_SCREENING_PARAMETERS,
					Util.serializeObjToHex(screeningParameter));
			savePref();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (execute) {
			System.out.println("Execute!");
			if (screeningParameter.isEmpty()) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText("No parameter is set.");
				alert.setContentText("To execute screening, set parameter(s) in the screening dialog.");
				alert.showAndWait();
				return;
			}

			// Screening
			List<TableScreeningData> list = doScreeningCorps(screeningParameter);
			if (list == null || list.size() == 0) {
				// TODO: alert() and return
			}
			System.out.println("Screening result count=" + list.size());

			// Update view
			ObservableList tableScreeningDataList = beforeUpdateTableStockDataList(true);
			tableScreeningDataList.addAll(list);
			TableView view = buildScreeningTableView();
			afterUpdateTableScreeningDataList(tableScreeningDataList, view);
		} else {
			System.out.println("Close!");
		}

		// TODO: save parameters
	}

	private List<TableScreeningData> doScreeningCorps(
			ScreeningParameter screeningParameter) {

		System.out.println("doScreeningCorps()");
		List list = new ArrayList();

		//
		// Get data
		//
		Map<String, StockSplitInfo> stockCodeToSplitInfoMap = financeManager
				.getStockCodeToSplitInfoMap();

		//
		// scan
		//
		Calendar currentDay = CalendarUtil.createToday();
		int count = 0;
		for (int idxCorp = 0; idxCorp < stockCodes.length; ++idxCorp) {
			String stockCode = stockCodes[idxCorp];
			List<StockRecord> oneCorpRecords = null;
			if (stockManager != null) {
				oneCorpRecords = stockManager.retrieve(stockCode);
			}

			if (oneCorpRecords == null) {

			} else if (oneCorpRecords.size() == 0) {
				System.out
						.println("Warning: No stock price record for one corp. stockCode="
								+ stockCode);
			} else {
				String splitSerachStockCode = financeManager
						.toSplitSearchStockCode(stockCode);
				StockSplitInfo stockSplitInfo = stockCodeToSplitInfoMap
						.get(splitSerachStockCode);
				stockManager.calcAdjustedPricesForOneCorp(oneCorpRecords,
						stockSplitInfo, currentDay);
			}

			TableScreeningData data = doScreeningOneCorp(stockCode,
					oneCorpRecords, stockManager, financeManager,
					screeningParameter);
			if (data != null) {
				list.add(data);
			}
		}
		return list;
	}

	public TableScreeningData doScreeningOneCorp(String stockCode,
			List<StockRecord> oneCorpRecords, StockManager stockManager,
			FinanceManager financeManager, ScreeningParameter screeningParameter) {

		Double minAnnualInterestRate = screeningParameter
				.getMinAnnualInterestRate();
		Double maxAnnualInterestRate = screeningParameter
				.getMaxAnnualInterestRate();
		Double minPer = screeningParameter.getMinPer();
		Double maxPer = screeningParameter.getMaxPer();
		Double minPbr = screeningParameter.getMinPbr();
		Double maxPbr = screeningParameter.getMaxPbr();
		Double minEps = screeningParameter.getMinEps();
		Double maxEps = screeningParameter.getMaxEps();
		Double minBps = screeningParameter.getMinBps();
		Double maxBps = screeningParameter.getMaxBps();
		Double minRoe = screeningParameter.getMinRoe();
		Double maxRoe = screeningParameter.getMaxRoe();
		Double minMarketCapitalization = screeningParameter
				.getMinMarketCapitalization();
		Double maxMarketCapitalization = screeningParameter
				.getMaxMarketCapitalization();
		Double minAverageAnnualSalary = screeningParameter
				.getMinAverageAnnualSalary();
		Double maxAverageAnnualSalary = screeningParameter
				.getMaxAverageAnnualSalary();
		Double minAverageAge = screeningParameter.getMinAverageAge();
		Double maxAverageAge = screeningParameter.getMaxAverageAge();

		IndicatorRecord indicator = new IndicatorRecord();
		StockRecord stockRecord = null;
		for (StockRecord record : oneCorpRecords) {
			// Get at least 1 record.
			stockRecord = record;

			String market = (String) record.get(StockEnum.MARKET);
			String sector = (String) record.get(StockEnum.SECTOR);
			String stockName = (String) record.get(StockEnum.STOCK_NAME);

			//
			// TODO: Calc indicator.
			//

			break; // TODO: Delete break if necessary.
		}
		//
		// TODO: Set indicator values.
		//

		if (stockRecord == null) {
			return null;
		}

		DetailRecord detail = (DetailRecord) stockCodeToDetailRecordMap
				.get(stockCode);
		ProfileRecord profile = (ProfileRecord) stockCodeToProfileRecordMap
				.get(stockCode);

		//
		// Screening.
		//

		Double d;

		if (minPer != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.PER)) == null) {
				return null;
			}
			if (d < minPer) {
				return null;
			}
		}
		if (maxPer != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.PER)) == null) {
				return null;
			}
			if (d > maxPer) {
				return null;
			}
		}

		if (minPbr != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.PBR)) == null) {
				return null;
			}
			if (d < minPbr) {
				return null;
			}
		}
		if (maxPbr != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.PBR)) == null) {
				return null;
			}
			if (d > maxPbr) {
				return null;
			}
		}

		if (minAnnualInterestRate != null) {
			if (detail == null
					|| (d = (Double) detail
							.get(DetailEnum.ANNUAL_INTEREST_RATE)) == null) {
				return null;
			}
			if (d < minAnnualInterestRate) {
				return null;
			}
		}
		if (maxAnnualInterestRate != null) {
			if (detail == null
					|| (d = (Double) detail
							.get(DetailEnum.ANNUAL_INTEREST_RATE)) == null) {
				return null;
			}
			if (d > maxAnnualInterestRate) {
				return null;
			}
		}

		if (minEps != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.EPS)) == null) {
				return null;
			}
			if (d < minEps) {
				return null;
			}
		}
		if (maxEps != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.EPS)) == null) {
				return null;
			}
			if (d > maxEps) {
				return null;
			}
		}

		if (minBps != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.BPS)) == null) {
				return null;
			}
			if (d < minBps) {
				return null;
			}
		}
		if (maxBps != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.BPS)) == null) {
				return null;
			}
			if (d > maxBps) {
				return null;
			}
		}

		if (minRoe != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.ROE)) == null) {
				return null;
			}
			if (d < minRoe) {
				return null;
			}
		}
		if (maxRoe != null) {
			if (detail == null
					|| (d = (Double) detail.get(DetailEnum.ROE)) == null) {
				return null;
			}
			if (d > maxRoe) {
				return null;
			}
		}

		if (minMarketCapitalization != null) {
			if (detail == null
					|| (d = (Double) detail
							.get(DetailEnum.MARKET_CAPITALIZATION)) == null) {
				return null;
			}
			if (d < minMarketCapitalization) {
				return null;
			}
		}
		if (maxMarketCapitalization != null) {
			if (detail == null
					|| (d = (Double) detail
							.get(DetailEnum.MARKET_CAPITALIZATION)) == null) {
				return null;
			}
			if (d > maxMarketCapitalization) {
				return null;
			}
		}

		if (minAverageAnnualSalary != null) {
			if (profile == null
					|| (d = (Double) profile
							.get(ProfileEnum.AVERAGE_ANNUAL_SALARY)) == null) {
				return null;
			}
			if (d < minAverageAnnualSalary) {
				return null;
			}
		}
		if (maxAverageAnnualSalary != null) {
			if (profile == null
					|| (d = (Double) profile
							.get(ProfileEnum.AVERAGE_ANNUAL_SALARY)) == null) {
				return null;
			}
			if (d > maxAverageAnnualSalary) {
				return null;
			}
		}

		if (minAverageAge != null) {
			if (profile == null
					|| (d = (Double) profile.get(ProfileEnum.AVERAGE_AGE)) == null) {
				return null;
			}
			if (d < minAverageAge) {
				return null;
			}
		}
		if (maxAverageAge != null) {
			if (profile == null
					|| (d = (Double) profile.get(ProfileEnum.AVERAGE_AGE)) == null) {
				return null;
			}
			if (d > maxAverageAge) {
				return null;
			}
		}

		TableScreeningData data = new TableScreeningData(stockRecord, detail,
				profile, indicator);
		return data;
	}
}
