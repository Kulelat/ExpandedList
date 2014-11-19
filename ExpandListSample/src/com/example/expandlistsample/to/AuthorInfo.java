package com.example.expandlistsample.to;

public class AuthorInfo implements Comparable<AuthorInfo> {
	private String title;

	private String author;

	public AuthorInfo() {
		// TODO Auto-generated constructor stub
	}

	public AuthorInfo(String title, String author) {
		super();
		this.title = title;
		this.author = author;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	@Override
	public int compareTo(AuthorInfo book) {
		return this.getTitle().compareTo(book.getTitle());
	}
}