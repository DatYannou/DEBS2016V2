package threadingTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import entity.Evenement;
import entity.OutPut;
import threading.Consumer;
import threading.Producer;
import threading.Writer;

public class ThreadingTest {

	Producer threadProducer;
	LinkedBlockingQueue<Evenement> sortie;
	Consumer consumer;
	Writer w1;
	ArrayBlockingQueue<OutPut> toBePrint;

	@Before
	public void setUp() throws Exception {
		sortie = new LinkedBlockingQueue<Evenement>();
		toBePrint = new ArrayBlockingQueue<OutPut>(10);
		threadProducer = new Producer(sortie, "posts.dat", "comments.dat");
		consumer = new Consumer(sortie, toBePrint);
		w1 = new Writer(toBePrint);

	}

	@Test
	public void producerConsumer() {

		long start = System.nanoTime();

		Thread prod = new Thread(threadProducer);
		Thread cons = new Thread(consumer);
		Thread writ = new Thread(w1);

		writ.start();
		prod.start();
		cons.start();

		try {
			prod.join();
			cons.join();
			writ.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.nanoTime();

		System.out.println(end - start);

	}

}
