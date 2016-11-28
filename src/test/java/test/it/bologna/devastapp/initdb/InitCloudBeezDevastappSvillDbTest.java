package test.it.bologna.devastapp.initdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.it.bologna.devastapp.util.TestDefinitionAndUtils;
import test.it.bologna.devastapp.util.UtilityScriptSql;

/**
 * 
 * Crea il DB (SCHEMA e TABELLE) per i TEST UNITARI.
 * 
 * @author ascuderetti
 */
@Ignore
public class InitCloudBeezDevastappSvillDbTest {

	private final static Logger LOG = LoggerFactory
			.getLogger(InitCloudBeezDevastappSvillDbTest.class);

	private static final String[] DB_INIT_SCRIPTS = {
			TestDefinitionAndUtils.SCRIPT_DROP_TABELLE,
			TestDefinitionAndUtils.SCRIPT_CREA_TABELLE,
			TestDefinitionAndUtils.SCRIPT_POPOLA_DB };

	/**
	 * <p>
	 * <b>Obiettivo</b> <br>
	 * Crea il DataBase per i Test Unitari.
	 * </p>
	 * <p>
	 * <b>Risultati attesi</b>
	 * <ol>
	 * <li>
	 * Nessun errore</li>
	 * </ol>
	 * </p>
	 * 
	 * @throws SQLException
	 * @throws TestException
	 * 
	 * @PDF
	 */
	@Test
	public void initSchemaDbTest() throws SQLException, Exception {

		Connection conn = null;

		try {
			// carica il driver
			Class.forName(TestDefinitionAndUtils.DB_DRIVER_MYSQL);
			conn = DriverManager.getConnection(
					TestDefinitionAndUtils.DB_URL_CLOUDBEES,
					TestDefinitionAndUtils.DB_USER_CLOUDBEES,
					TestDefinitionAndUtils.DB_PASS_CLOUDBEES);
			conn.setAutoCommit(false);

			LOG.info("Crea DB");
			for (String scriptName : DB_INIT_SCRIPTS) {
				LOG.info("Caricamento script: " + scriptName);
				UtilityScriptSql utility = new UtilityScriptSql(scriptName,
						TestDefinitionAndUtils.DB_DRIVER_MYSQL,
						TestDefinitionAndUtils.DB_URL_CLOUDBEES,
						TestDefinitionAndUtils.DB_USER_CLOUDBEES,
						TestDefinitionAndUtils.DB_PASS_CLOUDBEES);
				utility.execute(conn);
			}
			LOG.info("Creazione schemi completato");

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
							"Errore nella ciusura della connessione: "
									+ ex.getLocalizedMessage(), ex);
				}
			}
		}

	}

}
