package com.example.expandlistsample.to;

import com.example.expandlistsample.utils.Constants;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Author {

	// This class cannot be instantiated
	private Author() {
	}

	/**
	 * Notes table
	 */
	public static final class AuthorColumns implements BaseColumns {

		private AuthorColumns() {
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ Constants.AUTHORITY + Constants.AUTHORS);

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ Constants.AUTHORS;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ Constants.AUTHORS;

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
		public static final String AUTHOR_NAME = "name";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = AUTHOR_NAME
				+ " COLLATE NOCASE ASC";
	}

}
