package threading;

import java.util.concurrent.BlockingQueue;

import entity.OutPut;

public class Writer implements Runnable {

	private BlockingQueue<OutPut> top3;

	public Writer(BlockingQueue<OutPut> top3) {

		this.top3 = top3;

	}

	public void run() {

		OutPut print;
		String Resultat;
		while (true) {
			while (!top3.isEmpty()) {
				try {
					print = top3.take();
					if (!print.isPoison()) {
						Resultat = print.getDateSortie().getTime().toGMTString();
						if (print.getPost1() != null) {
							Resultat = Resultat + "," + print.getPost1().getPostId() + ","
									+ print.getPost1().getUserName() + "," + print.getPost1().getTotalScore() + ","
									+ print.getPost1().getNbrComment();
						} else {
							Resultat = Resultat + ",-,-,-,-";
						}
						if (print.getPost2() != null) {
							Resultat = Resultat + "," + print.getPost2().getPostId() + ","
									+ print.getPost2().getUserName() + "," + print.getPost2().getTotalScore() + ","
									+ print.getPost2().getNbrComment();
						} else {
							Resultat = Resultat + ",-,-,-,-";
						}

						if (print.getPost3() != null) {
							Resultat = Resultat + "," + print.getPost3().getPostId() + ","
									+ print.getPost3().getUserName() + "," + print.getPost3().getTotalScore() + ","
									+ print.getPost3().getNbrComment();
						} else {
							Resultat = Resultat + ",-,-,-,-";
						}
						System.out.println(Resultat);
					} else {
						return;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	}

}
