package jp.gr.java_conf.islandocean.stockanalysis.app;

import java.util.List;

import jp.gr.java_conf.islandocean.stockanalysis.finance.FinanceManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.DataStore;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockManager;
import jp.gr.java_conf.islandocean.stockanalysis.price.StockRecord;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;

public class CorpsAllData {
	private boolean useStockPrice;
	private DataStore store;
	private CalendarRange calendarRange;
	private boolean useDetailInfo;
	private boolean useProfileInfo;

	private StockManager stockManager;
	private List<StockRecord> lastData;
	private FinanceManager financeManager;
	private String[] stockCodes;

	public CorpsAllData(boolean useStockPrice, DataStore store,
			CalendarRange calendarRange, boolean useDetailInfo,
			boolean useProfileInfo) {
		super();
		this.useStockPrice = useStockPrice;
		this.store = store;
		this.calendarRange = calendarRange;
		this.useDetailInfo = useDetailInfo;
		this.useProfileInfo = useProfileInfo;
	}

	public boolean getUseStockPrice() {
		return useStockPrice;
	}

	public void setUseStockPrice(boolean useStockPrice) {
		this.useStockPrice = useStockPrice;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public CalendarRange getCalendarRange() {
		return calendarRange;
	}

	public void setCalendarRange(CalendarRange calendarRange) {
		this.calendarRange = calendarRange;
	}

	public boolean getUseDetailInfo() {
		return useDetailInfo;
	}

	public void setUseDetailInfo(boolean useDetailInfo) {
		this.useDetailInfo = useDetailInfo;
	}

	public boolean getUseProfileInfo() {
		return useProfileInfo;
	}

	public void setUseProfileInfo(boolean useProfileInfo) {
		this.useProfileInfo = useProfileInfo;
	}

	public StockManager getStockManager() {
		return stockManager;
	}

	public void setStockManager(StockManager stockManager) {
		this.stockManager = stockManager;
	}

	public List<StockRecord> getLastData() {
		return lastData;
	}

	public void setLastData(List<StockRecord> lastData) {
		this.lastData = lastData;
	}

	public FinanceManager getFinanceManager() {
		return financeManager;
	}

	public void setFinanceManager(FinanceManager financeManager) {
		this.financeManager = financeManager;
	}

	public String[] getStockCodes() {
		return stockCodes;
	}

	public void setStockCodes(String[] stockCodes) {
		this.stockCodes = stockCodes;
	}
}
