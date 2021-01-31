package RxJava.priyanku.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;

import org.apache.logging.log4j.Level;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import yahoofinance.Stock;

public class ObservableDemo1 {
	
	static final Logger logger = LogManager.getLogger(ObservableDemo1.class.getName());

	public static void main(String[] args) {

		logger.info("Processing started");
				
		// Using Fork Join pool does not work with Observable when lambda functions and streams are used in the onNext method
		//var executor = Executors.newWorkStealingPool() ;
		var executor = Executors.newFixedThreadPool(10) ;
		var pooledScheduler = Schedulers.from(executor);
		
		//Async Observable code
		Observable<Stock> stockQuote = new ObservableDemo1().getStockQuote();
		//Observable<Stock> stockQuote = Observable.just(new Stock("sas"), new Stock("dsd"));
		logger.info("Going to Subscribe");
		stockQuote
		//below line is required to run Observable in Async mode using Executor framework
		.subscribeOn(pooledScheduler)
		//below line fails 
		//.subscribeOn(Schedulers.io())
		.subscribe(ObservableDemo1::callBack,
				ObservableDemo1::errorCallBack,
				ObservableDemo1::completeCallBack
				);

		logger.info("Processing completed"); 

	}

	private static void completeCallBack() {
		logger.info("completeCallBack:: Completed Successfully");
	}

	private static void errorCallBack(Throwable throwable) {
		logger.log(Level.ERROR,"errorCallBack:: ", throwable);
	}

	private static Action callBack(Stock stock) {

		logger.info(
				String.format("callBack:: Quote: %s, Price: %s, Day's High: %s, " +
						"Day's Low: %s",
						stock.getSymbol(),
						stock.getQuote().getPrice(),
						stock.getQuote().getDayHigh(),
						stock.getQuote().getDayLow())
				);

		return null;
	}

	private Observable<Stock> getStockQuote() {
		return new StockService().getStock();
	}


}
