package com.example.expandlistsample.db;

import com.example.expandlistsample.to.Author.AuthorColumns;
import com.example.expandlistsample.to.Book.BookColumns;
import com.example.expandlistsample.utils.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookDBHelper extends SQLiteOpenHelper {

	public final String TAG = BookDBHelper.class.getSimpleName();

	public static final String DATABASE_NAME = "books.db";
	public static final int DATABASE_VERSION = 1;

	/**
	 * public constructor, Which creates a helper object to create, open, and/or
	 * manage a database.
	 * 
	 * @param context
	 *            to use to open or create the database
	 */
	public BookDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Called when the database is created for the first time. This is where the
	 * creation of tables and the initial population of the tables should
	 * happen.
	 * 
	 * @param db
	 *            The database.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		StringBuffer authorQuery = new StringBuffer("CREATE TABLE ");
		authorQuery.append(Constants.AUTHOR_TABLE_NAME + " (");
		authorQuery.append(AuthorColumns._ID + " TEXT PRIMARY KEY,");
		authorQuery.append(AuthorColumns.AUTHOR_ID + " TEXT,");
		authorQuery.append(AuthorColumns.AUTHOR_NAME + " TEXT");
		authorQuery.append(");");

		StringBuffer bookQuery = new StringBuffer("CREATE TABLE ");
		bookQuery.append(Constants.BOOK_TABLE_NAME + "(");
		bookQuery.append(BookColumns._ID + " TEXT PRIMARY KEY, ");
		bookQuery.append(BookColumns.BOOK_ID + " VARCHAR UNIQUE ,");
		bookQuery.append(BookColumns.BOOK_NAME + " VARCHAR, ");
		bookQuery.append(BookColumns.AUTHOR_ID + " VARCHAR,");
		bookQuery.append("FOREIGN KEY(" + BookColumns.AUTHOR_ID
				+ ") REFERENCES " + Constants.AUTHOR_TABLE_NAME + "("
				+ BookColumns._ID + "))");

		db.execSQL(authorQuery.toString());

		db.execSQL(bookQuery.toString());

	}

	/**
	 * Called when the database has to be upgraded. Where we can drop, alter
	 * tables or do anything else it needs to upgrade to the new schema version.
	 * 
	 * @param db
	 *            The database.
	 * @param oldVersion
	 *            The old database version.
	 * @param newVersion
	 *            The new database version.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + Constants.AUTHOR_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.BOOK_TABLE_NAME);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Downgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + Constants.AUTHOR_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.BOOK_TABLE_NAME);
		onCreate(db);
	}
}
