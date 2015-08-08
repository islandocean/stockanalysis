package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class AppStockTreeSample extends Application implements
		IScanCorpsTemplate {

	private VBox rootPane;

	private MenuBar menuBar;
	private Menu menuFile;
	private Menu menuView;

	private Button buttonDummy1;
	private Button buttonDummy2;

	private Button buttonTreeCollapse;
	private Button buttonTreeExpand;
	private TreeView<Object> treeView;
	private TreeItem<Object> rootItem;

	private TextField searchTextField;
	private Button buttonTableSearch;
	private Label label0;

	private Label label1;

	private Button buttonDummy3;
	private TextArea consoleTextArea;
	private Button buttonDummy4;

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
		// Tree view
		//

		treeView = new TreeView<Object>(rootItem);
		treeView.setOnMouseClicked(createTreeMouseEventHandler(treeView));
		treeView.setMinHeight(700d);

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
		searchTextField = new TextField();
		tableControlPane.getChildren().addAll(searchTextField,
				buttonTableSearch);
		tableControlPane.setSpacing(10);
		tableControlPane.setAlignment(Pos.CENTER_LEFT);
		tableControlPane.setPadding(new Insets(10, 10, 10, 10));
		tableControlPane.setMaxHeight(50d);

		//
		// Top pane
		//
		HBox topPane = new HBox();
		buttonDummy1 = new Button("Dummy1");
		buttonDummy2 = new Button("Dummy2");
		topPane.setSpacing(10);
		topPane.setAlignment(Pos.CENTER_LEFT);
		topPane.setPadding(new Insets(10, 10, 10, 10));
		createMenu();
		topPane.getChildren().addAll(menuBar, buttonDummy1, buttonDummy2);

		//
		// Bottom pane
		//
		HBox bottomPane = new HBox();
		buttonDummy3 = new Button("Dummy3");
		buttonDummy4 = new Button("Dummy4");
		consoleTextArea = new TextArea();
		consoleTextArea.setMinSize(400d, 50d);
		consoleTextArea.setMaxSize(400d, 50d);
		bottomPane.setSpacing(10);
		bottomPane.setAlignment(Pos.CENTER_LEFT);
		bottomPane.setPadding(new Insets(10, 10, 10, 10));
		bottomPane.getChildren().addAll(buttonDummy3, consoleTextArea,
				buttonDummy4);

		//
		// Layout
		//

		// center
		final SplitPane centerPane = new SplitPane();
		centerPane.setOrientation(Orientation.VERTICAL);
		label0 = new Label("Label 0");
		centerPane.getItems().addAll(tableControlPane, label0,
				new Button("Hi!"));
		centerPane.setDividerPositions(0.33f, 0.66f, 1.0f);

		// right
		final StackPane rightPane = new StackPane();
		label1 = new Label("Label1");
		rightPane.getChildren().add(label1);

		// left
		VBox leftPane = new VBox();
		leftPane.getChildren().addAll(treeControlPane, treeView);

		// middle = left + center + right
		SplitPane middlePane = new SplitPane();
		middlePane.setOrientation(Orientation.HORIZONTAL);
		middlePane.getItems().addAll(leftPane, centerPane, rightPane);
		middlePane.setDividerPositions(0.3f, 0.8f, 1.0f);
		middlePane.setMinSize(600d, 735d);

		// root pane = top + middle + bottom
		rootPane = new VBox();
		rootPane.getChildren().addAll(topPane, middlePane, bottomPane);

		stage.setScene(new Scene(rootPane, 1000, 870));
		stage.show();
	}

	public void scanMain() {
		try {
			boolean useStockPrice = true;
			boolean useDetailInfo = false;
			boolean useProfileInfo = false;
			doScanCorps(useStockPrice, selectDataStore(),
					selectCalendarRange(), useDetailInfo, useProfileInfo);
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
				if (oldMarketItem.getValue().equals(market)) {
					currentMarketItem = oldMarketItem;
					foundMarket = true;
					break;
				}
			}
			if (!foundMarket) {
				TreeItem<Object> newMarketItem = new TreeItem<Object>(market);
				newMarketItem.setExpanded(true);
				rootItem.getChildren().add(newMarketItem);
				currentMarketItem = newMarketItem;
			}

			// sector
			TreeItem<Object> currentSectorItem = null;
			boolean foundSector = false;
			for (TreeItem<Object> oldSectorItem : currentMarketItem
					.getChildren()) {
				if (oldSectorItem.getValue().equals(sector)) {
					currentSectorItem = oldSectorItem;
					foundSector = true;
					break;
				}
			}
			if (!foundSector) {
				TreeItem<Object> newSectorItem = new TreeItem<Object>(sector);
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
		menuView = new Menu("View");
		menuBar.getMenus().addAll(menuFile, menuView);
	}

	public EventHandler<MouseEvent> createTreeMouseEventHandler(TreeView tree) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					TreeItem item = (TreeItem) tree.getSelectionModel()
							.getSelectedItem();
					if (item != null) {
						Object value = item.getValue();
						// System.out.println("Selected Text : " + value);
						if (value instanceof StockRecord) {
							StockRecord record = (StockRecord) value;
							String tsv = record.toTsvString();
							label1.setText(tsv.replace("\t",
									System.lineSeparator()));
						}
					}
				}
			}
		};
	}
}
