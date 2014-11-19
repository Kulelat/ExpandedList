package com.example.expandlistsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.expandlistsample.utils.Constants;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private String authorName;

	private String bookName;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);

		bookName = getArguments().getString(Constants.BOOK_NAME);
		((TextView) rootView.findViewById(R.id.book_tv)).setText(bookName);

		authorName = getArguments().getString(Constants.AUTHOR_NAME);

		((TextView) rootView.findViewById(R.id.author_tv)).setText(authorName);

		if (authorName.equalsIgnoreCase("Jiddu Krishnamurti")) {
			((ImageView) rootView.findViewById(R.id.author_pic))
					.setImageDrawable(getResources().getDrawable(R.drawable.j));
		} else if (authorName.equalsIgnoreCase("Leo Tolstoy")) {
			((ImageView) rootView.findViewById(R.id.author_pic))
					.setImageDrawable(getResources().getDrawable(R.drawable.l));
		} else {
			((ImageView) rootView.findViewById(R.id.author_pic))
					.setImageDrawable(getResources().getDrawable(R.drawable.w));
		}

		return rootView;
	}
}
