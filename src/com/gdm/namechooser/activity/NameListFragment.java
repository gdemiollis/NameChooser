package com.gdm.namechooser.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.gdm.framework.compatibility.fragment.CursorListFragment;
import com.gdm.namechooser.R;
import com.gdm.namechooser.database.PrenomDbAdapter;
import com.gdm.namechooser.model.Prenom;
import com.gdm.namechooser.model.Prenom.Genre;

public class NameListFragment extends CursorListFragment<Prenom> {

	@Override
	protected int getRowResource() {
		return R.layout.prenom_row;
	}

	@Override
	protected int getIdDetailFragment() {
		return 0;
	}

	@Override
	protected int[] getTo() {
		return new int[] { R.id.textPrenom, R.id.textFrequence, R.id.textRang, R.id.imageSymbole, R.id.imageFavoris };
	}

	@Override
	protected String[] getFrom() {
		return new String[] { PrenomDbAdapter.KEY_PRENOM, PrenomDbAdapter.KEY_FREQUENCE, PrenomDbAdapter.KEY_FREQUENCE, PrenomDbAdapter.KEY_GENRE,
				PrenomDbAdapter.KEY_FAVORIS };
	}

	@Override
	protected PrenomDbAdapter getDbAdapter() {
		return PrenomDbAdapter.getInstance(getActivity());
	}

	@Override
	protected ViewBinder getViewBinder() {
		return new ViewBinder() {

			@Override
			public boolean setViewValue(View view, final Cursor cursor, int columnIndex) {
				final int position = cursor.getPosition();
				switch (view.getId()) {
				case R.id.textRang:
					((TextView) view).setText("" + (position + 1));
					return true;
				case R.id.imageSymbole:
					Genre genre = Genre.valueOf(cursor.getString(columnIndex));
					switch (genre) {
					case FILLE:
						((ImageView) view).setImageResource(R.drawable.fille);
						return true;
					case GARS:
						((ImageView) view).setImageResource(R.drawable.gars);
						return true;
					default:
						break;
					}
					return true;
				case R.id.imageFavoris:
					ImageView imageFavoris = (ImageView) view;
					if (cursor.getInt(columnIndex) == 1) {
						imageFavoris.setImageResource(R.drawable.star);
					} else {
						imageFavoris.setImageResource(R.drawable.star_gris);
					}
					return true;
				default:
					return false;
				}
			}
		};
	}

	private enum StatutFavoris {
		SUCCES_AJOUT, ECHEC_AJOUT, SUCCES_SUPPR, ECHEC_SUPPR
	}
	
	@Override
	protected OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}
	
	private OnItemClickListener onItemClickListener= new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			cursor.moveToPosition(position);
			Prenom prenom = getDbAdapter().convertToObject(cursor);
			new FavoriteTask().execute(prenom);
		}
	};

	private class FavoriteTask extends AsyncTask<Prenom, Void, StatutFavoris> {

		private Prenom prenom;

		@Override
		protected StatutFavoris doInBackground(Prenom... params) {
			prenom = params[0];
			if (prenom.isFavoris()) {
				if (getDbAdapter().removeFavorite(prenom) == 0) {
					return StatutFavoris.ECHEC_SUPPR;
				} else {
					return StatutFavoris.SUCCES_SUPPR;
				}
			} else {
				if (getDbAdapter().addFavorite(prenom) == 0) {
					return StatutFavoris.ECHEC_AJOUT;
				} else {
					return StatutFavoris.SUCCES_AJOUT;
				}
			}
		}

		protected void onPostExecute(StatutFavoris result) {
			getCursor().requery();
			String message = "";
			switch (result) {
			case ECHEC_AJOUT:
				message = getString(R.string.erreurAjoutFavoris);
				break;
			case SUCCES_AJOUT:
				message = getString(R.string.ajoutFavoris, prenom.getPrenom());
				break;
			case ECHEC_SUPPR:
				message = getString(R.string.erreurSupprFavoris);
				break;
			case SUCCES_SUPPR:
				message = getString(R.string.supprFavoris, prenom.getPrenom());
				break;
			default:
				break;
			}
			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected int getListeLayout() {
		return R.layout.prenom_list_fragment;
	}

	@Override
	public Cursor getDataToFillList() {
		return getDbAdapter().listPrenomOrderByFrequenceDecroissante();
	}

	@Override
	protected boolean isActivityManagingCursor() {
		return false;
	}

}
