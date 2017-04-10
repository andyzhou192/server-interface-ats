package com.protocol.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.asserts.AssertResult;
import com.asserts.Asserts;
import com.data.provider.testng.ExcelDataProvider;
import com.protocol.httpclient.HttpClientImpl;

/**
 * 
 * @author Administrator
 * @date
 *
 */
public class TestngTest {
	private static Log LOGGER = LogFactory.getLog(TestngTest.class);
	
	@Test(groups = { "SMOKE" },dataProvider="defaultMethod",dataProviderClass=ExcelDataProvider.class)
    public void testLoginPsonSmoke(String caseId, String desc, String method, String url, String headers, String params, String rspCode,String expected){
		LOGGER.info("########## "+desc+" ##########"+" BEGIN ##########");
		try {
			HttpClientImpl client = new HttpClientImpl(url, headers, params);
			HttpResponse rsp = client.doRequest(method);
			
			Assert.assertEquals(String.valueOf(rsp.getStatusLine().getStatusCode()), rspCode);
			if(null != expected && expected.trim().length() > 0){
				AssertResult result = Asserts.assertJson(EntityUtils.toString(rsp.getEntity()), expected);
				Assert.assertTrue(result.isSucc(), result.getMessage());
			}
//			HttpClientImpl client2 = new HttpClientImpl(url, header, param, client.getCookieStore());
//			HttpResponse rsp2 = client2.doRequest(method); //GET
		}
		catch(Exception e) {
			Assert.fail(e.toString());
		}finally{
			LOGGER.info("########## "+desc+" ##########"+" END ##########");
		}		
    }
	
	public static void main(String[] args) {
		List<Boolean> resultList = new ArrayList<Boolean>();
		resultList.add(true);
		resultList.add(true);
		resultList.add(false);
		resultList.add(true);
		resultList.add(true);
		System.out.println(resultList.contains(Boolean.FALSE) ? false : true);
	}
	
}
