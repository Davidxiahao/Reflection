

import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, XmlPullParserException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    	
    	String androidJars = args[0];
    	String apkFileLocation = args[1];

		ApkAnalysis_test apkAnalysis_test = new ApkAnalysis_test(androidJars, apkFileLocation);
		System.out.println("Time used: " + apkAnalysis_test.doAnalysis());
    }
}
