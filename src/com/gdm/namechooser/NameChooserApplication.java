package com.gdm.namechooser;

import android.app.Application;

import com.gdm.namechooser.database.NameChooserHelper;

public class NameChooserApplication extends Application {

	@Override
	public void onTerminate() {
		super.onTerminate();
		NameChooserHelper.getInstance(this).close();
	}
}
