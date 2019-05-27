package entity;

import java.util.Calendar;

public class Comment implements Evenement {

	private Calendar timestamp;
	private long commentId;
	private long userId;
	private String content;
	private String userName;
	private long commentReplied;
	private long postCommented;
	private long relatedPost;
	private boolean poison = false;
	private int score = 10;

	public Comment(Calendar timestamp, long commentId, long userId, String content, String userName,
			long commentReplied, long postCommented) {
		super();
		this.timestamp = timestamp;
		this.commentId = commentId;
		this.userId = userId;
		this.content = content;
		this.userName = userName;
		this.commentReplied = commentReplied;
		this.postCommented = postCommented;
		if (postCommented != -1) {
			this.relatedPost = postCommented;
		} else {
			this.relatedPost = -1;

		}

	}

	public Comment(boolean poison) {
		this.poison = poison;
	}

	public Comment(Comment c) {

		this.timestamp = c.timestamp;
		this.commentId = c.commentId;
		this.userId = c.userId;
		this.content = c.content;
		this.userName = c.userName;
		this.commentReplied = c.commentReplied;
		this.postCommented = c.postCommented;
		this.relatedPost = c.relatedPost;
		this.poison = c.poison;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		long result = 1;
		result = prime * result + commentId;
		result = prime * result + commentReplied;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + postCommented;
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + userId;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return (int) result;
	}

	@Override
	public String toString() {
		return "Comment [timestamp=" + timestamp.getTime().toGMTString() + ", commentId=" + commentId + ", userId="
				+ userId + ", content=" + content + ", userName=" + userName + ", commentReplied=" + commentReplied
				+ ", postCommented=" + postCommented + ", relatedPost=" + relatedPost + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (commentId != other.commentId)
			return false;
		if (commentReplied != other.commentReplied)
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (postCommented != other.postCommented)
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

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
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

	public long getCommentReplied() {
		return commentReplied;
	}

	public void setCommentReplied(int commentReplied) {
		this.commentReplied = commentReplied;
	}

	public long getPostCommented() {
		return postCommented;
	}

	public void setPostCommented(int postCommented) {
		this.postCommented = postCommented;
	}

	public long getRelatedPost() {
		return relatedPost;
	}

	public void setRelatedPost(long relatedPost) {
		this.relatedPost = relatedPost;
	}

	@Override
	public boolean isPoison() {
		return poison;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
