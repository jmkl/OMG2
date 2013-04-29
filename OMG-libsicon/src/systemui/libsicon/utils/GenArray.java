package systemui.libsicon.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenArray {
	
	public static int[] convertInt(List<Integer> integ) {
		int[] ret = new int[integ.size()];
		Iterator<Integer> iterator = integ.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().intValue();
		}
		return ret;

	}
	public static Object[] converTogels(ArrayList<Object> integ) {
		Object[] ret = new Object[integ.size()];
		Iterator<Object> iterator = integ.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next();
		}
		return ret;

	}
}
