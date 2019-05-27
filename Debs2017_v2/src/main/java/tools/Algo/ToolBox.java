package tools.Algo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entity.Comment;
import entity.Evenement;
import entity.Post;

public class ToolBox {

	public static ArrayList<Integer> decreaseScore(List<Post> input, List<Integer> score) {
		List<Integer> scoreCopy;
		ArrayList<Integer> top3 = new ArrayList<Integer>(3);
		Calendar last = input.get(input.size() - 1).getTimestamp();
		for (int i = 0; i < input.size() - 1; i++) {
			long timePassed = last.getTimeInMillis() - input.get(i).getTimestamp().getTimeInMillis();

			int decrease = (int) (timePassed / 86400000);

			if (decrease >= score.get(i)) {
				score.remove(i);
				input.remove(i);
				i--;
			} else {

				score.set(i, score.get(i) - decrease);

			}
		}

		scoreCopy = new ArrayList<Integer>(score);

		int index;
		int maxScore;
		for (int i = 0; i < 3; i++) {
			maxScore = Collections.max(scoreCopy);

			if (maxScore <= 0) {
				scoreCopy.clear();
				return top3;
			} else {
				index = scoreCopy.indexOf(maxScore);
				scoreCopy.set(index, 0);
				top3.add(index);
			}
		}

		scoreCopy.clear();

		return top3;

	}

	public static List<Post> topCreation(Map<Long, Post> posts, Calendar currentDate, boolean isPost,
			Map<Long, ArrayList<Comment>> multiMap) {
		List<Post> top3 = new ArrayList<Post>(3);
		Post p1 = null;
		Post p2 = null;
		if (!posts.isEmpty()) {

			if (posts.size() == 1) {
				Entry<Long, Post> entry = posts.entrySet().iterator().next();

				top3.add(entry.getValue());

			} else if (posts.size() == 2) {
				Post p = Collections.max(posts.entrySet(), (entry1, entry2) -> entry2.getValue().getTimestamp()
						.compareTo(entry1.getValue().getTimestamp())).getValue();

				top3.add(p);
				Post p21 = Collections.min(posts.entrySet(), (entry1, entry2) -> entry2.getValue().getTimestamp()
						.compareTo(entry1.getValue().getTimestamp())).getValue();

				top3.add(p21);

			} else {
				for (int i = 0; i < 3; i++) {

					Long max = Collections.max(posts.entrySet(), (entry1, entry2) -> entry2.getValue().getTimestamp()
							.compareTo(entry1.getValue().getTimestamp())).getKey();

					if (i == 0) {
						p1 = posts.get(max);
						posts.remove(max);
					} else if (i == 1) {

						p2 = posts.get(max);
						posts.remove(max);
					}
					top3.add(posts.get(max));
				}
				posts.put(p1.getPostId(), p1);
				posts.put(p2.getPostId(), p2);
			}

		}

		return top3;

	}

	public static void countCommentListScore(Post p, ArrayList<Comment> arComment, Calendar currentDate) {
		Comment temp;
		long timePassed;
		int decrease;
		for (int j = arComment.size() - 1; j >= 0; j--) {

			temp = arComment.get(j);
			if (temp.getScore() > 0) {
				timePassed = currentDate.getTimeInMillis() - temp.getTimestamp().getTimeInMillis();
				decrease = (int) (timePassed / 86400000);

				if (decrease >= 10 && arComment.size() > 1) {
					decrease = 10;
					arComment.remove(j);
				} else {

					temp.setScore(10 - decrease);
					p.setScore(temp.getScore() + p.getScore());
				}
			} else {

				return;

			}

		}
	}

	public static void refreshScore(Map<Long, ArrayList<Comment>> multiMap, Map<Long, Post> posts, Long Key) {
		ArrayList<Comment> arr = multiMap.get(Key);
		Post entry = posts.get(Key);
		entry.setTotalScore(entry.getScore());
		for (int i = 0; i < arr.size(); i++) {
			entry.setTotalScore(entry.getTotalScore() + arr.get(i).getScore());
		}
	}

