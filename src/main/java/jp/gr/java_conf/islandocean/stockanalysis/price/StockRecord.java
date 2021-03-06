package jp.gr.java_conf.islandocean.stockanalysis.price;

import jp.gr.java_conf.islandocean.stockanalysis.enumex.Record;

public class StockRecord extends Record {

	public StockRecord() {
		super(StockEnum.class);
	}

	public void resetAdjustedPrices() {
		this.put(StockEnum.ADJUSTED_OPENING_PRICE,
				this.get(StockEnum.OPENING_PRICE));
		this.put(StockEnum.ADJUSTED_HIGH_PRICE, this.get(StockEnum.HIGH_PRICE));
		this.put(StockEnum.ADJUSTED_LOW_PRICE, this.get(StockEnum.LOW_PRICE));
		this.put(StockEnum.ADJUSTED_CLOSING_PRICE,
				this.get(StockEnum.CLOSING_PRICE));
		this.put(StockEnum.SPLIT_COUNT, 0);
	}

	public void multiplyAdjustedPrices(double mul) {
		Double p;

		p = (Double) this.get(StockEnum.ADJUSTED_OPENING_PRICE);
		if (p != null) {
			this.put(StockEnum.ADJUSTED_OPENING_PRICE, p * mul);
		}

		p = (Double) this.get(StockEnum.ADJUSTED_HIGH_PRICE);
		if (p != null) {
			this.put(StockEnum.ADJUSTED_HIGH_PRICE, p * mul);
		}

		p = (Double) this.get(StockEnum.ADJUSTED_LOW_PRICE);
		if (p != null) {
			this.put(StockEnum.ADJUSTED_LOW_PRICE, p * mul);
		}

		p = (Double) this.get(StockEnum.ADJUSTED_CLOSING_PRICE);
		if (p != null) {
			this.put(StockEnum.ADJUSTED_CLOSING_PRICE, p * mul);
		}

		int splitCount = (int) this.get(StockEnum.SPLIT_COUNT);
		++splitCount;
		this.put(StockEnum.SPLIT_COUNT, splitCount);
	}

	/**
	 * For TreeItem. (TreeItem seems to use Object#toString() to display item in
	 * the tree.)
	 */
	@Override
	public String toString() {
		return (String) this.get(StockEnum.STOCK_NAME);
	}
}
