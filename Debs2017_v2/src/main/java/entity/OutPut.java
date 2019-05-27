package entity;

import java.util.Calendar;

public class OutPut {

	private Calendar dateSortie;
	private Post post1 = null;
	private Post post2 = null;
	private Post post3 = null;
	int i = 0;
	boolean isPoison = false;

	public OutPut(Calendar dateSortie) {
		super();
		this.dateSortie = dateSortie;
	}

	public OutPut(boolean b) {
		this.isPoison = b;
	}

	public void setPost(Post p) {
		switch (i) {
		case 0:
			this.post1 = p;
			i++;
			break;

		case 1:
			this.post2 = p;
			i++;
			break;

		case 2:
			this.post3 = p;
			i++;
			break;

		default:
			System.err.println("objet Plein");
			break;
		}

	}

	public boolean isPoison() {
		return this.isPoison;
	}

	public Calendar getDateSortie() {
		return dateSortie;
	}

	public void setDateSortie(Calendar dateSortie) {
		this.dateSortie = dateSortie;
	}

	public Post getPost1() {
		return post1;
	}

	public void setPost1(Post post1) {
		this.post1 = post1;
	}

	public Post getPost2() {
		return post2;
	}

	public void setPost2(Post post2) {
		this.post2 = post2;
	}

	public Post getPost3() {
		return post3;
	}

	public void setPost3(Post post3) {
		this.post3 = post3;
	}

}
