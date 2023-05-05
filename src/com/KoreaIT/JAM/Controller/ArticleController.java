package com.KoreaIT.JAM.Controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.JAM.Article;
import com.KoreaIT.JAM.Service.ArticleService;
import com.KoreaIT.JAM.util.DBUtil;
import com.KoreaIT.JAM.util.SecSql;

public class ArticleController {

	private Scanner sc;
	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc) {
		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite() {

		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(title, body);

		System.out.printf("%d번 게시글이 생성되었습니다\n", id);

	}

	public void showList() {

		List<Article> articles = articleService.getArticles();

		if (articles.size() == 0) {
			System.out.println("존재하는 게시물이 없습니다");
			return;
		}
		System.out.println("== 게시물 리스트 ==");
		System.out.println("번호	|	제목");

		for (Article article : articles) {
			System.out.printf("%d	|	%s\n", article.id, article.title);
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
		System.out.printf("작성날짜 : %s\n", article.regDate);
		System.out.printf("수정날짜 : %s\n", article.updateDate);
		System.out.printf("제	 목 : %s\n", article.title);
		System.out.printf("내	 용 : %s\n", article.body);

	}

	public void doModify(String cmd) {

		articleService.doModify();

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

	public void doDelete(String cmd) {

		articleService.doDelete();

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

	}

}