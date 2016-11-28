package test.it.bologna.devastapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * 
 * @author lfelici
 */
public class UtilityScriptSql {

	private final static Logger LOG = Logger.getLogger(UtilityScriptSql.class);

	private final static char QUERY_ENDS = ';';
	private final static char COMMENT = '-';

	private Connection conn;
	private Statement stat;

	private String scriptFileName;
	private String dbDriver;
	private String dbConnectionSDtring;
	private String user;
	private String password;

	/**
	 * 
	 * @param scriptFileName
	 * @throws SQLException
	 */
	public UtilityScriptSql(String scriptFileName, String dbDriver,
			String dbConnectionSDtring, String user, String password) {
		this.scriptFileName = scriptFileName;
		this.dbDriver = dbDriver;
		this.dbConnectionSDtring = dbConnectionSDtring;
		this.user = user;
		this.password = password;
	}

	/**
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	private void loadScript() throws IOException, SQLException {

		BufferedReader reader = null;

		try {

			InputStream inStream = UtilityScriptSql.class.getClassLoader()
					.getResourceAsStream(scriptFileName);

			if (inStream == null) {
				throw new RuntimeException("Stream nullo");
			}

			reader = new BufferedReader(new InputStreamReader(inStream));

			boolean queryEnds = false;
			String line;
			while ((line = reader.readLine()) != null) {
				if (isComment(line))
					continue;

				queryEnds = checkStatementEnds(line);

				line = line.trim();

				if (queryEnds) {
					line = line.substring(0, line.indexOf(QUERY_ENDS));
					stat.addBatch(line);
				}
			}

		} finally {

			if (reader != null) {
				reader.close();
			}
		}
	}

	private boolean isComment(String line) {
		if ((line != null) && (line.length() > 0)) {
			return (line.charAt(0) == COMMENT);
		}
		return false;
	}

	public int execute() throws IOException, SQLException {

		conn = getConnection();
		stat = conn.createStatement();

		loadScript();
		int numberStatement = 0;

		try {

			int[] result = stat.executeBatch();
			numberStatement = result.length;

		} finally {
			closeResources(stat, conn);
		}

		return numberStatement;
	}

	public int execute(Connection connection) throws IOException, SQLException {

		stat = connection.createStatement();

		loadScript();
		int numberStatement = 0;

		try {

			int[] result = stat.executeBatch();
			numberStatement = result.length;

		} finally {
			closeResources(stat, null);
		}

		return numberStatement;
	}

	private boolean checkStatementEnds(String s) {
		return (s.indexOf(QUERY_ENDS) != -1);
	}

	private Connection getConnection() {

		Connection connection = null;
		try {
			Class.forName(dbDriver);

			connection = DriverManager.getConnection(dbConnectionSDtring, user,
					password);

		} catch (SQLException ex) {

			LOG.error(ex.getLocalizedMessage(), ex);

		} catch (ClassNotFoundException ex) {

			LOG.error(ex.getLocalizedMessage(), ex);
		}
		return connection;
	}

	/**
	 * Metodo da inserire in finally quando si utilizzano risorse jdbc
	 * 
	 * @param prep
	 * @param conn
	 * @throws SQLException
	 */
	private void closeResources(Statement stmt, Connection conn)
			throws SQLException {

		try {

			if (conn != null) {

				if (stmt != null) {

					try {
						stmt.close();
					} catch (SQLException ex) {
						LOG.error(ex.getLocalizedMessage(), ex);
						throw ex;
					}
				}
			}

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOG.error(ex.getLocalizedMessage(), ex);
					throw ex;
				}
			}
		}
	}

}
