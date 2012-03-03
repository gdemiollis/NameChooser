package com.gdm.namechooser.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.util.Log;

import com.gdm.framework.MyService;
import com.gdm.namechooser.R;
import com.gdm.namechooser.database.PrenomDbAdapter;
import com.gdm.namechooser.model.Prenom;
import com.gdm.namechooser.model.Prenom.Genre;

public class PrenomService extends MyService {

	private PrenomDbAdapter podcastDbAdapter;

	public static final String ACTION_COMPLETE = "PRENOM_LOADED";

	public static final String ACTION_RUNNING = "PRENOM_RUNNING";

	public static final String ACTION_ERROR = "PRENOM_ERROR";

	public PrenomService() {
		super(PrenomService.class.getSimpleName());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		podcastDbAdapter = PrenomDbAdapter.getInstance(this);
	}

	private List<Prenom> convertStreamToString(InputStream is, Genre genre) {
		List<Prenom> prenoms = new ArrayList<Prenom>();
		if (is != null) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					String[] ligne = strLine.split(";");
					Prenom prenom = new Prenom(ligne[1], Integer.parseInt(ligne[2]), 2010, genre, false);
					prenoms.add(prenom);
				}
				is.close();
			} catch (Exception e) {
				Log.e(getClass().getSimpleName(), e.getMessage(), e);
			}
		}
		return prenoms;
	}

	@Override
	protected String getErrorAction() {
		return ACTION_ERROR;
	}

	@Override
	protected void execute(Intent intent) {
		long startOperation = System.currentTimeMillis();
		List<Prenom> prenoms = new ArrayList<Prenom>();
		InputStream is = getResources().openRawResource(R.raw.filles2010);
		prenoms.addAll(convertStreamToString(is, Genre.FILLE));
		podcastDbAdapter.replace(prenoms);

		Log.i(getClass().getSimpleName(), "Liste des filles chargée");

		is = getResources().openRawResource(R.raw.gars2010);
		prenoms.clear();
		prenoms.addAll(convertStreamToString(is, Genre.GARS));
		podcastDbAdapter.save(prenoms);

		Log.i(getClass().getSimpleName(), "Liste des gars chargée");

		long stopOperation = System.currentTimeMillis();
		Log.i(getClass().getSimpleName(), "Durée de l'update : " + (stopOperation - startOperation) + " ms");
	}

	@Override
	protected String getStartedAction() {
		return ACTION_RUNNING;
	}

	@Override
	protected String getCompleteAction() {
		return ACTION_COMPLETE;
	}

}
