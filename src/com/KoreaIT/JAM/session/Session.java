package com.KoreaIT.JAM.session;

public class Session {
	public static int loginedMemberId;

	static {
		loginedMemberId = -1;
	}

	public static void login(int id) {
		loginedMemberId = id;
	}

	public static void logout() {
		loginedMemberId = -1;

	}

	public static boolean islogined() {

		return loginedMemberId != -1;

	}

}
