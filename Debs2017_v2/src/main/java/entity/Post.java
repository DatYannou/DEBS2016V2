package entity;

import java.util.Calendar;
import java.util.Comparator;

public class Post implements Evenement, Comparator<Post>, Comparable<Post> {

	private Calendar timestamp;
	private long postId;
	private long userId;
	private String content;
	private String userName;
	private int score;
	private boolean isPoison = false;
	private boolean isDead = false;
	private int totalScore = 10;
	private int nbrComment;

	public Post(Calendar timestamp, long postId, long userId, String content, String userName) {
		super();
		this.timestamp = timestamp;
		this.postId = postId;
		this.userId = userId;
		this.content = content;
		this.userName = userName;
		this.score = 10;
		this.nbrComment = 0;
	}

	public Post(boolean t) {
		isPoison = t;
	}

	@Override
	public String toString() {
		return "Post [timestamp=" + timestamp.getTime().toGMTString() + ", postId=" + postId + ", userId=" + userId
				+ ", content=" + content + ", userName=" + userName + ", score=" + score + "]";
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + (isPoison ? 1231 : 1237);
		result = prime * result + (int) (postId ^ (postId >>> 32));
		result = prime * result + score;
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (isPoison != other.isPoison)
			return false;
		if (postId != other.postId)
			return false;
		if (score != other.score)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (userId != other.userId)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public boolean isPoison() {
		return this.isPoison;
	}

	@Override
	public int compare(Post o1, Post o2) {
		if ((o1.getTotalScore() > o2.getTotalScore())
				|| (o1.getTotalScore() == o2.getTotalScore() && o1.getTimestamp().compareTo(o2.getTimestamp()) == 1))
			return 1;
		else if ((o1.getTotalScore() < o2.getTotalScore())
				|| (o1.getTotalScore() == o2.getTotalScore() && o1.getTimestamp().compareTo(o2.getTimestamp()) == -1))
			return -1;
		return 0;

	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	@Override
	public int compareTo(Post o) {

		if ((this.getTotalScore() > o.getTotalScore())
				|| (this.getTotalScore() == o.getTotalScore() && this.getTimestamp().compareTo(o.getTimestamp()) == 1))
			return 1;
		else if ((this.getTotalScore() < o.getTotalScore())
				|| (this.getTotalScore() == o.getTotalScore() && this.getTimestamp().compareTo(o.getTimestamp()) == -1))
			return -1;

		return 0;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getNbrComment() {
		return nbrComment;
	}

	public void setNbrComment(int nbrComment) {
		this.nbrComment = nbrComment;
	}

}
