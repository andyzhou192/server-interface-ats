package com.asserts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Asserts {

	/**
	 * 判断期望的json串中的值与实际json串中对应的值相等
	 * @param actual
	 * @param expected
	 * @return
	 */
	public static AssertResult assertJson(String actual, String expected){
		List<AssertResult> resultList = new ArrayList<AssertResult>();
		JSONObject actJson = new JSONObject(actual);
		JSONObject expJson = new JSONObject(expected);
		for(String name:expJson.keySet()){
			Object expValue = expJson.get(name);
			Object actValue = actJson.get(name);
			if(expValue instanceof JSONObject){
				resultList.add(assertJson(String.valueOf(actJson.get(name)), String.valueOf(expValue)));
			} else if(expValue instanceof JSONArray){
				JSONArray expJsonArray = (JSONArray)expValue;
				Iterator<Object> expIt = expJsonArray.iterator();
				while (expIt.hasNext()) {
					JSONObject expOb = (JSONObject) expIt.next();
					JSONArray actJsonArray = (JSONArray)actValue;
					Iterator<Object> actIt = actJsonArray.iterator();
					while (actIt.hasNext()) {
						JSONObject actOb = (JSONObject) actIt.next();
						resultList.add(assertJson(actOb.toString(), expOb.toString()));
					}
				}
			} else {
				String message = "expected:" + expValue + ", actual:" + actValue;
				if(null == expValue)
					resultList.add(new AssertResult(null == actValue, message));
				else{
					boolean isEqual = String.valueOf(expValue).equals(String.valueOf(actValue));
					resultList.add(new AssertResult(isEqual, message));
				}
			}
		}
		AssertResult result = new AssertResult(true, "");
		for(AssertResult r : resultList){
			if(!r.isSucc()){
				result = r;
				break;
			}
		}
		return result;
	}

}
