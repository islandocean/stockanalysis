package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.gr.java_conf.islandocean.stockanalysis.common.InvalidDataException;
import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.finance.MarketUtil;
import jp.gr.java_conf.islandocean.stockanalysis.finance.SectorUtil;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStoreKdb;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockEnum;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.ui.MarketItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.ui.SectorItemValue;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class AppStockTreeSample extends Application implements
		CorpsScannerTemplate {

	//
	// Data
	//
	private CorpsAllData allData;
	private ObservableList<TableStockData> tableStockData = FXCollections
			.observableArrayList();

	//
	// Controls
	//
	private VBox rootPane;

	private MenuBar menuBar;
	private Menu menuFile;
	private Menu menuView;

	private Button buttonTreeCollapse;
	private Button buttonTreeExpand;

	private TreeView<Object> treeView;
	private TreeItem<Object> rootItem;

	private TextField searchTextField;
	private Button buttonTableSearch;

	private TableView tableView;
	private TableColumn stockCodeCol;
	private TableColumn stockNameCol;
	private TableColumn marketCol;
	private TableColumn sectorCol;
	private Label label1;

	private TextArea consoleTextArea;
	private Button buttonDummy1;

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
		rootItem = new TreeItem<Object>("All Markets");
		rootItem.setExpanded(true);

		// Add tree items.
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
		HBox treeControlPane = new HBox();
		buttonTreeCollapse = new Button("Collapse");
		buttonTreeCollapse.setOnAction((ActionEvent e) -> {
			rootItem.getChildren().forEach(market -> {
				((TreeItem) market).setExpanded(false);
			});
		});

		buttonTreeExpand = new Button("Expand");
		buttonTreeExpand.setOnAction((ActionEvent e) -> {
			rootItem.getChildren().forEach(market -> {
				((TreeItem) market).setExpanded(true);
			});
		});

		treeControlPane.getChildren().addAll(buttonTreeCollapse,
				buttonTreeExpand);
		treeControlPane.setSpacing(10);
		treeControlPane.setAlignment(Pos.CENTER_LEFT);
		treeControlPane.setPadding(new Insets(10, 10, 10, 10));

		// Table controls
		HBox tableControlPane = new HBox();
		buttonTableSearch = new Button("Search");
		buttonTableSearch.setDisable(true); //
		searchTextField = new TextField();
		tableControlPane.getChildren().addAll(searchTextField,
				buttonTableSearch);
		tableControlPane.setSpacing(10);
		tableControlPane.setAlignment(Pos.CENTER_LEFT);
		tableControlPane.setPadding(new Insets(10, 10, 10, 10));
		tableControlPane.setMaxHeight(50d);

		// Table View
		tableView = new TableView();
		stockCodeCol = new TableColumn("Stock Code");
		stockNameCol = new TableColumn("Stock Name");
		stockNameCol.setMinWidth(200);
		marketCol = new TableColumn("Market");
		sectorCol = new TableColumn("Sector");
		stockCodeCol
				.setCellValueFactory(new PropertyValueFactory<>("stockCode"));
		stockNameCol
				.setCellValueFactory(new PropertyValueFactory<>("stockName"));
		marketCol.setCellValueFactory(new PropertyValueFactory<>("market"));
		sectorCol.setCellValueFactory(new PropertyValueFactory<>("sector"));
		tableView.getColumns().addAll(stockCodeCol, stockNameCol, marketCol,
				sectorCol);
		tableView.setPlaceholder(new Label(""));
		tableView.setItems(tableStockData);
		tableView.getSelectionModel().selectedItemProperty()
				.addListener(createTableChangeListener());

		//
		// Layout
		//

		// Top
		HBox topPane = new HBox();
		topPane.setSpacing(10);
		topPane.setAlignment(Pos.CENTER_LEFT);
		topPane.setPadding(new Insets(10, 10, 10, 10));
		createMenu();
		topPane.getChildren().addAll(menuBar);

		// left
		VBox leftPane = new VBox();
		leftPane.getChildren().addAll(treeControlPane, treeView);

		// center
		final SplitPane centerPane = new SplitPane();
		centerPane.setOrientation(Orientation.VERTICAL);
		centerPane.getItems().addAll(tableControlPane, tableView);
		centerPane.setDividerPositions(0.33f, 0.66f, 1.0f);

		// right
		final StackPane rightPane = new StackPane();
		label1 = new Label("Label1");
		rightPane.getChildren().add(label1);

		// middle = left + center + right
		SplitPane middlePane = new SplitPane();
		middlePane.setOrientation(Orientation.HORIZONTAL);
		middlePane.getItems().addAll(leftPane, centerPane, rightPane);
		middlePane.setDividerPositions(0.17f, 0.55f, 1.0f);
		middlePane.setMinSize(600d, 735d);

		// Bottom
		HBox bottomPane = new HBox();
		buttonDummy1 = new Button("Dummy1");
		consoleTextArea = new TextArea();
		consoleTextArea.setMinSize(400d, 50d);
		consoleTextArea.setMaxSize(400d, 50d);
		bottomPane.setSpacing(10);
		bottomPane.setAlignment(Pos.CENTER_LEFT);
		bottomPane.setPadding(new Insets(10, 10, 10, 10));
		bottomPane.getChildren().addAll(consoleTextArea, buttonDummy1);

		// root = top + middle + bottom
		rootPane = new VBox();
		rootPane.getChildren().addAll(topPane, middlePane, bottomPane);

		stage.setScene(new Scene(rootPane, 1200, 870));

		stage.show();
	}

	public void scanMain() {
		try {
			boolean useStockPrice = true;
			boolean useDetailInfo = false;
			boolean useProfileInfo = false;
			allData = initializeCorpsAllData(useStockPrice, selectDataStore(),
					selectCalendarRange(), useDetailInfo, useProfileInfo);
			doScanCorps(allData);
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
				if (oldMarketItem.getValue().toString().equals(market)) {
					currentMarketItem = oldMarketItem;
					foundMarket = true;
					break;
				}
			}
			if (!foundMarket) {
				TreeItem<Object> newMarketItem = new TreeItem<Object>(
						new MarketItemValue(market));
				newMarketItem.setExpanded(true);
				rootItem.getChildren().add(newMarketItem);
				currentMarketItem = newMarketItem;
			}

			// sector
			TreeItem<Object> currentSectorItem = null;
			boolean foundSector = false;
			for (TreeItem<Object> oldSectorItem : currentMarketItem
					.getChildren()) {
				if (oldSectorItem.getValue().toString().equals(sector)) {
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
		menuFile = new Menu("File");
		menuFile.setDisable(true); //
		menuView = new Menu("View");
		menuView.setDisable(true); //
		menuBar.getMenus().addAll(menuFile, menuView);
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
							// System.out.println("MarketItemValue="
							// + value.toString());
						} else if (value instanceof SectorItemValue) {
							// System.out.println("SectorItemValue="
							// + value.toString());
							// TreeItem parent = item.getParent();
							// System.out.println("parent="
							// + parent.getValue().toString());
						} else if (value instanceof StockRecord) {
							// System.out.println("StockRecord");
						} else {
							// System.out.println("unknown item");
						}
					}
				} else if (mouseEvent.getClickCount() == 2) {
					TreeItem item = (TreeItem) tree.getSelectionModel()
							.getSelectedItem();
					if (item != null) {
						Object value = item.getValue();
						// System.out.println("Selected Text : " + value);
						if (value instanceof StockRecord) {
							StockRecord record = (StockRecord) value;
							String tsv = record.toTsvString();
							// System.out.println("record=" + tsv);
							label1.setText(tsv.replace("\t",
									System.lineSeparator()));
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
		if (value instanceof MarketItemValue) {
			tableStockData.clear();
		} else if (value instanceof SectorItemValue) {
			tableStockData.clear();
			item.getChildren()
					.forEach(
							stockItem -> {
								TableStockData stock = stockRecordToStock((StockRecord) stockItem
										.getValue());
								tableStockData.add(stock);
							});
		}
	}

	private TableStockData stockRecordToStock(StockRecord record) {
		return new TableStockData(record.getStockCode(), record.getStockName(),
				record.getMarket(), record.getSector());
	}

	public static class TableStockData {
		private final SimpleStringProperty stockCode;
		private final SimpleStringProperty stockName;
		private final SimpleStringProperty market;
		private final SimpleStringProperty sector;

		private TableStockData(String stockCode, String stockName,
				String market, String sector) {
			this.stockCode = new SimpleStringProperty(stockCode);
			this.stockName = new SimpleStringProperty(stockName);
			this.market = new SimpleStringProperty(market);
			this.sector = new SimpleStringProperty(sector);
		}

		public String getStockCode() {
			return stockCode.get();
		}

		public void setStockCode(String s) {
			stockCode.set(s);
		}

		public String getStockName() {
			return stockName.get();
		}

		public void setStockName(String s) {
			stockName.set(s);
		}

		public String getMarket() {
			return market.get();
		}

		public void setMarket(String s) {
			market.set(s);
		}

		public String getSector() {
			return sector.get();
		}

		public void setSector(String s) {
			sector.set(s);
		}
	}

	public ChangeListener createTableChangeListener() {
		return new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				if (tableView.getSelectionModel().getSelectedItem() != null) {
					TableViewSelectionModel selectionModel = tableView
							.getSelectionModel();
					ObservableList selectedCells = selectionModel
							.getSelectedCells();
					TablePosition tablePosition = (TablePosition) selectedCells
							.get(0);
					Object val = tablePosition.getTableColumn().getCellData(
							newValue);
					System.out.println("Selected Value" + val);
				}
			}
		};
	}
}
