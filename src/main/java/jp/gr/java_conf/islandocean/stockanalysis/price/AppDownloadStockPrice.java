package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.io.IOException;
import java.util.Calendar;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarRange;
import jp.gr.java_conf.islandocean.stockanalysis.util.CalendarUtil;

public class AppDownloadStockPrice extends Application {

	public AppDownloadStockPrice() {
		super();
	}

	private static String[] dataStoreNames = { "kdb(株価データダウンロードサイト)",
			"souko(株価データ倉庫)" };
	private static Class[] dataStoreClasses = { DataStoreKdb.class,
			DataStoreSouko.class };

	private static CalendarRange[] calendarRanges() {
		return new CalendarRange[] {
				CalendarUtil.createCalendarRangeRecentDays(7),
				CalendarUtil.createCalendarRangeRecentDays(14),
				CalendarUtil.createCalendarRangeRecentDays(30),
				CalendarUtil.createCalendarRangeRecentDays(90),
				CalendarUtil.createCalendarRangeRecentDays(180),
				CalendarUtil.createCalendarRangeRecentDays(365),
				CalendarUtil.createCalendarRangeYear(2007),
				CalendarUtil.createCalendarRangeYear(2008),
				CalendarUtil.createCalendarRangeYear(2009),
				CalendarUtil.createCalendarRangeYear(2010),
				CalendarUtil.createCalendarRangeYear(2011),
				CalendarUtil.createCalendarRangeYear(2012),
				CalendarUtil.createCalendarRangeYear(2013),
				CalendarUtil.createCalendarRangeYear(2014),
				CalendarUtil.createCalendarRangeYear(2015),
				CalendarUtil.createCalendarRangeYear(2016) };
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		DownloadService service = new DownloadService();
		Button buttonStart = new Button("Start Download");
		Button buttonStop = new Button("Stop");
		Button buttonReset = new Button("Reset State");
		Button buttonInfo = new Button("Console Out");
		Button buttonExit = new Button("Exit");
		ProgressBar bar = new ProgressBar();

		//
		// Service
		//

		service.setOnReady(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent ev) {
				System.out.println("### service.setOnReady handle()");
				printInfo(ev.getSource());
			}
		});

		service.setOnRunning(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent ev) {
				System.out.println("### service.setOnRunning handle()");
				printInfo(ev.getSource());
			}
		});

		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent ev) {
				System.out.println("### service.setOnSucceeded handle()");
				printInfo(ev.getSource());
				buttonStart.setDisable(true);
				buttonStop.setDisable(true);
				buttonReset.setDisable(false);
			}
		});

		service.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent ev) {
				System.out.println("### service.setOnCancelled handle()");
				printInfo(ev.getSource());
				buttonStart.setDisable(true);
				buttonStop.setDisable(true);
				buttonReset.setDisable(false);
			}
		});

		service.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent ev) {
				System.out.println("### service.setOnFailed handle()");
				printInfo(ev.getSource());
				buttonStart.setDisable(true);
				buttonStop.setDisable(true);
				buttonReset.setDisable(false);
			}
		});

		//
		// GUI
		//

		Scene scene = new Scene(new Group());
		scene.setFill(Color.ALICEBLUE);
		stage.setScene(scene);
		stage.show();

		stage.setTitle("Stock Price Data Downloader");
		stage.setWidth(850);
		stage.setHeight(300);

		// Label
		Label label = new Label("Select data store and date range to download.");
		label.setPadding(new Insets(10, 0, 0, 10));
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.web("#0076a3"));

		// ChoiceBox
		final ChoiceBox storeChoiceBox = new ChoiceBox(
				FXCollections.observableArrayList(dataStoreNames));
		storeChoiceBox.getSelectionModel().select(0);
		final ChoiceBox rangeChoiceBox = new ChoiceBox(
				FXCollections.observableArrayList("recent 7 days",
						"recent 14 days", "recent 30 days", "recent 90 days",
						"recent 180 days", "recent 365 days", "2007", "2008",
						"2009", "2010", "2011", "2012", "2013", "2014", "2015",
						"2016"));

		rangeChoiceBox
				.getSelectionModel()
				.selectedIndexProperty()
				.addListener(
						(ObservableValue<? extends Number> ov, Number old_val,
								Number new_val) -> {
							// label.setText(greetings[new_val.intValue()]);
						});
		rangeChoiceBox.setTooltip(new Tooltip(
				"Select calendar range to download"));
		rangeChoiceBox.getSelectionModel().select(0);

		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.add(new Label("Data Store: "), 0, 0);
		grid.add(storeChoiceBox, 1, 0);
		grid.add(new Label("Date Range: "), 0, 1);
		grid.add(rangeChoiceBox, 1, 1);

		// Buttons
		buttonStart
				.setOnAction((ActionEvent e) -> {
					buttonStart.setDisable(true);
					buttonStop.setDisable(false);
					buttonReset.setDisable(true);
					bar.setVisible(true);
					int selectedIndexOfRange = rangeChoiceBox
							.getSelectionModel().getSelectedIndex();
					CalendarRange calendarRange = calendarRanges()[selectedIndexOfRange];
					int selectedIndexOfStore = storeChoiceBox
							.getSelectionModel().getSelectedIndex();
					Class storeClass = dataStoreClasses[selectedIndexOfStore];
					DataStore store = null;
					try {
						store = (DataStore) storeClass.newInstance();
					} catch (InstantiationException | IllegalAccessException e1) {
						e1.printStackTrace();
						throw new RuntimeException(e1);
					}

					service.setCalendarRange(calendarRange);
					service.setDataStore(store);
					service.start();
				});
		buttonStop.setOnAction((ActionEvent e) -> {
			service.cancel();
		});
		buttonStop.setDisable(true);
		buttonReset.setOnAction((ActionEvent e) -> {
			service.reset();
			buttonStart.setDisable(false);
			buttonStop.setDisable(true);
			bar.setVisible(false);
		});
		buttonInfo.setOnAction((ActionEvent e) -> {
			System.out.println("### Console out");
			printInfo(service);
		});
		buttonExit.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});
		HBox hBoxButtons = new HBox();
		hBoxButtons.getChildren().addAll(buttonStart, buttonStop, buttonReset,
				buttonInfo, buttonExit);
		hBoxButtons.setSpacing(30);
		hBoxButtons.setAlignment(Pos.CENTER_LEFT);
		hBoxButtons.setPadding(new Insets(10, 0, 0, 10));

		// Message
		HBox hBoxMessage = new HBox();
		hBoxMessage.setPadding(new Insets(10, 0, 0, 10));
		Label CaptionMessage = new Label("Message:");
		Label labelMessage = new Label();
		hBoxMessage.getChildren().addAll(CaptionMessage, labelMessage);
		labelMessage.textProperty().bind(service.messageProperty());

		// Title
		HBox hBoxTitle = new HBox();
		hBoxTitle.setPadding(new Insets(10, 0, 0, 10));
		Label CaptionTitle = new Label("Title:");
		Label labelTitle = new Label();
		hBoxTitle.getChildren().addAll(CaptionTitle, labelTitle);
		labelTitle.textProperty().bind(service.titleProperty());

		// Value
		HBox hBoxValue = new HBox();
		hBoxValue.setPadding(new Insets(10, 0, 0, 10));
		Label CaptionValue = new Label("Value:");
		Label labelValue = new Label();
		hBoxValue.getChildren().addAll(CaptionValue, labelValue);
		labelValue.textProperty().bind(service.valueProperty());

		// Progress Bar
		bar.setPadding(new Insets(10, 0, 0, 10));
		bar.setMinSize(400, 25);
		bar.progressProperty().bind(service.progressProperty());
		bar.setVisible(false);

		// Layout
		VBox vBox = new VBox();
		vBox.getChildren().add(label);
		vBox.getChildren().add(grid);
		vBox.getChildren().add(hBoxButtons);
		vBox.getChildren().add(hBoxTitle);
		vBox.getChildren().addAll(hBoxMessage);
		vBox.getChildren().add(hBoxValue);
		vBox.getChildren().add(bar);
		((Group) scene.getRoot()).getChildren().add(vBox);
	}

	private class DownloadService extends Service<String> {
		private DataStore dataStore;
		private CalendarRange calendarRange;

		@Override
		protected Task<String> createTask() {
			DownloadTask task = new DownloadTask();
			task.setDataStore(dataStore);
			task.setCalendarRange(calendarRange);
			return task;
		}

		public DataStore getDataStore() {
			return dataStore;
		}

		public void setDataStore(DataStore dataStore) {
			this.dataStore = dataStore;
		}

		public CalendarRange getCalendarRange() {
			return calendarRange;
		}

		public void setCalendarRange(CalendarRange calendarRange) {
			this.calendarRange = calendarRange;
		}
	}

	public class DownloadTask extends Task<String> {
		private DataStore dataStore;
		private CalendarRange calendarRange;

		public DownloadTask() {
			super();
		}

		@Override
		protected String call() throws IOException {
			Calendar begin = calendarRange.getBegin();
			Calendar end = calendarRange.getEnd();
			System.out.println("begin=" + CalendarUtil.format_yyyyMMdd(begin));
			System.out.println("end=" + CalendarUtil.format_yyyyMMdd(end));

			DownloadTask task = this;
			int count = dataStore.download(calendarRange, task);
			System.out.println("count=" + count);

			return Integer.toOctalString(count);
		}

		public void upMessage(String s) {
			updateMessage(s);
		}

		public void upProgress(double d1, double d2) {
			updateProgress(d1, d2);
		}

		public void upTitle(String s) {
			updateTitle(s);
		}

		public void upValue(String s) {
			updateValue(s);
		}

		public DataStore getDataStore() {
			return dataStore;
		}

		public void setDataStore(DataStore dataStore) {
			this.dataStore = dataStore;
		}

		public CalendarRange getCalendarRange() {
			return calendarRange;
		}

		public void setCalendarRange(CalendarRange calendarRange) {
			this.calendarRange = calendarRange;
		}
	}

	private void printInfo(Worker worker) {
		System.out.println("getValue():" + worker.getValue());
		System.out.println("getMessage():" + worker.getMessage());
		System.out.println("getProgress():" + worker.getProgress());
		System.out.println("getTitle():" + worker.getTitle());
		System.out.println("getWorkDone():" + worker.getWorkDone());
		System.out.println("getTotalWork():" + worker.getTotalWork());
		System.out.println("getState():" + worker.getState());
		System.out.println("---------------------------------");
	}
}
