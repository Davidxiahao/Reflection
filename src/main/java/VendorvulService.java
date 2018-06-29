

import java.util.List;

public class VendorvulService {
	private final DbHelper dbHelper;
	static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/vendorvul?user=vendorvul&password=vendorvul";
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	
	private static VendorvulService ourInstance = new VendorvulService();
	
	public static VendorvulService getInstance() {return ourInstance;}
	
	private VendorvulService() {
		dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
	}
	
	public void insertIntoYingyongbao_reflection(List<ReflectionInformationModel> list) {
		String sql = "insert into yingyongbao_reflection (path, apkName, pkgName, "
				+ "signature, codeClass, codeMethod, invokeString) values (?, ?, ?, "
				+ "?, ?, ?, ?)";
		dbHelper.doBatchUpdate(sql, ps -> {
			for(ReflectionInformationModel line : list) {
				ps.setString(1, line.path);
				ps.setString(2, line.apkName);
				ps.setString(3, line.pkgName);
				ps.setString(4, line.signature);
				ps.setString(5, line.codeClass);
				ps.setString(6, line.codeMethod);
				ps.setString(7, line.invokeString);
				ps.addBatch();
			}
		});
	}
}
