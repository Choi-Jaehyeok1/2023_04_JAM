package com.KoreaIT.JAM.Dao;

import java.sql.Connection;
import java.util.Map;

import com.KoreaIT.JAM.util.DBUtil;
import com.KoreaIT.JAM.util.SecSql;

public class MemberDao {
	
	private Connection conn;
	
	public MemberDao(Connection conn) {
		this.conn = conn;
	}

	public boolean isLoginIdDup(String LoginId) {
		
		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*) > 0");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", LoginId);
		
		return DBUtil.selectRowBooleanValue(conn, sql);
	}

	public void doJoin(String LoginId, String LoginPw, String name) {
		
		SecSql sql = new SecSql();
		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", LoginId);
		sql.append(", loginPw = ?", LoginPw);
		sql.append(", name = ?", name);
		
		DBUtil.insert(conn, sql);
	}

	public Map<String, Object> getMember(String LoginId) {
		
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", LoginId);

		return DBUtil.selectRow(conn, sql);
	}
}