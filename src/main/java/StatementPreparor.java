

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementPreparor {
    void prepare(PreparedStatement ps) throws SQLException;
}