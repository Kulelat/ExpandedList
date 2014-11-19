package com.example.expandlistsample.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.expandlistsample.to.Author.AuthorColumns;
import com.example.expandlistsample.to.Book.BookColumns;
import com.example.expandlistsample.utils.Constants;

/**
 * BookProvider extends Content providers, Where ContentProviders are one of the
 * primary building blocks of Android applications, providing content to
 * applications. They encapsulate data and provide it to applications through
 * the single {@link ContentResolver} interface.
 * 
 * <p>
 * The primary methods that need to be implemented are:
 * <ul>
 * <li>{@link #query} which returns data to the caller</li>
 * <li>{@link #insert} which inserts new data into the content provider</li>
 * <li>{@link #update} which updates existing data in the content provider</li>
 * <li>{@link #delete} which deletes data from the content provider</li>
 * <li>{@link #getType} which returns the MIME type of data in the content
 * provider</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This class do all CRUD operations for the application.
 * </p>
 */
public class BookProvider extends ContentProvider {

	private static final int AUTHOR = 1;
	private static final int AUTHOR_ID = 2;
	private static final int BOOK = 3;
	private static final int BOOK_ID = 4;

	public static final Uri AUTHOR_URI = Uri.parse("content://"
			+ Constants.AUTHORITY + "/" + Constants.AUTHOR_TABLE_NAME);

	public static final Uri BOOK_URI = Uri.parse("content://"
			+ Constants.AUTHORITY + "/" + Constants.BOOK_TABLE_NAME);

	/**
	 * The MIME type of a directory of events
	 */
	private static final String AUTHOR_CONTENT_TYPE = "vnd.android.cursor.dir/author";

	/**
	 * The MIME type of a single event
	 */
	private static final String AUTHOR_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/author";

	/**
	 * The MIME type of a directory of events
	 */
	private static final String BOOK_CONTENT_TYPE = "vnd.android.cursor.dir/book";

	/**
	 * The MIME type of a single event
	 */
	private static final String BOOK_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/book";

	private UriMatcher uriMatcher;
	private BookDBHelper dbHelper;

	@Override
	public boolean onCreate() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		uriMatcher.addURI(Constants.AUTHORITY, "author", AUTHOR);
		uriMatcher.addURI(Constants.AUTHORITY, "author/#", AUTHOR_ID);
		uriMatcher.addURI(Constants.AUTHORITY, "book", BOOK);
		uriMatcher.addURI(Constants.AUTHORITY, "book/#", BOOK_ID);

		dbHelper = new BookDBHelper(getContext());
		return true;
	}

	/**
	 * Retrieve data from provider. Use the arguments to select the table to
	 * query, the rows and columns to return, and the sort order of the result.
	 * Return the data as a Cursor object.
	 * 
	 * @param uri
	 *            , The URI to query.
	 * @param projection
	 *            The list of columns to put into the cursor. If null all
	 *            columns are included.
	 * @param selection
	 *            A selection criteria to apply when filtering rows. If null
	 *            then all rows are included.
	 * @param selectionArgs
	 *            we may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @param sortOrder
	 *            How the rows in the cursor should be sorted. If null then the
	 *            provider is free to define the sort order.
	 * 
	 * @return a Cursor or null.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String orderBy) {

		dbHelper.getWritableDatabase();
		String database;

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
		switch (uriMatcher.match(uri)) {
		case AUTHOR:
			database = Constants.AUTHOR_TABLE_NAME;
			break;
		case BOOK:
			database = Constants.BOOK_TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Get the database and run the query
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		if (selection != null) {
			selection = selection + "=?";
		}

		Cursor cursor = db.query(database, projection, selection,
				selectionArgs, null, null, orderBy);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	/**
	 * Return the MIME type of the data at the given URI. This should start with
	 * <code>vnd.android.cursor.item</code> for a single record, or
	 * <code>vnd.android.cursor.dir/</code> for multiple items. This method can
	 * be called from multiple threads, as described in Application
	 * Fundamentals: Processes and Threads</a>.
	 * 
	 * @param uri
	 *            , the URI to query.
	 * @return a MIME type string, or null if there is no type.
	 */
	@Override
	public String getType(Uri uri) {

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
		switch (uriMatcher.match(uri)) {
		case AUTHOR:
			return AUTHOR_CONTENT_TYPE;
		case AUTHOR_ID:
			return AUTHOR_CONTENT_ITEM_TYPE;
		case BOOK:
			return BOOK_CONTENT_TYPE;
		case BOOK_ID:
			return BOOK_CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/**
	 * Implement this to insert a new row. Once the row is updated it notify the
	 * URI which is observing the object.
	 * 
	 * @param uri
	 *            The content:// URI of the insertion request.
	 * @param values
	 *            A set of column_name/value pairs to add to the database.
	 * @return The URI for the newly inserted item.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Uri newUri;
		long id;

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
		switch (uriMatcher.match(uri)) {
		case AUTHOR:
			// Insert into database
			id = db.insertOrThrow(Constants.AUTHOR_TABLE_NAME, null, values);

			// Notify any watchers of the change
			newUri = ContentUris.withAppendedId(AUTHOR_URI, id);
			break;
		case BOOK:
			// Insert into database
			id = db.insertOrThrow(Constants.BOOK_TABLE_NAME, null, values);

			// Notify any watchers of the change
			newUri = ContentUris.withAppendedId(BOOK_URI, id);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(newUri, null);
		return newUri;
	}

	/**
	 * A request to delete one or more rows. The selection clause is applied
	 * when performing the deletion, allowing the operation to affect multiple
	 * rows in a directory. Once the row is deleted, it notify the URI which is
	 * observing the object.
	 * 
	 * @param uri
	 *            The full URI to query, including a row ID (if a specific
	 *            record is requested).
	 * @param selection
	 *            An optional restriction to apply to rows when deleting.
	 * @return The number of rows affected.
	 * @throws SQLException
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int affected;

		if (selection != null) {
			selection = selection + "=?";
		}

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
		switch (uriMatcher.match(uri)) {
		case AUTHOR:
			affected = db.delete(Constants.AUTHOR_TABLE_NAME, selection,
					selectionArgs);
			break;
		case BOOK:
			affected = db.delete(Constants.BOOK_TABLE_NAME, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return affected;
	}

	/**
	 * Update a content URI. All rows matching the optionally provided selection
	 * will have their columns listed as the keys in the values map with the
	 * values of those keys. Once the row is updated, notifyChange change update
	 * to the observer.
	 * 
	 * @param uri
	 *            The URI to query. This can potentially have a record ID if
	 *            this is an update request for a specific record.
	 * @param values
	 *            A Bundle mapping from column names to new column values (NULL
	 *            is a valid value).
	 * @param selection
	 *            An optional filter to match rows to update.
	 * @return the number of rows affected.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		int affected;

		/*
		 * Choose the table to query and a sort order based on the code returned
		 * for the incoming URI.
		 */
		switch (uriMatcher.match(uri)) {
		case AUTHOR:
			affected = db.update(Constants.AUTHOR_TABLE_NAME, values,
					AuthorColumns._ID + "=?", selectionArgs);
			break;
		case BOOK:
			affected = db.update(Constants.BOOK_TABLE_NAME, values,
					BookColumns._ID + "=?", selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return affected;
	}
}
