package it.bologna.devastapp.util;

import it.bologna.devastapp.presentation.pmo.PosizionePmo;

public final class DistanzaUtil {

	public static int distanzaCoordinate(PosizionePmo posizione1,
			PosizionePmo posizione2) {

		double d2r = Math.PI / 180;
		int distanza = 0;

		double dlong = (posizione1.getLongitudine() - posizione2
				.getLongitudine()) * d2r;
		double dlat = (posizione1.getLatitudine() - posizione2.getLatitudine())
				* d2r;
		double a = Math.pow(Math.sin(dlat / 2.0), 2)
				+ Math.cos(posizione1.getLatitudine() * d2r)
				* Math.cos(posizione2.getLatitudine() * d2r)
				* Math.pow(Math.sin(dlong / 2.0), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = 6367 * c * 1000;

		distanza = (int) d;

		return distanza;

	}

}
