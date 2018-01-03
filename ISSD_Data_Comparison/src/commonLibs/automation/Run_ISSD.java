package commonLibs.automation;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class Run_ISSD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestNG oTestNG;
		String sSuiteFile;
		String sResultPath;
		String sNewFolderName; 
		List<String> oSuiteFiles;
		int iResult;
		
		sSuiteFile = "testng.xml";
		
		sResultPath  = "./test-output/";
			
		sNewFolderName = "Result as on" + userAutomationLibrary.getDateTimeStamp();
		sResultPath = sResultPath + sNewFolderName;
		
		oSuiteFiles =  new ArrayList<String>();
		oSuiteFiles.add(sSuiteFile);
			
			
		oTestNG = new TestNG();
		
		oTestNG.setOutputDirectory(sResultPath);
		oTestNG.setTestSuites(oSuiteFiles);
		oTestNG.run();
		iResult = oTestNG.getStatus();
		if(iResult==0)
		{
			System.out.println("Execution: PASSED");
		}
		else
		{
			System.out.println("Execution: Failed");
		}
		
	}
	
}
