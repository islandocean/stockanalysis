package jp.gr.java_conf.islandocean.stockanalysis.price;

import java.util.Calendar;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
				CalendarUtil.createCalendarRangeYear(2015) };
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		scene.setFill(Color.ALICEBLUE);
		stage.setScene(scene);
		stage.show();

		stage.setTitle("Stock Price Data Downloader");
		stage.setWidth(500);
		stage.setHeight(300);

		//
		// Label
		//
		Label label = new Label("Select data store and date range to download.");

		//
		// ChoiceBox
		//
		final ChoiceBox storeChoiceBox = new ChoiceBox(
				FXCollections.observableArrayList(dataStoreNames));
		storeChoiceBox.getSelectionModel().select(0);
		final ChoiceBox rangeChoiceBox = new ChoiceBox(
				FXCollections.observableArrayList("recent 7 days",
						"recent 14 days", "recent 30 days", "recent 90 days",
						"recent 180 days", "recent 365 days", "2007", "2008",
						"2009", "2010", "2011", "2012", "2013", "2014", "2015"));

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

		//
		// Buttons
		//
		Button buttonOk = new Button("Start Download");
		buttonOk.setOnAction((ActionEvent e) -> {
			int selectedIndexOfRange = rangeChoiceBox.getSelectionModel()
					.getSelectedIndex();
			CalendarRange calendarRange = calendarRanges()[selectedIndexOfRange];
			int selectedIndexOfStore = storeChoiceBox.getSelectionModel()
					.getSelectedIndex();
			Class storeClass = dataStoreClasses[selectedIndexOfStore];
			download(calendarRange, storeClass);
		});
		Button buttonCancel = new Button("Exit");
		buttonCancel.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});
		HBox hBox = new HBox();
		hBox.getChildren().addAll(buttonOk, buttonCancel);
		hBox.setSpacing(30);
		hBox.setAlignment(Pos.CENTER);
		hBox.setPadding(new Insets(10, 0, 0, 10));

		//
		// Layout
		//
		VBox vBox = new VBox();
		vBox.getChildren().add(label);
		vBox.getChildren().add(storeChoiceBox);
		vBox.getChildren().add(rangeChoiceBox);
		vBox.getChildren().add(hBox);
		((Group) scene.getRoot()).getChildren().add(vBox);
	}

	private void download(CalendarRange calendarRange, Class storeClass) {
		DataStore store = null;
		try {
			store = (DataStore) storeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Calendar begin = calendarRange.getBegin();
		Calendar end = calendarRange.getEnd();
		System.out.println("begin=" + CalendarUtil.format_yyyyMMdd(begin));
		System.out.println("end=" + CalendarUtil.format_yyyyMMdd(end));

		int count = store.download(calendarRange, null);
		System.out.println("count=" + count);
	}
}
