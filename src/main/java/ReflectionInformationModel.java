

public class ReflectionInformationModel {
	public String path;
	public String apkName;
	public String pkgName;
	public String signature;
	public String codeClass;
	public String codeMethod;
	public String invokeString;
	
	public ReflectionInformationModel(String path, String apkName, String pkgName, String signature, String codeClass, 
			String codeMethod, String invokeString) {
		this.path = path;
		this.apkName = apkName;
		this.pkgName = pkgName;
		this.signature = signature;
		this.codeClass = codeClass;
		this.codeMethod = codeMethod;
		this.invokeString = invokeString;
	}
}
