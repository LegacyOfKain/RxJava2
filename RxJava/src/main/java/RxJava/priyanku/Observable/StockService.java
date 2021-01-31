package RxJava.priyanku.Observable;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/*
 * Mock Service
 */
public class StockService {

	static final Logger logger = LogManager.getLogger(StockService.class.getName());
	
    private static String[] quotes = {"AAPL", "GOOG", "INTC", "BABA", "TSLA", "AIR.PA"};

    public Observable<Stock> getStock() {

        return Observable.create(subscriber -> {
            if (!subscriber.isDisposed()) {
                Arrays.stream(quotes)
                        .map(quote -> getStock(quote, subscriber))
                        .filter(Objects::nonNull)
                        .forEach(stock -> {
                            try {
								subscriber.onNext(stock);
							} catch (Exception e) {
								subscriber.onError(e);
							}
                            sleep(1000);
                            //below line not required in rxjava 2
                            //subscriber.onError(new RuntimeException("exception"));
                        });

            }
            subscriber.onComplete();
        });
    }

    private void sleep(int i) {
        try {
        	logger.info("Sleeping...");
            Thread.sleep(i);
        } catch (InterruptedException e) {
        	logger.log(Level.ERROR, "InterruptedException", e);
        }
    }

    private Stock getStock(String quote, ObservableEmitter<Stock> subscriber) {

    	logger.info("service:: Retrieve stock info for: " + quote);
    	logger.info("subscriber Disposed: " + subscriber.isDisposed());
        try {
            if (quote.equals("GOOG")) {
                throw new IOException("Hye");
            }
            return YahooFinance.get(quote);
        } catch (IOException e) {
            subscriber.onError(e);
        }
        return null;
    }
}