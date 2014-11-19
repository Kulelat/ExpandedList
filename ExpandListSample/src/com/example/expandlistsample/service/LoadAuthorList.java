package com.example.expandlistsample.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.expandlistsample.network.FetchList;

public class LoadAuthorList extends IntentService {

	FetchList fetchList = new FetchList();

	public LoadAuthorList() {
		super("LoadAuthorList");
	}

	/**
	 * This method is invokes a request to pool the camera list for every 5 sec.
	 */
	@Override
	protected void onHandleIntent(Intent requestIntent) {

		fetchList.loadAuthorInfoData(getApplicationContext()
				.getContentResolver());
	}
}