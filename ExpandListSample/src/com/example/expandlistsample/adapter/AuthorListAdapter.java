package com.example.expandlistsample.adapter;

import java.util.HashMap;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.example.expandlistsample.ItemListActivity;
import com.example.expandlistsample.R;
import com.example.expandlistsample.db.BookProvider;
import com.example.expandlistsample.to.Author.AuthorColumns;
import com.example.expandlistsample.to.Book.BookColumns;

public class AuthorListAdapter extends CursorTreeAdapter {

	public HashMap<String, View> childView = new HashMap<String, View>();

	/**
	 * The columns we are interested in from the database
	 */
	protected static final String[] BOOK_PROJECTION = new String[] {
			BookColumns._ID, // 0
			BookColumns.BOOK_NAME, // 1
			BookColumns.AUTHOR_ID, // 2
	};

	private static final String TAG = AuthorListAdapter.class.getSimpleName();

	private ItemListActivity mActivity;
	private LayoutInflater mInflater;

	public AuthorListAdapter(Cursor cursor, Context context) {

		super(cursor, context);
		mActivity = (ItemListActivity) context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View newGroupView(Context context, Cursor cursor,
			boolean isExpanded, ViewGroup parent) {

		final View view = mInflater
				.inflate(R.layout.author_list, parent, false);
		return view;
	}

	@Override
	public void bindGroupView(View view, Context context, Cursor cursor,
			boolean isExpanded) {

		TextView authorName = (TextView) view.findViewById(R.id.author_name);

		authorName.setText(cursor.getString(cursor
				.getColumnIndex(AuthorColumns.AUTHOR_NAME)));
	}

	@Override
	public View newChildView(Context context, Cursor cursor,
			boolean isLastChild, ViewGroup parent) {

		final View view = mInflater.inflate(R.layout.books_list, parent, false);

		return view;
	}

	@Override
	public void bindChildView(View view, Context context, Cursor cursor,
			boolean isLastChild) {

		TextView bookName = (TextView) view.findViewById(R.id.book_name);

		bookName.setText(cursor.getString(cursor
				.getColumnIndex(BookColumns.BOOK_NAME)));
	}

	protected Cursor getChildrenCursor(Cursor groupCursor) {

		Cursor itemCursor = getGroup(groupCursor.getPosition());

		Log.d(TAG, itemCursor.getString(itemCursor
				.getColumnIndex(AuthorColumns._ID)));

		CursorLoader cursorLoader = new CursorLoader(mActivity,
				BookProvider.BOOK_URI, BOOK_PROJECTION, BookColumns.AUTHOR_ID,
				new String[] { itemCursor.getString(itemCursor
						.getColumnIndex(AuthorColumns._ID)) },
				BookColumns.DEFAULT_SORT_ORDER);

		Cursor childCursor = null;

		try {
			childCursor = cursorLoader.loadInBackground();
			Log.d(TAG, "childCursor " + childCursor.getCount());
			childCursor.moveToFirst();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		return childCursor;
	}
}
