package threading;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import entity.Comment;
import entity.Evenement;
import entity.OutPut;
import entity.Post;
import tools.Algo.ToolBox;

public class Consumer implements Runnable {

	LinkedBlockingQueue<Evenement> queue;
	Map<Long, Post> posts;
	List<Comment> comments;
	List<Post> top3;
	List<Integer> top3IDs;
	List<Post> lastTop3;
	Calendar currentDate;
	ArrayList<Long> idPost;
	Map<Long, ArrayList<Comment>> multiMap;
	Map<Long, Comment> mapComment;
	LinkedHashSet<Evenement> h24;
	LinkedHashSet<Evenement> h48;
	LinkedHashSet<Evenement> h72;
	LinkedHashSet<Evenement> h96;
	LinkedHashSet<Evenement> h120;
	LinkedHashSet<Evenement> h144;
	LinkedHashSet<Evenement> h168;
	LinkedHashSet<Evenement> h192;
	LinkedHashSet<Evenement> h216;
	LinkedHashSet<Evenement> h240;
	ArrayList<LinkedHashSet<Evenement>> timeTable;

	BlockingQueue<OutPut> resultat;
	private boolean isPost = false;

	public Consumer(LinkedBlockingQueue<Evenement> queue, BlockingQueue<OutPut> top3) {
		super();
		this.queue = queue;
		this.multiMap = new HashMap<>(200000, 1);
		this.resultat = top3;
		this.posts = new HashMap<Long, Post>(200000, 1);
		this.top3 = new ArrayList<Post>(3);
		this.lastTop3 = new ArrayList<Post>(3);
		this.mapComment = new HashMap<Long, Comment>(10000, 1);
		this.idPost = new ArrayList<Long>(20000);
		h24 = new LinkedHashSet<Evenement>(10000);
		h48 = new LinkedHashSet<Evenement>(10000);
		h72 = new LinkedHashSet<Evenement>(10000);
		h96 = new LinkedHashSet<Evenement>(10000);
		h120 = new LinkedHashSet<Evenement>(10000);
		h144 = new LinkedHashSet<Evenement>(10000);
		h168 = new LinkedHashSet<Evenement>(10000);
		h192 = new LinkedHashSet<Evenement>(10000);
		h216 = new LinkedHashSet<Evenement>(10000);
		h240 = new LinkedHashSet<Evenement>(10000);
		timeTable = new ArrayList<LinkedHashSet<Evenement>>(12);
		timeTable.add(h24);
		timeTable.add(h48);
		timeTable.add(h72);

		timeTable.add(h96);

		timeTable.add(h120);

		timeTable.add(h144);
		timeTable.add(h168);
		timeTable.add(h192);
		timeTable.add(h216);
		timeTable.add(h240);

	}

	@Override
	public void run() {

		OutPut sortie = null;
		while (true) {

			Evenement event = null;
			try {
				event = queue.take();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {

				if (event.isPoison()) {
					resultat.put(new OutPut(true));
					return;
				}

				// top3.clear();
				sortie = new OutPut(event.getTimestamp());
				fillCommentPostlist(event);
			} catch (Exception e) {

			}

			checkEvent();
			for (int i = 0; i < 3; i++) {
				if (top3.size() > i) {
					sortie.setPost(top3.get(i));

				}

			}

			if (lastTop3.size() != top3.size()) {
				try {
					resultat.put(sortie);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				for (int i = 0; i < top3.size(); i++) {
					if (!lastTop3.get(i).equals(top3.get(i))) {
						try {
							resultat.put(sortie);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					}

				}
			}

			lastTop3 = new ArrayList<Post>(top3);

		}
	}

	public void fillCommentPostlist(Evenement event) {
		Post p;
		timeTable.get(0).add(event);
		if (event instanceof Comment) {
			ToolBox.findRelatedPosts(mapComment, (Comment) event, multiMap);
			isPost = false;

			this.currentDate = event.getTimestamp();
			ToolBox.refreshScore(multiMap, posts, ((Comment) event).getRelatedPost());
			ToolBox.addPostTop(posts.get(((Comment) event).getRelatedPost()), top3);
			posts.get(((Comment) event).getRelatedPost())
					.setNbrComment(posts.get(((Comment) event).getRelatedPost()).getNbrComment() + 1);

		} else if (event instanceof Post) {

			p = (Post) event;

			posts.put(p.getPostId(), p);
			if (top3.size() < 3) {
				top3.add(p);
				top3.sort(new Post(false));
				Collections.reverse(top3);
			} else {

				ToolBox.addPostTop(p, top3);
			}
			idPost.add(p.getPostId());

			isPost = true;
			if (!multiMap.containsKey(p.getPostId()))
				multiMap.put(p.getPostId(), new ArrayList<Comment>(500));

			this.currentDate = event.getTimestamp();

		} else {
			System.out.println("\nInput object is neither a Post or a Comment.\n");
		}
	}

	public List<Post> top3() {
		return this.top3;
	}

	public void checkEvent() {

		for (int i = 0; i < timeTable.size(); i++) {
			LinkedHashSet<Evenement> temp = timeTable.get(i);
			for (Iterator<Evenement> iter = temp.iterator(); iter.hasNext();)

			{
				Evenement tempEv = iter.next();
				if (currentDate.getTimeInMillis() - tempEv.getTimestamp().getTimeInMillis() > (i + 1) * 86400000) {
					if (i < timeTable.size() - 1)
						timeTable.get(i + 1).add(tempEv);
					ToolBox.updateScore(this.top3, tempEv, this.posts, this.multiMap, this.mapComment, i + 1);
					iter.remove();
				} else
					break;
			}

		}

	}

	public Calendar getCalendar() {
		return currentDate;
	}

	public boolean getIsPost() {
		return isPost;
	}

}
