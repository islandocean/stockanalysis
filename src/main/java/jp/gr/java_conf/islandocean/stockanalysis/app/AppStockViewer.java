package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.CorpInfoPane;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.CorpViewType;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.ItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.MarketItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.MessageKey;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.Pref;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.ResourceBundleWithUtf8;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.RootItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.SectorItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.app.ui.TableStockData;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.DetailRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.finance.MarketUtil;
import jp.gr.java_conf.islandocean.stockanalysis.finance.ProfileRecord;
import jp.gr.java_conf.islandocean.stockanalysis.finance.SectorUtil;
import jp.gr.java_conf.islandocean.stockanalysis.price.Config;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class AppStockViewer extends Application implements CorpsScannerTemplate {

	//
	private static final String PREFKEY_REGISTERED_STOCKS_1 = "REGISTERED_STOCKS_1";
	private static final String PREFKEY_REGISTERED_STOCKS_2 = "REGISTERED_STOCKS_2";
	private static final String PREFKEY_REGISTERED_STOCKS_3 = "REGISTERED_STOCKS_3";
	private static final String PREFKEY_REGISTERED_STOCKS_4 = "REGISTERED_STOCKS_4";
	private static final String PREFKEY_REGISTERED_STOCKS_5 = "REGISTERED_STOCKS_5";
	private static final double TREEVIEW_MIN_HEIGHT = 700d;

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
	private static final int numRegister = 5;
	private String[] registeredStocksPrefStrs;
	private List<String>[] registeredStockLists;
	private Set<String>[] registeredStockSets;

	private ObservableList<TableStockData> tableStockDataList = FXCollections
			.observableArrayList();

	//
	// Controls
	//

	// Pane
	private VBox rootPane;
	private HBox topPane;
	private TabPane leftPane;
	private SplitPane centerPane;
	private VBox rightPane1;
	private VBox rightPane2;
	private SplitPane middlePane;
	private HBox bottomPane;

	// Menu
	private MenuBar menuBar;
	private Menu fileMenu;
	private Menu viewMenu;

	// Tab and Tab content
	private Tab allStocksTab;
	private Tab registeredStocksTab;
	private Tab searchResultsTab;
	private Tab screeningResultsTab;

	private VBox allStocksTabContent;
	private VBox registeredStocksTabContent;
	private VBox searchResultsTabContent;
	private VBox screeningResultsTabContent;

	// All Stocks
	private HBox allStocksControlPane;
	private Button allStocksTreeCollapseButton;
	private Button allStocksTreeExpandButton;
	private TreeView<Object> allStocksTreeView;
	private TreeItem<Object> allStocksRootItem;

	// Registered Stocks
	private HBox registeredStocksControlPane;
	private ToggleGroup toggleGroup;
	int selectedToggleIdx;
	private ToggleButton[] toggleButtons;
	private TreeView<Object>[] registeredStocksTreeViews;
	private TreeItem<Object>[] registeredStocksRootItems;

	// Search
	private TextField searchTextField;
	private Button searchButton;

	// Table
	private HBox tableControlPane;
	private TableView<TableStockData> tableView;
	private TableColumn stockCodeColumn;
	private TableColumn stockNameColumn;
	private TableColumn marketColumn;
	private TableColumn sectorColumn;

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

		// Initialize resource bundle
		initializeResource();

		// Initialize pref
		readPref();

		// Initialize corp data
		scanInit();

		// Build UI
		buildUi(stage); // Calls scanMain();
	}

	private void initializeResource() {
		// Locale defaultLocale = Locale.getDefault();
		Locale locale = Config.getAppLocale();
		if (locale != null) {
			Locale.setDefault(locale);
		}
		// Locale.setDefault(Locale.JAPAN);
		// Locale.setDefault(Locale.ENGLISH);

		resource = ResourceBundle.getBundle("resources",
				ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL);

		// Locale.setDefault(defaultLocale);
	}

	private void readPref() {
		pref = new Pref(AppStockViewer.class);

		registeredStocksPrefStrs = new String[numRegister];
		registeredStocksPrefStrs[0] = (String) pref
				.getProperty(PREFKEY_REGISTERED_STOCKS_1);
		registeredStocksPrefStrs[1] = (String) pref
				.getProperty(PREFKEY_REGISTERED_STOCKS_2);
		registeredStocksPrefStrs[2] = (String) pref
				.getProperty(PREFKEY_REGISTERED_STOCKS_3);
		registeredStocksPrefStrs[3] = (String) pref
				.getProperty(PREFKEY_REGISTERED_STOCKS_4);
		registeredStocksPrefStrs[4] = (String) pref
				.getProperty(PREFKEY_REGISTERED_STOCKS_5);

		registeredStockLists = new List[numRegister];
		registeredStockSets = new Set[numRegister];
		for (int idxList = 0; idxList < numRegister; ++idxList) {
			registeredStockLists[idxList] = new ArrayList<String>();
			registeredStockSets[idxList] = new HashSet();
			String s = registeredStocksPrefStrs[idxList];
			if (s != null && s.length() > 0) {
				String[] codes = s.split(",");
				for (int idxCodes = 0; idxCodes < codes.length; ++idxCodes) {
					registeredStockLists[idxList].add(codes[idxCodes]);
					registeredStockSets[idxList].add(codes[idxCodes]);
				}
			}
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

	private void buildUi(Stage stage) {
		tableStockDataList.clear();

		//
		// Tree items
		//

		// Create all stocks root item.
		allStocksRootItem = new TreeItem<Object>(new RootItemValue(
				resource.getString(MessageKey.ALL_MARKETS_TREEITEM)));
		allStocksRootItem.setExpanded(true);

		// Create registered stocks root item.
		registeredStocksRootItems = new TreeItem[numRegister];
		for (int i = 0; i < numRegister; ++i) {
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
		sortTreesAndSetCaptions();

		//
		// GUI Parts
		//

		// All stocks tree view
		allStocksTreeView = new TreeView<Object>(allStocksRootItem);
		allStocksTreeView.setMinHeight(TREEVIEW_MIN_HEIGHT);
		allStocksTreeView
				.setOnMouseClicked(createTreeMouseEventHandler(allStocksTreeView));
		allStocksTreeView.getSelectionModel().selectedItemProperty()
				.addListener(createTreeChangeListener());
		allStocksTreeView.setContextMenu(new ContextMenu(
				createTreeContextMenuContents()));

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

		// Registered stocks tree view
		registeredStocksTreeViews = new TreeView[numRegister];
		for (int i = 0; i < numRegister; ++i) {
			registeredStocksTreeViews[i] = new TreeView<Object>(
					registeredStocksRootItems[i]);
			registeredStocksTreeViews[i].getSelectionModel()
					.selectedItemProperty()
					.addListener(createTreeChangeListener());
			registeredStocksTreeViews[i].setContextMenu(new ContextMenu(
					createTreeContextMenuContents()));
			registeredStocksTreeViews[i].setMinHeight(TREEVIEW_MIN_HEIGHT);
		}

		// Registered stocks tree controls
		registeredStocksControlPane = new HBox();
		toggleGroup = new ToggleGroup();
		toggleButtons = new ToggleButton[numRegister];
		for (int i = 0; i < numRegister; ++i) {
			toggleButtons[i] = new ToggleButton();
			toggleButtons[i].setText(Integer.toString(i + 1));
			toggleButtons[i].setToggleGroup(toggleGroup);
			toggleButtons[i].setOnAction((ActionEvent e) -> {
				String text = ((ToggleButton) e.getSource()).getText();
				selectedToggleIdx = Integer.parseInt(text) - 1;
				registeredStocksTabContent.getChildren().clear();
				registeredStocksTabContent.getChildren().addAll(
						registeredStocksControlPane,
						registeredStocksTreeViews[selectedToggleIdx]);
			});
			registeredStocksControlPane.getChildren().add(toggleButtons[i]);
		}
		selectedToggleIdx = 0;
		toggleGroup.selectToggle(toggleButtons[selectedToggleIdx]);

		registeredStocksControlPane.setSpacing(2);
		registeredStocksControlPane.setAlignment(Pos.CENTER_LEFT);
		registeredStocksControlPane.setPadding(new Insets(10, 0, 10, 0));

		// Tab and Tab content
		allStocksTab = new Tab();
		registeredStocksTab = new Tab();
		searchResultsTab = new Tab();
		screeningResultsTab = new Tab();

		allStocksTabContent = new VBox();
		registeredStocksTabContent = new VBox();
		searchResultsTabContent = new VBox();
		screeningResultsTabContent = new VBox();

		allStocksTabContent.setPadding(new Insets(0, 0, 0, 10));
		registeredStocksTabContent.setPadding(new Insets(0, 0, 0, 10));
		searchResultsTabContent.setPadding(new Insets(0, 0, 0, 10));
		screeningResultsTabContent.setPadding(new Insets(0, 0, 0, 10));

		allStocksTab.setContent(allStocksTabContent);
		registeredStocksTab.setContent(registeredStocksTabContent);
		searchResultsTab.setContent(searchResultsTabContent);
		screeningResultsTab.setContent(screeningResultsTabContent);

		allStocksTab.setText(resource.getString(MessageKey.ALL_STOCKS_TABTEXT));
		registeredStocksTab.setText(resource
				.getString(MessageKey.REGISTERED_STOCKS_TABTEXT));
		searchResultsTab.setText(resource
				.getString(MessageKey.SEARCH_RESULTS_TABTEXT));
		screeningResultsTab.setText(resource
				.getString(MessageKey.SCREENING_RESULTS_TABTEXT));

		allStocksTab.setClosable(false);
		registeredStocksTab.setClosable(false);
		searchResultsTab.setClosable(false);
		screeningResultsTab.setClosable(false);

		// All stocks
		allStocksTabContent.getChildren().addAll(allStocksControlPane,
				allStocksTreeView);

		// Registered stocks
		registeredStocksTabContent.getChildren().addAll(
				registeredStocksControlPane,
				registeredStocksTreeViews[selectedToggleIdx]);

		// Table controls
		tableControlPane = new HBox();
		searchTextField = new TextField();
		searchTextField.setPromptText(resource
				.getString(MessageKey.SEARCH_PROMPT_TEXT));
		searchTextField.setMinWidth(230d);
		searchButton = new Button(resource.getString(MessageKey.SEARCH_BUTTON));
		searchButton.setOnAction((ActionEvent e) -> {
			String text = searchTextField.getText();
			if (text != null && text.length() >= 1) {
				searchCorps(text);
			}
		});
		tableControlPane.getChildren().addAll(searchTextField, searchButton);
		tableControlPane.setSpacing(10);
		tableControlPane.setAlignment(Pos.CENTER_LEFT);
		tableControlPane.setPadding(new Insets(10, 10, 10, 10));
		tableControlPane.setMaxHeight(50d);

		// Table View
		tableView = new TableView<TableStockData>();
		stockCodeColumn = new TableColumn(
				resource.getString(MessageKey.STOCK_CODE));
		stockNameColumn = new TableColumn(
				resource.getString(MessageKey.STOCK_NAME));
		stockNameColumn.setMinWidth(200);
		marketColumn = new TableColumn(resource.getString(MessageKey.MARKET));
		sectorColumn = new TableColumn(resource.getString(MessageKey.SECTOR));
		stockCodeColumn.setCellValueFactory(new PropertyValueFactory<>(
				"stockCode"));
		stockNameColumn.setCellValueFactory(new PropertyValueFactory<>(
				"stockName"));
		marketColumn.setCellValueFactory(new PropertyValueFactory<>("market"));
		sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sector"));
		tableView.getColumns().addAll(stockCodeColumn, stockNameColumn,
				marketColumn, sectorColumn);
		tableView.setPlaceholder(new Label(""));
		tableView.setItems(tableStockDataList);
		tableView.getSelectionModel().selectedItemProperty()
				.addListener(createTableChangeListener());
		tableView.setContextMenu(new ContextMenu(
				createTableContextMenuContents()));
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Accordion
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
		leftPane = new TabPane();
		leftPane.getTabs().addAll(allStocksTab, registeredStocksTab,
				searchResultsTab, screeningResultsTab);
		leftPane.getSelectionModel().select(0);
		leftPane.setSide(Side.LEFT);

		// center
		centerPane = new SplitPane();
		centerPane.setOrientation(Orientation.VERTICAL);
		centerPane.getItems().addAll(tableControlPane, tableView);
		centerPane.setDividerPositions(0.33f, 0.66f, 1.0f);

		// right
		rightPane1 = new VBox();
		rightPane2 = new VBox();

		// middle = left + center + right
		middlePane = new SplitPane();
		middlePane.setOrientation(Orientation.HORIZONTAL);
		middlePane.getItems().addAll(leftPane, centerPane, rightPane1,
				rightPane2);
		middlePane.setDividerPositions(0.17f, 0.55f, 0.75f, 1.0f);
		middlePane.setMinSize(600d, 735d);

		// Bottom
		bottomPane = new HBox();
		consoleTextArea = new TextArea();
		consoleTextArea.setMinSize(200d, 50d);
		consoleTextArea.setMaxSize(200d, 50d);
		consoleTextArea.setPromptText("");
		updateButton = new Button("Update Table");
		updateButton.setOnAction((ActionEvent e) -> {
			updateTable();
		});
		getButton = new Button("Get Stock Codes from Table");
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
		stage.setScene(new Scene(rootPane, 1200, 870));
		stage.show();
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
			for (int idxList = 0; idxList < numRegister; ++idxList) {
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
		fileMenu = new Menu("File");
		fileMenu.setDisable(true); //
		viewMenu = new Menu("View");
		viewMenu.setDisable(true); //
		menuBar.getMenus().addAll(fileMenu, viewMenu);
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

	private MenuItem[] createTreeContextMenuContents() {
		List<MenuItem> menuItems = new ArrayList<>();
		MenuItem menuRegister = new MenuItem("_Test");
		menuRegister.setOnAction((ActionEvent e) -> {
			ObservableList selectedItems = allStocksTreeView
					.getSelectionModel().getSelectedItems();
			selectedItems.forEach(item -> {
				Object obj = ((TreeItem) item).getValue();
				if (obj instanceof ItemValue) {
					ItemValue data = (ItemValue) obj;
					String name = data.getName();
					System.out.println("name=" + name);
				} else if (obj instanceof StockRecord) {
					StockRecord record = (StockRecord) obj;
					System.out.println("stockCode=" + record.getStockCode());
				}
			});
		});
		menuItems.add(menuRegister);
		return menuItems.toArray(new MenuItem[menuItems.size()]);
	}

	private void sortTreesAndSetCaptions() {
		// Sort tree items of all stocks.
		allStocksRootItem.getChildren().sort(MarketUtil.marketTreeComparator());
		allStocksRootItem.getChildren().forEach(
				marketTreeItem -> {
					marketTreeItem.getChildren().sort(
							SectorUtil.sectorTreeComparator());
				});

		// Set tree captions
		setTreeCaptions(allStocksRootItem);

		for (int idxList = 0; idxList < numRegister; ++idxList) {
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
					TreeItem item = (TreeItem) tree.getSelectionModel()
							.getSelectedItem();
					if (item != null) {
						Object value = item.getValue();
						if (value instanceof MarketItemValue) {
						} else if (value instanceof SectorItemValue) {
						} else if (value instanceof StockRecord) {
						} else {
						}
					}
				} else if (mouseEvent.getClickCount() == 2) {
					TreeItem item = (TreeItem) tree.getSelectionModel()
							.getSelectedItem();
					if (item != null) {
						Object value = item.getValue();
						if (value instanceof StockRecord) {
						}
					}
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
				reloadTableData(item);
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

	private MenuItem[] createTableContextMenuContents() {
		List<MenuItem> menuItems = new ArrayList<>();
		MenuItem menuRegister = new MenuItem("_Register");
		menuRegister.setOnAction((ActionEvent e) -> {
			ObservableList<TableStockData> selectedItems = tableView
					.getSelectionModel().getSelectedItems();
			selectedItems.forEach(item -> {
				TableStockData data = (TableStockData) item;
				String stockCode = data.getStockCode();
				System.out.println("stockCode=" + stockCode);
			});
		});
		menuItems.add(menuRegister);

		MenuItem menuOpenYahooFinance = new MenuItem(
				"_Open Yahoo Finance HTML Page");
		menuOpenYahooFinance.setOnAction((ActionEvent e) -> {
			ObservableList<TableStockData> selectedItems = tableView
					.getSelectionModel().getSelectedItems();
			selectedItems.forEach(item -> {
				TableStockData data = (TableStockData) item;
				String stockCode = data.getStockCode();
				System.out.println("stockCode=" + stockCode);
				if (stockCode != null) {
					String spec = financeManager
							.getHtmlChartPageSpec(financeManager
									.toDetailSearchStockCode(stockCode));
					startBrowser(spec);
				}
			});
		});
		menuItems.add(menuOpenYahooFinance);

		MenuItem menuPrefTest = new MenuItem("_Pref Test");
		menuPrefTest.setOnAction((ActionEvent e) -> {
			pref.setProperty("abc", "def");
			try {
				pref.save();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		menuItems.add(menuPrefTest);

		return menuItems.toArray(new MenuItem[menuItems.size()]);
	}

	private void reloadTableData(TreeItem<Object> item) {
		if (item == null) {
			return;
		}
		Object value = item.getValue();
		if (value instanceof RootItemValue) {
			reloadTableByAllCorpsUnderTree(item);
		} else if (value instanceof MarketItemValue) {
			tableStockDataList.clear();
			item.getChildren().forEach(sectorItem -> {
				sectorItem.getChildren().forEach(stockItem -> {
					if (!(stockItem.getValue() instanceof StockRecord)) {
						return; // continue
					}
					StockRecord record = (StockRecord) stockItem.getValue();
					tableStockDataList.add(new TableStockData(record));
				});
			});
		} else if (value instanceof SectorItemValue) {
			tableStockDataList.clear();
			item.getChildren().forEach(stockItem -> {
				StockRecord record = (StockRecord) stockItem.getValue();
				tableStockDataList.add(new TableStockData(record));
			});
		} else if (value instanceof StockRecord) {
			String stockCode = ((StockRecord) value).getStockCode();
			reloadRightPane(stockCode);
		}
	}

	private void reloadTableByAllCorpsUnderTree(TreeItem<Object> root) {
		tableStockDataList.clear();
		root.getChildren().forEach(marketItem -> {
			marketItem.getChildren().forEach(sectorItem -> {
				sectorItem.getChildren().forEach(stockItem -> {
					if (!(stockItem.getValue() instanceof StockRecord)) {
						return; // continue
					}
					StockRecord record = (StockRecord) stockItem.getValue();
					tableStockDataList.add(new TableStockData(record));
				});
			});
		});
	}

	private ChangeListener createTableChangeListener() {
		return new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				if (tableView.getSelectionModel().getSelectedItem() != null) {
					TableViewSelectionModel<TableStockData> selectionModel = tableView
							.getSelectionModel();
					// ObservableList selectedCells = selectionModel
					// .getSelectedCells();
					// TablePosition tablePosition = (TablePosition)
					// selectedCells
					// .get(0);
					// Object val = tablePosition.getTableColumn().getCellData(
					// newValue);
					// System.out.println("Selected Value" + val);

					int index = selectionModel.getSelectedIndex();
					if (index >= 0) {
						TableStockData tableStockData = tableStockDataList
								.get(index);
						if (tableStockData != null) {
							String stockCode = tableStockData.getStockCode();
							reloadRightPane(stockCode);
						}
					}
				}
			}
		};
	}

	private void reloadRightPane(String stockCode) {
		ProfileRecord profileRecord = (ProfileRecord) stockCodeToProfileRecordMap
				.get(stockCode);
		DetailRecord detailRecord = (DetailRecord) stockCodeToDetailRecordMap
				.get(stockCode);

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
		tableStockDataList.clear();
		lastData.forEach(record -> {
			String stockCode = record.getStockCode();
			String stockName = record.getStockName();
			String market = record.getMarket();
			String sector = record.getSector();
			if ((stockCode != null && stockCode.length() > 0 && stockCode
					.contains(text))
					|| (stockName != null && stockName.length() > 0 && stockName
							.contains(text))
					|| (market != null && market.length() > 0 && market
							.contains(text))
					|| (sector != null && sector.length() > 0 && sector
							.contains(text))) {
				tableStockDataList.add(new TableStockData(record));
			}
		});
	}

	private void updateTable() {
		tableStockDataList.clear();
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
				tableStockDataList.add(new TableStockData(record));
				break;
			}
		}
	}

	private void getFromTable() {
		StringBuilder sb = new StringBuilder();
		for (TableStockData stockData : tableStockDataList) {
			if (sb.length() != 0) {
				sb.append(System.lineSeparator());
			}
			sb.append(stockData.getStockCode());
		}
		consoleTextArea.setText(sb.toString());
	}

	private void startBrowser(String spec) {
		getHostServices().showDocument(spec);
	}
}
