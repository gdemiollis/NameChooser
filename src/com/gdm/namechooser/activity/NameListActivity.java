package com.gdm.namechooser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.gdm.framework.compatibility.activity.MyActivity;
import com.gdm.namechooser.R;
import com.gdm.namechooser.database.PrenomDbAdapter;
import com.gdm.namechooser.listener.MyAdListener;
import com.gdm.namechooser.model.Prenom;
import com.gdm.namechooser.service.PrenomService;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class NameListActivity extends MyActivity<Prenom> {

	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		adView = (AdView) findViewById(R.id.ad);

		AdRequest adRequest = new AdRequest();
		adRequest.addKeyword("Baby Goods");
		adView.loadAd(adRequest);
		adView.setAdListener(new MyAdListener(this));
		//getActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	protected String getExtraId() {
		return "";
	}

	@Override
	protected int getIdDetailFragment() {
		return 0;
	}

	@Override
	protected int getIdListFragment() {
		return R.id.fragmentList;
	}

	@Override
	protected PrenomDbAdapter getDbAdapter() {
		return PrenomDbAdapter.getInstance(this);
	}

	@Override
	protected int getIdLayout() {
		return R.layout.prenom_list;
	}

	@Override
	public void onElementSelected(int position) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.liste_prenom, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		NameListFragment nameListFragment = (NameListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentList);
		switch (item.getItemId()) {
		case R.id.filtreGars:
			nameListFragment.updateCursor(getDbAdapter()
					.listGarsOrderByFrequenceDecroissante());
			nameListFragment.getListView().setBackgroundResource(R.color.colGars);
			return true;
		case R.id.filtreFille:
			nameListFragment.updateCursor(getDbAdapter()
					.listFillesOrderByFrequenceDecroissante());
			nameListFragment.getListView().setBackgroundResource(R.color.colFille);
			return true;
		case R.id.filtreTous:
			nameListFragment.updateCursor(getDbAdapter()
					.listPrenomOrderByFrequenceDecroissante());
			nameListFragment.getListView().setBackgroundResource(R.color.colTous);
			return true;
		case R.id.filtreFavoris:
			nameListFragment.updateCursor(getDbAdapter()
					.listFavorisOrderByFrequenceDecroissante());
			nameListFragment.getListView().setBackgroundResource(R.color.colFavoris);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	@Override
	protected Class<PrenomService> getServiceClass() {
		return PrenomService.class;
	}

	@Override
	public String getActionComplete() {
		return PrenomService.ACTION_COMPLETE;
	}

	@Override
	protected String getActionRunning() {
		return PrenomService.ACTION_RUNNING;
	}

	@Override
	protected String getActionError() {
		return PrenomService.ACTION_ERROR;
	}

	@Override
	protected Intent getIntentDetailActivity(int position) {
		return null;
	}

}
