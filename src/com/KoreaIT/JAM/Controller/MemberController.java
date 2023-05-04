package com.KoreaIT.JAM.Controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.JAM.Service.MemberService;

public class MemberController {

	private Scanner sc;
	private MemberService memberService;

	public MemberController(Connection conn, Scanner sc) {
		this.sc = sc;
		this.memberService = new MemberService(conn);
	}

	public void doJoin() {

		String LoginId = null;
		String LoginPw = null;
		String name = null;
		String LoginPw2 = null;

		while (true) {
			System.out.printf("LoginId : ");
			LoginId = sc.nextLine().trim();

			if (LoginId.length() == 0) {
				System.out.println("아이디를 입력해주세요");
				continue;
			}

			boolean isLoginIdDup = memberService.isLoginIdDup(LoginId);

			if (isLoginIdDup) {
				System.out.printf("%s(은)는 이미 사용중인 아이디입니다. \n", LoginId);
				continue;
			}

			System.out.printf("%s(은)는 이미 사용가능한 아이디입니다. \n", LoginId);
			break;
		}

		while (true) {
			System.out.printf("LoginPw : ");
			LoginPw = sc.nextLine().trim();

			if (LoginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}

			boolean loginPwCheck = true;

			while (true) {
				System.out.printf("LoginPw2 : ");
				LoginPw2 = sc.nextLine().trim();

				if (LoginPw2.length() == 0) {
					System.out.println("확인 비밀번호를 입력해주세요.");
					continue;
				}
				if (LoginPw.equals(LoginPw2) == false) {
					System.out.println("비밀번호를 확인해주시기 바랍니다.");
					loginPwCheck = false;
				}
				break;
			}
			if (loginPwCheck) {
				break;
			}

		}

		while (true) {
			System.out.printf("name : ");
			name = sc.nextLine().trim();

			if (name.length() == 0) {
				continue;
			}
			break;
		}

		memberService.doJoin(LoginId, LoginPw, name);
		System.out.printf("%s님 환영합니다.\n", name);

	}
}
