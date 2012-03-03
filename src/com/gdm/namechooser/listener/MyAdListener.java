package com.gdm.namechooser.listener;

import android.util.Log;
import android.view.View;

import com.gdm.namechooser.R;
import com.gdm.namechooser.activity.NameListActivity;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;

public class MyAdListener implements AdListener {

	private final NameListActivity activity;

	public MyAdListener(NameListActivity activity) {
		this.activity = activity;
	}

	public void onReceiveAd(Ad ad) {
		Log.i(getClass().getSimpleName(), "Add received " + ad);
		activity.findViewById(R.id.ad).setVisibility(View.VISIBLE);
	}

	public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
		Log.e(getClass().getSimpleName(), "Error with Ad " + ad);
		activity.findViewById(R.id.ad).setVisibility(View.GONE);
	}

	public void onPresentScreen(Ad ad) {
		
	}

	public void onDismissScreen(Ad ad) {
		
	}

	public void onLeaveApplication(Ad ad) {
		
	}
}