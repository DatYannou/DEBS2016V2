package caseTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import entity.Evenement;
import entity.OutPut;
import threading.Consumer;
import threading.Producer;
import threading.Writer;

public class Case4 {

	Producer threadProducer;
	LinkedBlockingQueue<Evenement> sortie;
	Consumer consumer;
	Writer w1;
	ArrayBlockingQueue<OutPut> toBePrint;

	@Before
	public void setUp() throws Exception {
		sortie = new LinkedBlockingQueue<Evenement>(1000);
		toBePrint = new ArrayBlockingQueue<OutPut>(10);
		threadProducer = new Producer(sortie, "Q1Case4/posts.dat", "Q1Case4/comments.dat");
		consumer = new Consumer(sortie, toBePrint);
		w1 = new Writer(toBePrint);

	}

	@Test
	public void producerConsumer() {

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

	}

}
