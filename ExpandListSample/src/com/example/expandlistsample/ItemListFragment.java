package com.example.expandlistsample;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;

import com.example.expandlistsample.adapter.AuthorListAdapter;
import com.example.expandlistsample.db.BookProvider;
import com.example.expandlistsample.service.LoadAuthorList;
import com.example.expandlistsample.to.Author.AuthorColumns;
import com.example.expandlistsample.to.Book.BookColumns;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private static final String TAG = ItemListFragment.class.getSimpleName();

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private ExpandableListView elv;

	/**
	 * Expandable list adapter instance.
	 */
	private AuthorListAdapter adapter;

	/**
	 * The columns we are interested in from the database
	 */
	protected static final String[] AUTHOR_PROJECTION = new String[] {
			AuthorColumns._ID, AuthorColumns.AUTHOR_ID,
			AuthorColumns.AUTHOR_NAME };

	/**
	 * The columns we are interested in from the database
	 */
	protected static final String[] BOOK_PROJECTION = new String[] {
			BookColumns._ID, // 0
			BookColumns.BOOK_NAME, // 1
			BookColumns.AUTHOR_ID, // 2
	};

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(int childPosition, String authorID,
				String authorName, String bookID, String bookName);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onItemSelected(int childPosition, String authorID,
				String authorName, String bookID, String bookName) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemListFragment() {

		Log.d(TAG, "ItemListFragment constructor");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

		Intent intent = new Intent(getActivity(), LoadAuthorList.class);
		getActivity().startService(intent);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.author_book_list, null);
		elv = (ExpandableListView) v.findViewById(R.id.authorbook_list);
		elv.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new AuthorListAdapter(null, getActivity());

		elv.setAdapter(adapter);
		elv.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				// Log.d(TAG, "childpo " + childPosition + " , id " + id
				// + " group ops " + groupPosition);
				Cursor authorCursor = (Cursor) adapter.getGroup(groupPosition);

				Cursor itemCursor = adapter.getChild(groupPosition,
						childPosition);

				// Notify the active callbacks interface (the activity, if the
				// fragment is attached to one) that an item has been selected.
				mCallbacks.onItemSelected(childPosition, authorCursor
						.getString(authorCursor
								.getColumnIndex(AuthorColumns._ID)),
						authorCursor.getString(authorCursor
								.getColumnIndex(AuthorColumns.AUTHOR_NAME)),
						itemCursor.getString(itemCursor
								.getColumnIndex(BookColumns._ID)), itemCursor
								.getString(itemCursor
										.getColumnIndex(BookColumns.BOOK_NAME)));

				int index = parent.getFlatListPosition(ExpandableListView
						.getPackedPositionForChild(groupPosition, childPosition));
				parent.setItemChecked(index, true);
				
				return false;
			}
		});

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		Loader<Cursor> loader = getActivity().getLoaderManager().getLoader(-1);
		if (loader != null && !loader.isReset()) {
			getActivity().getLoaderManager().restartLoader(-1, null, this);
		} else {
			getActivity().getLoaderManager().initLoader(-1, null, this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			elv.setItemChecked(mActivatedPosition, false);
		} else {
			elv.setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.

		// group cursor
		CursorLoader cl = new CursorLoader(getActivity(),
				BookProvider.AUTHOR_URI, AUTHOR_PROJECTION, null, null,
				AuthorColumns.DEFAULT_SORT_ORDER);

		return cl;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		// Swap the new cursor in.
		int id = loader.getId();

		if (id == -1) {

			adapter.setGroupCursor(cursor);

			expandAllChild();
		}

	}

	private void expandAllChild() {

		for (int i = 0; i < adapter.getGroupCount(); i++) {

			elv.expandGroup(i);
		}

	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// is about to be closed.
		int id = loader.getId();
		if (id != -1) {
			// child cursor
			try {
				adapter.setChildrenCursor(id, null);
			} catch (NullPointerException e) {
				Log.w("TAG", "Adapter expired, try again on the next query: "
						+ e.getMessage());
			}
		} else {
			adapter.setGroupCursor(null);
		}
	}
}
