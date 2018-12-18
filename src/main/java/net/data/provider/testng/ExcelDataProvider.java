package net.data.provider.testng;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.DataProvider;

import net.data.handler.Excel2003Handler;

/**
 * Data Driver
 * 
 *
 */
public class ExcelDataProvider {
	private static Log LOGGER = LogFactory.getLog(ExcelDataProvider.class);
	
	private static Excel2003Handler excHandler = new Excel2003Handler();
	
	private static List<String> titles = new ArrayList<String>();
	
	public static List<String> getTitles() {
		return titles;
	}

	@DataProvider
	public static Object[][] defaultMethod(Method method) {
		String prePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		Object targetSheet = 0;
		String classPath = method.getDeclaringClass().getName().replace(".", File.separator);
		String file = prePath + classPath.toLowerCase() + File.separator + method.getName() + ".xls";
		if(!new File(file).exists()){
			file = prePath + classPath + ".xls";
			targetSheet = method.getName();
		}
		LOGGER.debug("DataFile----->" + file);
		LOGGER.debug("targetSheet----->" + targetSheet);
		List<String[]> paramList = excHandler.readExcel(file, targetSheet);
		
		for(String str : paramList.get(0)){
			titles.add(str);
		}
		String[][] params = new String[paramList.size()-1][];
		for(int i = 0; i < paramList.size()-1; i++){
			params[i] = paramList.get(i+1);
		}
		return params;
	}

}
