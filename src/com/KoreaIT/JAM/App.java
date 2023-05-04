package com.KoreaIT.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.JAM.util.DBUtil;
import com.KoreaIT.JAM.util.SecSql;

public class App {
	public void run() {
		System.out.println("== 프로그램 시작 ==");

		Scanner sc = new Scanner(System.in);

		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("exit")) {
					System.out.println("== 프로그램 끝 ==");
					break;
				}

				
				
				
				
				
				
				
				
				
				
				if (cmd.equals("member join")) {

					String LoginId = null;
					String LoginPw = null;
					String name = null;
					String LoginPw2 = null;
					SecSql sql = null;

					System.out.println("== 회원 가입 시작 ==");
					while (true) {
						System.out.printf("LoginId : ");
						LoginId = sc.nextLine().trim();

						if (LoginId.length() == 0) {
							System.out.println("아이디를 입력해주세요");
							continue;
						}

						sql = new SecSql();
						sql.append("SELECT COUNT(*) > 0");
						sql.append("FROM `member`");
						sql.append("WHERE loginId = ?", LoginId);

						boolean isLoginIdDup = DBUtil.selectRowBooleanValue(conn, sql);

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

						while (true) {
							System.out.printf("LoginPw2 : ");
							LoginPw2 = sc.nextLine().trim();

							if (LoginPw2.length() == 0) {
								System.out.println("확인 비밀번호를 입력해주세요.");
								continue;
							}
							if (LoginPw.equals(LoginPw2) == false) {
								System.out.println("비밀번호를 확인해주시기 바랍니다.");
								continue;
							}
							break;
						}
						break;

					}

					while (true) {
						System.out.printf("name : ");
						name = sc.nextLine().trim();

						if (name.length() == 0) {
							continue;
						}
						break;
					}

					sql = new SecSql();
					sql.append("INSERT INTO member");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", LoginId = ?", LoginId);
					sql.append(", LoginPw = ?", LoginPw);
					sql.append(", `name` = ?", name);

					DBUtil.insert(conn, sql);

					System.out.printf("%s님 환영합니다.\n", name);

				} else if (cmd.equals("article write")) {
					System.out.println("== 게시물 작성 ==");

					System.out.printf("제목 : ");
					String title = sc.nextLine();
					System.out.printf("내용 : ");
					String body = sc.nextLine();

					SecSql sql = new SecSql();
					sql.append("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);

					int id = DBUtil.insert(conn, sql);

					System.out.printf("%d번 게시글이 생성되었습니다\n", id);

				} else if (cmd.equals("article list")) {

					List<Article> articles = new ArrayList<>();

					SecSql sql = new SecSql();

					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("ORDER BY id DESC");

					List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

					for (Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}
					if (articles.size() == 0) {
						System.out.println("존재하는 게시물이 없습니다");
						continue;
					}

					System.out.println("== 게시물 리스트 ==");
					System.out.println("번호	|	제목");

					for (Article article : articles) {
						System.out.printf("%d	|	%s\n", article.id, article.title);
					}

				} else if (cmd.startsWith("article detail ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = new SecSql();
					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);

					Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

					if (articleMap.isEmpty()) {
						System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
						continue;
					}

					Article article = new Article(articleMap);
					System.out.printf("== %d번 게시글 상세보기  ==\n", id);
					System.out.printf("번	 호 : %d\n", article.id);
					System.out.printf("작성날짜 : %s\n", article.regDate);
					System.out.printf("수정날짜 : %s\n", article.updateDate);
					System.out.printf("제	 목 : %s\n", article.title);
					System.out.printf("내	 용 : %s\n", article.body);

				} else if (cmd.startsWith("article modify ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = SecSql.from("SELECT COUNT(*)");
					sql.append("FROM article");
					sql.append(" WHERE id = ?", id);

					int articleCount = DBUtil.selectRowIntValue(conn, sql);

					if (articleCount == 0) {
						System.out.printf("%d번 게시글은 존재하지 않습니다.\n", articleCount);
						continue;
					}

					System.out.printf("== %d번 게시글 수정 ==\n", id);
					System.out.printf("수정할 제목 : ");
					String title = sc.nextLine();
					System.out.printf("수정할 내용 : ");
					String body = sc.nextLine();

					sql = SecSql.from("UPDATE article");
					sql.append("SET updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					sql.append(" WHERE id = ?", id);

					DBUtil.update(conn, sql);

					System.out.printf("%d번 게시글이 수정되었습니다\n", id);
				}

				else if (cmd.startsWith("article delete ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = new SecSql();
					sql.append("SELECT COUNT(*) > 0");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);

					boolean isHaveArticle = DBUtil.selectRowBooleanValue(conn, sql);

					if (!isHaveArticle) {
						System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
						continue;
					}

					sql = new SecSql();
					sql.append("DELETE FROM article");
					sql.append("WHERE id = ?", id);

					DBUtil.delete(conn, sql);

					System.out.printf("%d번 게시글이 삭제되었습니다\n", id);
				} else {
					System.out.println("해당 명령어는 존재하지 않습니다.");
				}

			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sc.close();
	}
}