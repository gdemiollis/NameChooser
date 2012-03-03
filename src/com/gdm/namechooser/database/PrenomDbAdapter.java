package com.gdm.namechooser.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gdm.framework.database.DatabaseHelper;
import com.gdm.framework.database.DbAdapter;
import com.gdm.namechooser.model.Prenom;
import com.gdm.namechooser.model.Prenom.Genre;

public class PrenomDbAdapter extends DbAdapter<Prenom> {

	public static final String TABLE_NAME = Prenom.class.getSimpleName();
	public static final String KEY_PRENOM = "prenom";
	public static final String KEY_FREQUENCE = "frequence";
	public static final String KEY_ANNEE = "annee";
	public static final String KEY_GENRE = "genre";
	public static final String KEY_FAVORIS = "favoris";

	private static PrenomDbAdapter instance;

	private PrenomDbAdapter(Context context) {
		super(context);
	}

	public static PrenomDbAdapter getInstance(Context context) {
		if (instance == null) {
			instance = new PrenomDbAdapter(context);
		}
		return instance;
	}

	@Override
	protected ContentValues getValues(Prenom prenom) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PRENOM, prenom.getPrenom());
		initialValues.put(KEY_FREQUENCE, prenom.getFrequence());
		initialValues.put(KEY_ANNEE, prenom.getAnnee());
		initialValues.put(KEY_GENRE, prenom.getGenre().toString());
		initialValues.put(KEY_FAVORIS, prenom.isFavoris());
		return initialValues;
	}

	private static Map<String, String> getAttributs() {
		Map<String, String> attributs = new HashMap<String, String>();
		attributs.put(KEY_PRENOM, "text not null");
		attributs.put(KEY_GENRE, "text not null");
		attributs.put(KEY_ANNEE, "int");
		attributs.put(KEY_FREQUENCE, "int");
		attributs.put(KEY_FAVORIS, "int");
		return attributs;
	}

	@Override
	public String[] getDbColumns() {
		return new String[] { KEY_ROWID, KEY_PRENOM, KEY_FREQUENCE, KEY_ANNEE, KEY_GENRE, KEY_FAVORIS };
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public Prenom convertToObject(Cursor mCursor) {
		Prenom prenom = new Prenom();
		prenom.setId(mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_ROWID)));
		prenom.setPrenom(mCursor.getString(mCursor.getColumnIndex(KEY_PRENOM)));
		prenom.setFrequence(mCursor.getInt(mCursor.getColumnIndex(KEY_FREQUENCE)));
		prenom.setAnnee(mCursor.getInt(mCursor.getColumnIndex(KEY_ANNEE)));
		prenom.setGenre(Genre.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_GENRE))));
		prenom.setFavoris(mCursor.getInt(mCursor.getColumnIndex(KEY_FAVORIS)) == 1);
		return prenom;
	}

	@Override
	protected DatabaseHelper getDbHelper() {
		return NameChooserHelper.getInstance(context);
	}

	public static String getCreateRequest() {
		String createTable = "create table " + TABLE_NAME + " (" + KEY_ROWID + " integer primary key autoincrement";
		for (Entry<String, String> entry : getAttributs().entrySet()) {
			createTable += ", " + entry.getKey() + " " + entry.getValue();
		}
		createTable += ")";
		return createTable;
	}

	public Cursor listPrenomOrderByFrequenceDecroissante() {
		Cursor cursor = new RequestBuilder().orderBy(KEY_FREQUENCE + " " + DbAdapter.DESC).request();
		return cursor;
	}

	public Cursor listGarsOrderByFrequenceDecroissante() {
		Cursor cursor = new RequestBuilder().where(KEY_GENRE + "=" + "\"" + Genre.GARS + "\"").orderBy(KEY_FREQUENCE + " " + DbAdapter.DESC).request();
		return cursor;
	}

	public Cursor listFillesOrderByFrequenceDecroissante() {
		Cursor cursor = new RequestBuilder().where(KEY_GENRE + "=" + "\"" + Genre.FILLE + "\"").orderBy(KEY_FREQUENCE + " " + DbAdapter.DESC).request();
		return cursor;
	}

	public Cursor listFavorisOrderByFrequenceDecroissante() {
		Cursor cursor = new RequestBuilder().where(KEY_FAVORIS + "= 1").orderBy(KEY_FREQUENCE + " " + DbAdapter.DESC).request();
		return cursor;
	}

	public int removeFavorite(Prenom prenom) {
		prenom.setFavoris(false);
		return mDb.update(getTableName(), getValues(prenom), KEY_ROWID + "=" + prenom.getId(), null);
	}

	public int addFavorite(Prenom prenom) {
		prenom.setFavoris(true);
		return mDb.update(getTableName(), getValues(prenom), KEY_ROWID + "=" + prenom.getId(), null);
	}
}
