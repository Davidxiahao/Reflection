

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultHandler {
	void handle(ResultSet rs) throws SQLException;
}
