package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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

	private TreeItem<Object> rootItem;

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

		TreeView<Object> tree = new TreeView<Object>(rootItem);
		tree.setOnMouseClicked(createTreeMouseEventHandler(tree));

		//
		// Layout
		//

		StackPane root = new StackPane();
		root.getChildren().add(tree);
		stage.setScene(new Scene(root, 500, 800));
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
				// System.out.println("market=" + market);
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

	public EventHandler<MouseEvent> createTreeMouseEventHandler(TreeView tree) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					TreeItem item = (TreeItem) tree.getSelectionModel()
							.getSelectedItem();
					Object value = item.getValue();

					System.out.println("Selected Text : " + value);
					if (value instanceof StockRecord) {
						StockRecord record = (StockRecord) value;
						System.out.println("record=" + record.toTsvString());
					}
				}
			}
		};
	}
}
