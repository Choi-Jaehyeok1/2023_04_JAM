package com.KoreaIT.JAM.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.KoreaIT.JAM.Article;
import com.KoreaIT.JAM.Dao.ArticleDao;

public class ArticleService {

	private ArticleDao articleDao;
	
	public ArticleService(Connection conn) {
		this.articleDao = new ArticleDao(conn);
	}

	public int doWrite(String title, String body) {
		return articleDao.doWrite(title, body);
		
	}

	public List<Article> getArticles() {
		List<Map<String, Object>> articleListMap = articleDao.getArticles();
		
		List<Article> articles = new ArrayList<>();
		
		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}
		return articles;
	}

	public Article getArticle(int id) {

		Map<String, Object> articleMap = articleDao.getArticle(id);
		new Article(articleMap);
		
		if (articleMap.isEmpty()) {
			return null;
		}
		
		return new Article(articleMap);
		
	}

	public void doModify() {
		articleDao.doModify();

	}

	public void doDelete() {
		articleDao.doDelete();

	}

	
	
	
	
	
	
}