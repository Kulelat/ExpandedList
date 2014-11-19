package com.example.expandlistsample.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.example.expandlistsample.db.BookProvider;
import com.example.expandlistsample.to.Author.AuthorColumns;
import com.example.expandlistsample.to.AuthorInfo;
import com.example.expandlistsample.to.Book.BookColumns;

public class FetchList {

	public void loadAuthorInfoData(ContentResolver contentResolver) {

		contentResolver.delete(BookProvider.AUTHOR_URI, null, null);
		contentResolver.delete(BookProvider.BOOK_URI, null, null);

		ContentValues values;
		int j = 0;

		for (String authorInfo : getAuthorList()) {
			values = new ContentValues();

			values.put(AuthorColumns._ID, authorInfo);
			values.put(AuthorColumns.AUTHOR_ID, authorInfo);
			values.put(AuthorColumns.AUTHOR_NAME, authorInfo);

			contentResolver.insert(BookProvider.AUTHOR_URI, values);
		}

		for (AuthorInfo authorInfo : getUserList()) {
			values = new ContentValues();
			values.put(BookColumns._ID, j);
			values.put(BookColumns.BOOK_ID, j++);
			values.put(BookColumns.BOOK_NAME, authorInfo.getTitle());
			values.put(BookColumns.AUTHOR_ID, authorInfo.getAuthor());

			contentResolver.insert(BookProvider.BOOK_URI, values);
		}

	}

	public static List<String> getAuthorList() {

		List<String> authorsName = new ArrayList<String>();
		authorsName.add("Leo Tolstoy");
		authorsName.add("William Shakespeare");
		authorsName.add("Jiddu Krishnamurti");
		return authorsName;
	}

	public static Vector<AuthorInfo> getUserList() {
		Vector<AuthorInfo> authorInfoList = new Vector<AuthorInfo>();

		AuthorInfo authorInfo = new AuthorInfo("Freedom from the Known",
				"Jiddu Krishnamurti");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("On Fear", "Jiddu Krishnamurti");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("The awakening of intelligence",
				"Jiddu Krishnamurti");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("The impossible question",
				"Jiddu Krishnamurti");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("Total freedom", "Jiddu Krishnamurti ");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("Kholstomer", "Leo Tolstoy");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("What Men Live By", "Leo Tolstoy");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("War and Peace", "Leo Tolstoy");
		authorInfoList.add(authorInfo);
		authorInfo = new AuthorInfo("Romeo and Juliet", "William Shakespeare");
		authorInfoList.add(authorInfo);

		Collections.sort(authorInfoList);
		return authorInfoList;
	}
}
