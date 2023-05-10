package com.KoreaIT.JAM.Controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.JAM.Member;
import com.KoreaIT.JAM.Service.MemberService;
import com.KoreaIT.JAM.session.Session;

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

			if (Session.islogined() == true) {
				System.out.println("이미 로그인 된 상태입니다.");
				return;
			}

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

			System.out.printf("%s(은)는 사용가능한 아이디입니다. \n", LoginId);
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

	public void dologin() {

		if (Session.islogined() == true) {
			System.out.println("이미 로그인 된 상태입니다.");
			return;
		}

		System.out.printf("=== Login ===\n");
		Member member = null;
		while (true) {
			System.out.printf("LoginId : ");
			String LoginId = sc.nextLine().trim();

			if (LoginId.length() == 0) {
				System.out.println("아이디를 입력해주세요");
				continue;
			}

			System.out.printf("LoginPw : ");
			String LoginPw = sc.nextLine().trim();

			if (LoginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}

			member = memberService.getMember(LoginId);

			if (member == null) {
				System.out.println("존재 하지 않는 아이디입니다.");
				return;
			}

			if (member.loginPw.equals(LoginPw) == false) {
				System.out.println("비밀번호를 확인해 주시기 바랍니다.");
				return;
			}
			break;
		}

		Session.login(member.id);

		System.out.printf("%s 님 환영합니다.\n", member.loginId);
	}

	public void dologout() {

		if (Session.islogined() == false) {
			System.out.println("로그인 중이 아닙니다.");
			return;
		}

		Session.logout();
		System.out.println("로그아웃 되었습니다.");
	}
}
