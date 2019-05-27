package threading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

import entity.Comment;
import entity.Evenement;
import entity.Post;

public class Producer implements Runnable {

	private File post;
	private File comment;
	private LinkedBlockingQueue<Evenement> timeline;

	public Producer(LinkedBlockingQueue<Evenement> input, String post, String comment) {
		this.timeline = input;
		this.post = getFileFromResources(post);
		this.comment = getFileFromResources(comment);

	}

	@Override
	public void run() {

		try (BufferedReader brPost = new BufferedReader(new FileReader(post))) {

			try (BufferedReader brComment = new BufferedReader(new FileReader(comment))) {
				String linePost = null;
				String lineComment = null;
				Post postCompare = null;
				Comment commentCompare;
				while (true) {
					try {

						lineComment = brComment.readLine();
						linePost = brPost.readLine();

						if ((lineComment == null || lineComment.isEmpty())
								&& (linePost == null || linePost.isEmpty())) {
							Comment poison = new Comment(true);
							// try {
							try {
								timeline.put(poison);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							return;

						}

						while (lineComment != null && linePost != null && !lineComment.isEmpty()
								&& !linePost.isEmpty()) {
							commentCompare = commentParser(lineComment);
							postCompare = postParser(linePost);
							while (commentCompare.getTimestamp().compareTo(postCompare.getTimestamp()) == 1
									|| commentCompare.getTimestamp().compareTo(postCompare.getTimestamp()) == 0) {

								try {
									timeline.put(postCompare);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (((linePost = brPost.readLine()) != null)) {
									postCompare = postParser(linePost);
								} else {
									break;
								}

							}
							while (commentCompare.getTimestamp().compareTo(postCompare.getTimestamp()) == -1) {
								try {
									timeline.put(commentCompare);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								if ((lineComment = brComment.readLine()) != null) {
									commentCompare = commentParser(lineComment);
								} else {
									break;
								}
							}

						}
						if (lineComment != null && !lineComment.isEmpty() && (linePost == null || linePost.isEmpty())) {
							commentCompare = commentParser(lineComment);
							try {
								timeline.put(commentCompare);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else if (linePost != null && !linePost.isEmpty()
								&& (lineComment == null || lineComment.isEmpty())) {
							postCompare = postParser(linePost);
							linePost = null;
							try {
								timeline.put(postCompare);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}

					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public File getFileFromResources(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();

		java.net.URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}
	}

	public Post postParser(String Line) throws ParseException {
		String[] line = Line.split("\\|");
		Post post;
		post = new Post(getTimeStamp(line[0]), getPostId(line[1]), getUserId(line[2]), getContent(line[3]),
				getUserName(line[4]));

		return post;
	}

	public Comment commentParser(String Line) throws NumberFormatException, ParseException {
		String[] line = Line.split("\\|");
		Comment comment;
		if (line[5].isEmpty()) {
			line[5] = "-1";
		}
		if (line.length < 7) {
			line = Arrays.copyOf(line, line.length + 1);
			line[6] = "-1";

		}
		comment = new Comment(getTimeStamp(line[0]), getPostId(line[1]), getUserId(line[2]), getContent(line[3]),
				line[4], Long.parseLong(line[5]), Long.parseLong(line[6]));

		return comment;
	}

	public Calendar getTimeStamp(String S) throws ParseException {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			cal.setTime(sdf.parse(S));
			return cal;

		} catch (ParseException e) {

			throw new ParseException(S, 0);
		}

	}

	public long getPostId(String S) {
		return Long.parseLong(S);
	}

	public long getUserId(String S) {
		return Long.parseLong(S);
	}

	public String getUserName(String S) {
		return S;
	}

	public String getContent(String S) {
		return S;
	}

	public Producer() {
	}

}
