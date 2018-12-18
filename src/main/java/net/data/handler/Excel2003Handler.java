package net.data.handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * excel2003处理
 * @author sifuma@163.com
 *
 */
public class Excel2003Handler {
	
	private Map<String, List<String>> primaryKeysMap = new HashMap<String, List<String>>();
	
	public List<String> getPrimaryKeys(String tableName) {
		return primaryKeysMap.get(tableName + "Primary");
	}

	/**
	 * read excel file
	 * @param file
	 * @return Map<String, List<String[]>>:key is sheetname,value is sheet content every row
	 */
	public Map<String, List<String[]>> readExcel(String file){
		String prePath = Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
		Map<String, List<String[]>> resultMap = new HashMap<String, List<String[]>>();
        try {
            InputStream mInputStream = new FileInputStream(prePath + file);
            Workbook wb = Workbook.getWorkbook(mInputStream);
            for(String sheetName : wb.getSheetNames()){
				List<String> primaryKeys = new ArrayList<String>();
            	Sheet mSheet = wb.getSheet(sheetName);
            	int rowCou = getRowCount(mSheet);
            	int colCou = getColCount(mSheet);
            	List<String[]> rowList = new ArrayList<String[]>();
            	for(int i= 0 ; i < rowCou ; i ++){
            		String[] colArray = new String[colCou];
            		for(int j = 0 ; j < colCou ; j ++){
            			Cell cell = mSheet.getCell(j, i);
            			String content = cell.getContents();
            			colArray[j] = content;
            			//判断单元格有下划线
            			if(i == 0 && 0 < cell.getCellFormat().getFont().getUnderlineStyle().getValue()){
            				primaryKeys.add(content);
            			}
            		}
            		rowList.add(colArray);
            	}
            	resultMap.put(sheetName, rowList);
            	primaryKeysMap.put(sheetName + "Primary", primaryKeys);
            }
            wb.close();
            mInputStream.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return resultMap;
    }

	/**
	 * 读取excel中指定sheet的内容
	 * @param file
	 * @param targetSheet
	 * @return
	 */
	public List<String[]> readExcel(String file, Object targetSheet){
		//String prePath = Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
		List<String[]> rowList = new ArrayList<String[]>();
        try {
            //InputStream mInputStream = new FileInputStream(prePath + file);
            InputStream mInputStream = new FileInputStream(file);
            Workbook wb = Workbook.getWorkbook(mInputStream);
            Sheet mSheet = null;
            if(null == targetSheet)
            	targetSheet = 0;
            if(targetSheet instanceof Integer){
            	mSheet = wb.getSheet((Integer) targetSheet);
            } else if(targetSheet instanceof String){
            	mSheet = wb.getSheet((String) targetSheet);
            } else {
            	mSheet = wb.getSheet(0);
            }
            int rowCou = getRowCount(mSheet);
            int colCou = getColCount(mSheet);
            for(int i= 0 ; i < rowCou ; i ++){
            	String[] colArray = new String[colCou];
                for(int j = 0 ; j < colCou ; j ++){
                    Cell temp = mSheet.getCell(j, i);
                    String content = temp.getContents();
                    colArray[j] = content;
                }
                rowList.add(colArray);
            }
            wb.close();
            mInputStream.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return rowList;
    }
	
	
	/**
	 * 获取指定sheet中，第一列不为空的总行数
	 * @param mSheet
	 * @return
	 */
	private static int getRowCount(Sheet mSheet){
		int count = 0;
		int row = mSheet.getRows();
		for(int i=0; i<row; i++){
			String content = mSheet.getCell(0, i).getContents();
			if(TextUtils.isEmpty(content) || TextUtils.isBlank(content)){
				continue;
			}
			++count;
		}
		return count;
	}
	
	/**
	 * 获取指定sheet中，第一行不为空的总列数
	 * @param mSheet
	 * @return
	 */
	private static int getColCount(Sheet mSheet){
		int count = 0;
		int columns = mSheet.getColumns();
		for(int i=0; i<columns; i++){
			String content = mSheet.getCell(i, 0).getContents();
			if(TextUtils.isEmpty(content) || TextUtils.isBlank(content)){
				continue;
			}
			++count;
		}
        return count;
	}
	
//	public static void main(String[] args) {
//		readExcel("D:/work/workspace/server-ats/src/main/resources/com/cmcc/ats/server/handle/test.xls",null);
//	}
}
