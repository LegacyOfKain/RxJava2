package RxJava.priyanku.Observable;

import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ObservableSchedulerDemo {

	static final Logger logger = LogManager.getLogger(ObservableSchedulerDemo.class.getName());
	
	public static void main(String[] args) {

		//var executor = Executors.newWorkStealingPool() ;
		var executor = Executors.newFixedThreadPool(10);
		var pooledScheduler = Schedulers.from(executor);
		
		// One observalbe runs on one thread only, so if you spawn multiple observables it will spawn multiple threads
		logger.info("Started");
		Observable.just(1,2,3,4)
	    .subscribeOn(pooledScheduler)
	 
	    .subscribe(s->logger.info(s));
		
		logger.info("Interim");
		Observable.just( 5,6, 7,8, 9)
	    .subscribeOn(pooledScheduler)
	    .subscribe(s->logger.info(s));
		
		Observable.just( 35,36, 37,38, 39)
	    .subscribeOn(pooledScheduler)
	    .subscribe(s->logger.info(s));
		
		Observable.just( 15,16, 17,18, 19)
	    .subscribeOn(pooledScheduler)
	    .subscribe(s->logger.info(s));
		
		logger.info("Finished");

	}

}
