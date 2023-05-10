package com.KoreaIT.JAM.Dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.KoreaIT.JAM.util.DBUtil;
import com.KoreaIT.JAM.util.SecSql;

public class ArticleDao {

	private Connection conn;

	public ArticleDao(Connection conn) {
		this.conn = conn;
	}

	public int doWrite(String title, String body, int loginedId) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginedId = ?", loginedId);
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);

		return DBUtil.insert(conn, sql);
	}

	public List<Map<String, Object>> getArticles() {

		SecSql sql = new SecSql();

		sql.append("SELECT A.*, M.name AS writerName");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("On A.loginedId = M.id");
		sql.append("ORDER BY a.id DESC");

		return DBUtil.selectRows(conn, sql);
	}

	public Map<String, Object> getArticle(int id) {

		SecSql sql = new SecSql();
		sql.append("SELECT A.*, M.name AS writerName");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("On A.loginedId = M.id");
		sql.append("WHERE A.id = ?", id);

		return DBUtil.selectRow(conn, sql);
	}


	public void doModify(String title, String body, int id) {

		SecSql sql = new SecSql();
		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append(" WHERE id = ?", id);

		DBUtil.update(conn, sql);

	}

	public void doDelete(int id) {

		SecSql sql = new SecSql();
		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);

		DBUtil.delete(conn, sql);

	}

}
