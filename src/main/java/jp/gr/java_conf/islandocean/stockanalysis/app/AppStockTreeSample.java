package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import jp.gr.java_conf.islandocean.stockanalysis.ui.ItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.ui.MarketItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.ui.RootItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.ui.SectorItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.ui.TableStockData;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class AppStockTreeSample extends Application implements
		CorpsScannerTemplate {

	//
	// Data
	//
	private CorpsAllData allData;
	private StockManager stockManager;
	private FinanceManager financeManager;
	private List<StockRecord> lastData;
	private String[] stockCodes;
	private Map stockCodeToDetailRecordMap;
	private Map stockCodeToProfileRecordMap;

	private ObservableList<TableStockData> tableStockDataList = FXCollections
			.observableArrayList();

	//
	// Controls
	//
	private VBox rootPane;
	private HBox topPane;
	private VBox leftPane;
	private SplitPane centerPane;
	private HBox rightPane;
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
	private Button clearButton;

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
		stage.setTitle("Stock Tree View Sample");

		//
		// Tree items
		//

		// Create root item.
		rootItem = new TreeItem<Object>(new RootItemValue("All Markets"));
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
		treeCollapseButton = new Button("Collapse");
		treeCollapseButton.setOnAction((ActionEvent e) -> {
			rootItem.getChildren().forEach(market -> {
				((TreeItem) market).setExpanded(false);
			});
		});

		treeExpandButton = new Button("Expand");
		treeExpandButton.setOnAction((ActionEvent e) -> {
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
		searchTextField.setPromptText("Enter search string.");
		searchTextField.setMinWidth(230d);
		searchButton = new Button("Search");
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
		stockCodeColumn = new TableColumn("Stock Code");
		stockNameColumn = new TableColumn("Stock Name");
		stockNameColumn.setMinWidth(200);
		marketColumn = new TableColumn("Market");
		sectorColumn = new TableColumn("Sector");
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
		rightPane = new HBox();

		// middle = left + center + right
		middlePane = new SplitPane();
		middlePane.setOrientation(Orientation.HORIZONTAL);
		middlePane.getItems().addAll(leftPane, centerPane, rightPane);
		middlePane.setDividerPositions(0.17f, 0.55f, 1.0f);
		middlePane.setMinSize(600d, 735d);

		// Bottom
		bottomPane = new HBox();
		consoleTextArea = new TextArea();
		consoleTextArea.setMinSize(400d, 50d);
		consoleTextArea.setMaxSize(400d, 50d);
		clearButton = new Button("Clear");
		clearButton.setOnAction((ActionEvent e) -> {
			consoleTextArea.clear();
		});
		bottomPane.setSpacing(10);
		bottomPane.setAlignment(Pos.CENTER_LEFT);
		bottomPane.setPadding(new Insets(10, 10, 10, 10));
		bottomPane.getChildren().addAll(consoleTextArea, clearButton);

		// root = top + middle + bottom
		rootPane = new VBox();
		rootPane.getChildren().addAll(topPane, middlePane, bottomPane);

		stage.setScene(new Scene(rootPane, 1200, 870));
		stage.show();
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
		rightPane.getChildren().clear();

		ProfileRecord profileRecord = (ProfileRecord) stockCodeToProfileRecordMap
				.get(stockCode);
		DetailRecord detailRecord = (DetailRecord) stockCodeToDetailRecordMap
				.get(stockCode);

		if (detailRecord != null) {
			String detail = detailRecord.toTsvString().replace("\t",
					System.lineSeparator());
			Label labelDetail = new Label(detail);
			labelDetail.setMinWidth(60d);
			rightPane.getChildren().add(labelDetail);
		}

		if (profileRecord != null) {
			String profile = profileRecord.toTsvString().replace("\t",
					System.lineSeparator());
			Label labelProfile = new Label(profile);
			labelProfile.setMinWidth(200d);
			rightPane.getChildren().addAll(new Label(" "), labelProfile);
		}
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
}
