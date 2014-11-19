package com.example.expandlistsample.to;

import com.example.expandlistsample.utils.Constants;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Book {

	// This class cannot be instantiated
	private Book() {
	}

	/**
	 * Book table
	 */
	public static final class BookColumns implements BaseColumns {

		private BookColumns() {
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ Constants.AUTHORITY + Constants.BOOKS);

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ Constants.BOOKS;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ Constants.BOOKS;

		/**
		 * The BOOK_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BOOK_ID = "BookID";

		/**
		 * The AUTHOR_ID
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String AUTHOR_ID = "AuthorID";

		/**
		 * The name
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BOOK_NAME = "name";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = BOOK_NAME
				+ " COLLATE NOCASE ASC";
	}

}
