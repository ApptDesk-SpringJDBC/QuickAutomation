package com.quicktest.utils;
/**
 * 
 * @author Balaji
 *
 */

public class Wait {
	private static int SHORTWAIT;
	private static int NORMALWAIT;
	private static int LONGWAIT;
	private static int VERYLONGWAIT;

	public static void setWaits() throws Exception {
		SHORTWAIT = TestUtility.selectTimeCountToWait("SHORTWAIT");
		NORMALWAIT = TestUtility.selectTimeCountToWait("NORMALWAIT");
		LONGWAIT = TestUtility.selectTimeCountToWait("LONGWAIT");
		VERYLONGWAIT = TestUtility.selectTimeCountToWait("VERYLONGWAIT");
	}

	public static int getsShortWait() {
		return SHORTWAIT;
	}

	public static int getsNormalWait() {
		return NORMALWAIT;
	}

	public static int getsLongWait() {
		return LONGWAIT;
	}

	public static int getsVeryLongWait() {
		return VERYLONGWAIT;
	}
}
