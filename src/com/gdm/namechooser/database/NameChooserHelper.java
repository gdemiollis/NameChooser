package com.gdm.namechooser.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.gdm.framework.database.DatabaseHelper;

public class NameChooserHelper extends DatabaseHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "nameChooser.db";
	
	private static NameChooserHelper instance;

	protected NameChooserHelper(Context context, String name, int version) {
		super(context, name, version);
	}

	public static NameChooserHelper getInstance(Context context) {
		if (instance == null) {
			instance = new NameChooserHelper(context, DATABASE_NAME, DATABASE_VERSION);
		}
		return instance;
	}

	@Override
	protected List<String> getCreateRequests() {
		List<String> requests = new ArrayList<String>();
		requests.add(PrenomDbAdapter.getCreateRequest());
		return requests;
	}

	@Override
	protected List<String> getTableNames() {
		List<String> tableNames = new ArrayList<String>();
		tableNames.add(PrenomDbAdapter.TABLE_NAME);
		return tableNames;
	}
}
