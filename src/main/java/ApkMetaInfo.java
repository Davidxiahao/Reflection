

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import soot.jimple.infoflow.android.manifest.ProcessManifest;

public class ApkMetaInfo {
	private String packageName;
	private String appName;
	private String apk;
	private int minSdkVersion;
	private int targetSdkVersion;
	
	public ApkMetaInfo(String apkPath) {
		ProcessManifest processManifest = null;
		try {
			processManifest = new ProcessManifest(apkPath);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		this.packageName = processManifest.getPackageName();
		this.appName = processManifest.getApplicationName();
		this.minSdkVersion = processManifest.getMinSdkVersion();
		this.targetSdkVersion = processManifest.targetSdkVersion();
		this.apk = apkPath+"; "+appName;
	}
	
	public String getApk() {
		return apk;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public int getMinSdkVersion() {
		return minSdkVersion;
	}
	
	public int getTargetSdkVersion() {
		return targetSdkVersion;
	}
}
