package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class AppStockTreeSample extends Application implements
		CorpsScannerTemplate {

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

	private ObservableList<TableStockData> tableStockDataList = FXCollections
			.observableArrayList();

	//
	// Controls
	//
	private VBox rootPane;
	private HBox topPane;
	private VBox leftPane;
	private SplitPane centerPane;
	private VBox rightPane1;
	private VBox rightPane2;
	private SplitPane middlePane;
	private HBox bottomPane;
	private HBox treeControlPane;
	private HBox tableControlPane;

	private MenuBar menuBar;
	private Menu fileMenu;
	private Menu viewMenu;

	private Button treeCollapseButton;
	private Button treeExpandButton;

	private TreeView<Object> treeView;
	private TreeItem<Object> rootItem;

	private TextField searchTextField;
	private Button searchButton;

	private TableView tableView;
	private TableColumn stockCodeColumn;
	private TableColumn stockNameColumn;
	private TableColumn marketColumn;
	private TableColumn sectorColumn;

	private TextArea consoleTextArea;
	private Button updateButton;
	private Button getButton;
	private Button clearButton;

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

	public AppStockTreeSample() {
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
		//
		// Initialize resource bundle
		//
		initializeResource();

		//
		// Tree items
		//

		// Create root item.
		rootItem = new TreeItem<Object>(new RootItemValue(
				resource.getString(MessageKey.ALL_MARKETS)));
		rootItem.setExpanded(true);

		// ---------------------------------------
		// Get stock all data, and add tree items.
		// ---------------------------------------
		scanMain();

		// Sort tree items.
		rootItem.getChildren().sort(MarketUtil.marketTreeComparator());
		rootItem.getChildren().forEach(
				marketTreeItem -> {
					marketTreeItem.getChildren().sort(
							SectorUtil.sectorTreeComparator());
				});

		// Set tree captions
		setTreeCaptions();

		//
		// GUI Parts
		//

		// Tree view
		treeView = new TreeView<Object>(rootItem);
		treeView.setMinHeight(700d);
		treeView.setOnMouseClicked(createTreeMouseEventHandler(treeView));
		treeView.getSelectionModel().selectedItemProperty()
				.addListener(createTreeChangeListener());

		// Tree controls
		treeControlPane = new HBox();
		treeCollapseButton = new Button(
				resource.getString(MessageKey.COLLAPSE_BUTTON));
		treeCollapseButton.setOnAction((ActionEvent e) -> {
			rootItem.getChildren().forEach(market -> {
				((TreeItem) market).setExpanded(false);
			});
		});

		treeExpandButton = new Button(
				resource.getString(MessageKey.EXPAND_BUTTON));
		treeExpandButton.setOnAction((ActionEvent e) -> {
			rootItem.setExpanded(true);
			rootItem.getChildren().forEach(market -> {
				((TreeItem) market).setExpanded(true);
			});
		});

		treeControlPane.getChildren().addAll(treeCollapseButton,
				treeExpandButton);
		treeControlPane.setSpacing(10);
		treeControlPane.setAlignment(Pos.CENTER_LEFT);
		treeControlPane.setPadding(new Insets(10, 10, 10, 10));

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
		tableView = new TableView();
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
		leftPane = new VBox();
		leftPane.getChildren().addAll(treeControlPane, treeView);

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
		consoleTextArea
				.setPromptText("Enter stock codes separated by comma or line separator.");
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

	private void initializeResource() {
		// Locale defaultLocale = Locale.getDefault();
		// Locale.setDefault(Locale.JAPAN);
		Locale.setDefault(Locale.ENGLISH);

		resource = ResourceBundle.getBundle("resources",
				ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL);

		// Locale.setDefault(defaultLocale);
	}

	public void scanMain() {
		try {
			boolean useStockPrice = true;
			boolean useDetailInfo = true;
			boolean useProfileInfo = true;

			// Initialize
			allData = initializeCorpsAllData(useStockPrice, selectDataStore(),
					selectCalendarRange(), useDetailInfo, useProfileInfo);

			// Scan corps
			doScanCorps(allData);

			// Save to reuse
			this.stockManager = allData.getStockManager();
			this.stockManager.generateCorpDataListInCodeMap();
			this.corpDataListInCodeMap = stockManager
					.getCorpDataListInCodeMap();
			this.financeManager = allData.getFinanceManager();
			this.lastData = allData.getLastData();
			this.stockCodes = allData.getStockCodes();
			this.stockCodeToDetailRecordMap = financeManager
					.getStockCodeToDetailRecordMap();
			this.stockCodeToProfileRecordMap = financeManager
					.getStockCodeToProfileRecordMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
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

			// market
			TreeItem<Object> currentMarketItem = null;
			boolean foundMarket = false;
			for (TreeItem<Object> oldMarketItem : rootItem.getChildren()) {
				if (((ItemValue) oldMarketItem.getValue()).getName().equals(
						market)) {
					currentMarketItem = oldMarketItem;
					foundMarket = true;
					break;
				}
			}
			if (!foundMarket) {
				TreeItem<Object> newMarketItem = new TreeItem<Object>(
						new MarketItemValue(market));
				newMarketItem.setExpanded(false);
				rootItem.getChildren().add(newMarketItem);
				currentMarketItem = newMarketItem;
			}

			// sector
			TreeItem<Object> currentSectorItem = null;
			boolean foundSector = false;
			for (TreeItem<Object> oldSectorItem : currentMarketItem
					.getChildren()) {
				if (((ItemValue) oldSectorItem.getValue()).getName().equals(
						sector)) {
					currentSectorItem = oldSectorItem;
					foundSector = true;
					break;
				}
			}
			if (!foundSector) {
				TreeItem<Object> newSectorItem = new TreeItem<Object>(
						new SectorItemValue(sector));
				newSectorItem.setExpanded(false);
				currentMarketItem.getChildren().add(newSectorItem);
				currentSectorItem = newSectorItem;
			}

			// stock name
			TreeItem<Object> stockNameItem = new TreeItem<Object>(record);
			currentSectorItem.getChildren().add(stockNameItem);

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

	public EventHandler<MouseEvent> createTreeMouseEventHandler(TreeView tree) {
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

	public ChangeListener<TreeItem<Object>> createTreeChangeListener() {
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

	private void reloadTableData(TreeItem<Object> item) {
		if (item == null) {
			return;
		}
		Object value = item.getValue();
		if (item == rootItem) {
			reloadTableByAll();
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

	private void reloadTableByAll() {
		tableStockDataList.clear();
		rootItem.getChildren().forEach(marketItem -> {
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

	private void setTreeCaptions() {
		ItemValue rootItemValue = (ItemValue) (rootItem.getValue());
		rootItemValue.setNumChildren(0);
		rootItem.getChildren().forEach(marketItem -> {
			ItemValue marketItemValue = (ItemValue) (marketItem.getValue());
			marketItemValue.setNumChildren(0);
			marketItem.getChildren().forEach(item -> {
				ItemValue sectorItemValue = (ItemValue) (item.getValue());

				int num = item.getChildren().size();
				sectorItemValue.setNumChildren(num);
				constructItemCaption(sectorItemValue);

				marketItemValue.addNumChildren(num);
				rootItemValue.addNumChildren(num);
			});
			constructItemCaption(marketItemValue);
		});
		constructItemCaption(rootItemValue);
	}

	private void constructItemCaption(ItemValue itemValue) {
		String caption = itemValue.getName() + " ("
				+ itemValue.getNumChildren() + ")";
		itemValue.setCaption(caption);
	}

	public ChangeListener createTableChangeListener() {
		return new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				if (tableView.getSelectionModel().getSelectedItem() != null) {
					TableViewSelectionModel selectionModel = tableView
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
}
