# stockanalysis

# Overview
- For Japanese stock market
- Download daily stock price data from freely available 2 Internet sites
- Download stock split info and stock detail info from Yahoo Finance Japan.
- Read downloaded stock price data and split info into memory, and reflect stock split info to stock price.
- Currently privides samples of simple screening or retrieving.
- No user interface is provided. Changing parameters needs to rewrite program.
- Run on Java8, Eclipse 4.4.2 (Luna).

# Setup
- Install JDK8.
- Install Eclipse4.
- Install apache-maven3.
- Download this project from GitHub.
- Edit stock.properties if you want to change data folder. Default location is as follows.

```
rootFolder=%USERPROFILE%\\StockAnalysis\\data
```

# Dependency
- jsoup (HTML parser)

# Execution

Under construcion......

## Download stock price data from k-db.
- Edit MainDownloadKdb.java and change calendar range as you wish.
- Execute MainDownloadKdb#main().
- Data will be downloaded into folder %USERPROFILE%\StockAnalysis\data\StockPrice\kdb.

## Download stock split data from Yahoo Finance Japan.
- Execute MainDownloadSplitInfoOfAllStocks#main().
- Data will be downloaded into folder %USERPROFILE%\StockAnalysis\data\finance\.

## Download stock detail info from Yahoo Finance Japan
- Execute MainDownloadDetailInfoOfAllStocks#main().
- Data will be downloaded into folder %USERPROFILE%\StockAnalysis\data\finance\.

## Execution of retrieving and screening.
Before executing, you need to download sufficient stock price data and stock split data.
Additionally in some samples, you need to download stock detail data.

If some data is missing or data is old, the result may not be precise.
(Currently data validation function is not provided.)

Class which has the name starts with "Main" contains main() and it is a sample. 

### list of samples
-
-
-


## Download jreit info from jreit portal site.
- This sample is independent from others.
- This sample download HTML table data in Jreit portal page and save to the local tsv (tab separated text).
- Execute MainSaveJreitCsv#main().
- Data will be downloaded into folder %USERPROFILE%\StockAnalysis\data\jreitPortal\.

-----
June, 2015 

