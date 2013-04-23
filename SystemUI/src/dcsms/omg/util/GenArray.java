package dcsms.omg.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dcsms.omg.notification.TogglesModel;

public class GenArray {
	
	public static int[] convertInt(List<Integer> integ) {
		int[] ret = new int[integ.size()];
		Iterator<Integer> iterator = integ.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().intValue();
		}
		return ret;

	}
	public static TogglesModel[] converTogels(ArrayList<TogglesModel> integ) {
		TogglesModel[] ret = new TogglesModel[integ.size()];
		Iterator<TogglesModel> iterator = integ.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next();
		}
		return ret;

	}
}
