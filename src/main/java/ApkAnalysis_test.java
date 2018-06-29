

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xmlpull.v1.XmlPullParserException;

import soot.*;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.handlers.ResultsAvailableHandler;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ApkAnalysis_test {
    private String apkFile;
    private String androidPath;
    private Logger logger = LogManager.getLogger();
    private ApkMetaInfo apkMetaInfo;
    private List<ReflectionInformationModel> resultList;
    
    public ApkAnalysis_test(String androidPath, String apkFile){
        this.apkFile = apkFile;
        this.androidPath = androidPath;
        apkMetaInfo = new ApkMetaInfo(apkFile);
        resultList = new ArrayList<>();
    }

    public long doAnalysis() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, XmlPullParserException {
        long start = System.currentTimeMillis();

        ResultsAvailableHandler handler = (icfg, infoflowResults) -> {
            SootMethod dummyMain = Scene.v().getMethod("<dummyMainClass: void dummyMainMethod(java.lang.String[])>");
            traverse(icfg,  dummyMain, new HashSet<>());
        };
        runWithSoot(this.androidPath, this.apkFile, handler);

        return (System.currentTimeMillis()-start)/1000;
    }

    public void runWithSoot(String androidPath, String apkFile, ResultsAvailableHandler handler) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, XmlPullParserException {
        SetupApplication app = new SetupApplication(androidPath, apkFile);
        InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);

        app.setConfig(config);
        app.addResultsAvailableHandler(handler);

        //app.runInfoflow("SourcesAndSinks.txt");
        app.constructCallgraph();

        VendorvulService.getInstance().insertIntoYingyongbao_reflection(resultList);
    }

    public void traverse(IInfoflowCFG cfg, SootMethod method, HashSet<String> visited){
        if (method.hasActiveBody() && !visited.contains(method.getSignature())){
            visited.add(method.getSignature());

            logger.info("###Get Into: "+method.getSignature()+" ###");

            for(Unit unit : method.getActiveBody().getUnits()) {
            	if (unit.toString().contains("java.lang.reflect.Method: java.lang.Object invoke")) {
            		//logger.info("Unit: "+unit.toString());
            		resultList.add(new ReflectionInformationModel(apkFile, 
            				apkMetaInfo.getAppName(), apkMetaInfo.getPackageName(), 
            				method.getSignature(),
            				method.getDeclaringClass().getName(), 
            				method.getName(), unit.toString()));
				}
            }
            
            
            method.getActiveBody().getUnits().forEach(unit -> {
            	for (SootMethod calleemethod : cfg.getCalleesOfCallAt(unit)) {
            		traverse(cfg, calleemethod, visited);
            	}
            });
        }
    }
   
}
