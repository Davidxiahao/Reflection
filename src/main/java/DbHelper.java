

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbHelper {
    private final String CLASS_NAME;    // "org.sqlite.JDBC"
    private final String DB_URL;        // "jdbc:sqlite:origins.db"

    private Logger logger = LogManager.getLogger();

    public DbHelper(String CLASS_NAME, String DB_URL) {
        this.CLASS_NAME = CLASS_NAME;
        this.DB_URL = DB_URL;
    }

    /**
     * @return
     */
    public Connection createConnection(){
        Connection conn = null;
        try{
            Class.forName(this.CLASS_NAME);
            conn = DriverManager.getConnection(this.DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return conn;
    }

    /**
     * @param sql
     * @return
     */
    public int doUpdate(String sql) {
        try (Connection conn = createConnection();
             Statement stmt = conn.createStatement())
        {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return -1;
    }

    /**
     * @param sql
     * @param statementPreparor
     * @return
     */
    public int doUpdate(String sql, StatementPreparor statementPreparor) throws SQLException{
        try (Connection conn = createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            statementPreparor.prepare(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }


    /**
     * do batch update
     * @param sql
     * @param statementPreparor
     * @return
     */
    public int[] doBatchUpdate(String sql, StatementPreparor statementPreparor) {
        try (Connection conn = createConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            statementPreparor.prepare(ps);
            conn.setAutoCommit(false);
            int[] ret = ps.executeBatch();
            conn.commit();
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    /**
     * @param sql
     * @param resultHandler
     */
    public void doQuery(String sql, ResultHandler resultHandler) {
        try (Connection conn = createConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            resultHandler.handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    /**
     * @param sql
     * @param statementPreparor
     * @param resultHandler
     */
    public void doQuery(String sql, StatementPreparor statementPreparor, ResultHandler resultHandler) {
        try (Connection conn = createConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            statementPreparor.prepare(ps);
            ResultSet rs = ps.executeQuery();
            resultHandler.handle(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }





}