	public static void updateScore(List<Post> top3, Evenement e, Map<Long, Post> posts,
			Map<Long, ArrayList<Comment>> multiMap, Map<Long, Comment> comments, int a) {
		Post entry = null;
		if (e instanceof Comment) {
			Comment etemp = (Comment) e;
			etemp = comments.get(etemp.getCommentId());
			ArrayList<Comment> arr = multiMap.get(etemp.getRelatedPost());
			etemp = arr.get(arr.indexOf(etemp));
			etemp.setScore(10 - a);
			entry = posts.get(etemp.getRelatedPost());
			entry.setTotalScore(entry.getScore());

			if (etemp.getScore() <= 0) {
				arr.remove(arr.indexOf(etemp));
				comments.remove(etemp.getCommentId());
			}
			for (int i = 0; i < arr.size(); i++) {
				entry.setTotalScore(entry.getTotalScore() + arr.get(i).getScore());
			}

			if (entry.getTotalScore() <= 0) {
				multiMap.remove(entry.getPostId());
				posts.remove(entry.getPostId());
			}

		} else if (e instanceof Post) {

			Post etemp = (Post) e;
			entry = posts.get(etemp.getPostId());
			entry.setScore(10 - a);
			entry.setTotalScore(entry.getScore());
			ArrayList<Comment> arr = multiMap.get(entry.getPostId());
			for (int i = 0; i < arr.size(); i++) {
				entry.setTotalScore(entry.getTotalScore() + arr.get(i).getScore());
			}

			if (entry.getTotalScore() <= 0) {
				multiMap.remove(entry.getPostId());
				posts.remove(entry.getPostId());
			}

		}
		if (entry.getTotalScore() <= 0) {
			posts.remove(entry.getPostId());
			multiMap.remove(entry.getPostId());
		} else {

			ToolBox.addPostTop(entry, top3);
		}

	}

	public static void addPostTop(Post entry, List<Post> top3) {

		Post top3min = top3.get(top3.size() - 1);
		if (top3min.compareTo(entry) == -1 && !top3.contains(entry)) {
			top3.set(top3.indexOf(top3min), entry);
			top3.sort(new Post(false));
			Collections.reverse(top3);
		} else {
			if (top3.contains(top3min)) {
				top3.sort(new Post(false));
				Collections.reverse(top3);
			}
		}

	}

	public static void countScore(ArrayList<Long> idPost, List<Post> top3, Map<Long, Post> posts, Calendar currentDate,
			boolean isPost, Map<Long, ArrayList<Comment>> multiMap) {
		double timePassed;
		int decrease;
		ArrayList<Comment> arComment;

		for (int i = 0; i < idPost.size(); i++) {
			Post entry = posts.get(idPost.get(i));

			if (!entry.isDead()) {
				timePassed = currentDate.getTimeInMillis() - entry.getTimestamp().getTimeInMillis();
				decrease = (int) (timePassed / 86400000);

				if (decrease >= 10) {
					entry.setDead(true);
					decrease = 10;
				}

				entry.setScore(10 - decrease);
			} else {
				entry.setScore(0);
			}

			arComment = multiMap.get(entry.getPostId());

			if (!arComment.isEmpty())
				countCommentListScore(entry, arComment, currentDate);

			if (entry.getScore() <= 0) { // Checks if a post is dead (score down to 0)
				Long swap = idPost.get(idPost.size() - 1);
				idPost.set(idPost.size() - 1, idPost.get(i));
				idPost.set(i, swap);
				multiMap.remove(entry.getPostId());
				posts.remove(entry.getPostId());
				idPost.remove(idPost.size() - 1);

			} else {
				if (top3.size() > 2) {
					Post top3min = top3.get(top3.size() - 1);
					if (top3min.compareTo(entry) == -1) {
						top3.set(top3.indexOf(top3min), entry);
						top3.sort(new Post(false));
						Collections.reverse(top3);
					}
				} else {
					top3.add(entry);
				}

			}

		}
	}

	public static void findRelatedPosts(Map<Long, Comment> hashCom, Comment c, Map<Long, ArrayList<Comment>> hashmap) {
		Comment Ctemp;

		if (c.getRelatedPost() == -1) {
			Ctemp = hashCom.get(c.getCommentReplied());
			c.setRelatedPost(Ctemp.getRelatedPost());

		}

		hashCom.put(c.getCommentId(), c);

		ArrayList<Comment> al = hashmap.get(c.getRelatedPost());
		al.add(c);

	}

	public static ArrayList<Integer> CountScoreComment(ArrayList<Comment> comments) {

		return null;
	}

}
