package test.it.bologna.devastapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDefinitionAndUtils {

	private static final Logger LOG = LoggerFactory
			.getLogger(TestDefinitionAndUtils.class);

	/*
	 * CONFIGURAZIONI DB DI TEST
	 */
	public static final String DB_DRIVER_TEST = "org.hsqldb.jdbcDriver";
	public static final String DB_USER_TEST = "sa";
	public static final String DB_PASS_TEST = "";
	// Aggiunto ;shutdown=true per avere il commit subito con HSQLDB:
	public static final String DB_URL_RISORSE_TEST = "jdbc:hsqldb:file:src/test/resources/testdb/testdb;sql.syntax_mys=true;shutdown=true";
	public static final String DB_URL_TARGET_TEST = "jdbc:hsqldb:file:./target/test-classes/testdb/testdb;sql.syntax_mys=true;shutdown=true";

	/*
	 * CONFIGURAZIONI DB DI MYSQL
	 */
	public static final String DB_DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	/*
	 * CONFIGURAZIONI DB DI CLOUDBEES SVILUPPO
	 */
	public static final String DB_USER_CLOUDBEES = "devastappsvil";
	public static final String DB_PASS_CLOUDBEES = "devastappsvil";
	public static final String DB_HOST_CLOUDBEES = "ec2-23-21-211-172.compute-1.amazonaws.com";
	public static final String DB_PORT_CLOUDBEES = "3306";
	public static final String DB_URL_CLOUDBEES = "jdbc:mysql://"
			+ DB_HOST_CLOUDBEES + ":" + DB_PORT_CLOUDBEES + "/devastappsvil";

	/*
	 * CONFIGURAZIONI DB MYSQL DI TEST
	 */
	public static final String DB_URL_MYSQL_LOCAL = "jdbc:mysql://"
			+ "localhost" + ":" + DB_PORT_CLOUDBEES + "/devastappsvil";

	/*
	 * SCRIPT DB
	 */
	public static final String SCRIPT_CREA_TABELLE = "scriptdb/1_2_Tabelle.sql";
	public static final String SCRIPT_DROP_TABELLE = "scriptdb/1_1_DropTabelle.sql";
	public static final String SCRIPT_CANCELLA_TABELLE = "scriptdb/2_DeleteTabelle.sql";
	public static final String SCRIPT_POPOLA_DB = "scriptdb/3_Popola.sql";

	/**
	 * Richiama lo script {@link this#SCRIPT_CANCELLA_TABELLE} sul db di test in
	 * target {@link this#DB_URL_TARGET_TEST}
	 * 
	 * @throws Exception
	 * 
	 */
	public static void pulisciTabelleTargetDb() throws Exception {

		Connection conn = null;

		try {
			// carica il driver
			Class.forName(TestDefinitionAndUtils.DB_DRIVER_TEST);
			conn = DriverManager.getConnection(
					TestDefinitionAndUtils.DB_URL_TARGET_TEST,
					TestDefinitionAndUtils.DB_USER_TEST,
					TestDefinitionAndUtils.DB_PASS_TEST);
			conn.setAutoCommit(false);

			UtilityScriptSql utility = new UtilityScriptSql(
					TestDefinitionAndUtils.SCRIPT_CANCELLA_TABELLE,
					TestDefinitionAndUtils.DB_DRIVER_TEST,
					TestDefinitionAndUtils.DB_URL_TARGET_TEST,
					TestDefinitionAndUtils.DB_USER_TEST,
					TestDefinitionAndUtils.DB_PASS_TEST);

			utility.execute(conn);

			conn.commit();

		} catch (SQLException ex) {
			LOG.error("Errore sql: " + ex.getLocalizedMessage(), ex);
			throw new SQLException(ex);
		} catch (Exception ex) {
			LOG.error(
					"Errore di inizializzazione database: "
							+ ex.getLocalizedMessage(), ex);
			throw new Exception(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					LOG.error(
							"Errore nella chiusura della connessione: "
									+ ex.getLocalizedMessage(), ex);
				}
			}
		}
	}
}
