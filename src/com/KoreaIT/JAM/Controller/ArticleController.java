package com.KoreaIT.JAM.Controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.JAM.Article;
import com.KoreaIT.JAM.Service.ArticleService;
import com.KoreaIT.JAM.session.Session;
import com.KoreaIT.JAM.util.Util;

public class ArticleController {

	private Scanner sc;
	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc) {
		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite() {

		if (Session.islogined() == false) {
			System.out.println("로그인 중이 아닙니다.");
			return;
		}

		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(title, body, Session.loginedMemberId);

		System.out.printf("%d번 게시글이 생성되었습니다\n", id);

	}

	public void showList(String cmd) {
		
		String searchKeyword = cmd.substring("article list".length()).trim();

		List<Article> articles = articleService.getArticles(searchKeyword);

		if (articles.size() == 0) {
			System.out.println("존재하는 게시물이 없습니다");
			return;
		}
		System.out.println("== 게시물 리스트 ==");
		System.out.println("번 호	|	제 목	|	작성자	|	조회수	|	날  짜	");

		for (Article article : articles) {
			System.out.printf("%d	|	%s	|	%s	|	%d	|	%s\n", article.id, article.title, article.writerName, article.views, Util.datetimeFormat(article.regDate));
		}

	}

	public void showDetail(String cmd) {

		int id = Integer.parseInt(cmd.split(" ")[2]);
		Article article = articleService.getArticle(id);

		if (article == null) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.printf("== %d번 게시글 상세보기  ==\n", id);
		System.out.printf("번	 호 : %d\n", article.id);
		System.out.printf("작성날짜 : %s\n", Util.datetimeFormat(article.regDate));
		System.out.printf("수정날짜 : %s\n", Util.datetimeFormat(article.updateDate));
		System.out.printf("작 성 자 : %s\n", article.writerName);
		System.out.printf("제	 목 : %s\n", article.title);
		System.out.printf("내	 용 : %s\n", article.body);
		System.out.printf("조 회 수 : %d\n", article.views);

	}

	public void doModify(String cmd) {

		if (Session.islogined() == false) {
			System.out.println("로그인 중이 아닙니다.");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		if (Session.loginedMemberId != article.loginedId) {
			System.out.printf("%d번 게시글 수정 권한이 없습니다.\n", id);
			return;
		}

		System.out.printf("== %d번 게시글 수정 ==\n", id);
		System.out.printf("수정할 제목 : ");
		String title = sc.nextLine();
		System.out.printf("수정할 내용 : ");
		String body = sc.nextLine();

		articleService.doModify(title, body, id);

		System.out.printf("%d번 게시글이 수정되었습니다\n", id);
	}

	public void doDelete(String cmd) {

		if (Session.islogined() == false) {
			System.out.println("로그인 중이 아닙니다.");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}
		if (Session.loginedMemberId != article.loginedId) {
			System.out.printf("%d번 게시글 삭제 권한이 없습니다.\n", id);
			return;
		}

		articleService.doDelete(id);

		System.out.printf("%d번 게시글이 삭제되었습니다\n", id);

	}

}