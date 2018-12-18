package net.protocol.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.TextUtils;

import net.CommonConsts;

public class HttpClientImpl extends AbstractHttpClient {
	private static Log LOGGER = LogFactory.getLog(HttpClientImpl.class);
	
	private String uri;
	private Header[] headers;
	private String params;
	private CookieStore cookieStore;
	
	/**
	 * 
	 * @param uri  请求地址，如：http://172.23.23.97/ems_cmcc/new/getEnterprise
	 * @param headers  请求头信息（请求头之间以\r\n分隔），如：Connection: keep-alive\r\nContent-Type: application/x-www-form-urlencoded\r\nContent-Length: 52\r\nHost: 172.23.23.97\r\nUser-Agent: Apache-HttpClient/4.5.2 (Java/1.8.0_111)
	 * @param params   请求参数（参数之间以&分隔），如：loginName=18867103603&loginPassword=123456789&type=1
	 */
	public HttpClientImpl(String uri, String headers, String params){
		this.uri = uri;
		this.headers = super.getHeaders(headers, CommonConsts.LF);
		this.params = params;
	}
	
	/**
	 * 
	 * @param uri  请求地址，如：http://172.23.23.97/ems_cmcc/new/getEnterprise
	 * @param headers  请求头信息（请求头之间以\r\n分隔），如：Connection: keep-alive\r\nContent-Type: application/x-www-form-urlencoded\r\nContent-Length: 52\r\nHost: 172.23.23.97\r\nUser-Agent: Apache-HttpClient/4.5.2 (Java/1.8.0_111)
	 * @param params   请求参数（参数之间以&分隔），如：loginName=18867103603&loginPassword=123456789&type=1
	 * @param cookieStore
	 */
	public HttpClientImpl(String uri, String headers, String params, CookieStore cookieStore){
		this.uri = uri;
		this.headers = super.getHeaders(headers, CommonConsts.LF);
		this.params = params;
		this.cookieStore = cookieStore;
	}
	
////////////////////////// below method is send http request method //////////////////////////////////////
	public HttpResponse doRequest(String method) throws ClientProtocolException, IOException{
		HttpResponse response = null;
		switch(HttpMethod.getHttpMethod(method)){
		case PUT:
			response = doPut();
			break;
		case DELETE:
			response = doDelete();
			break;
		case POST:
			response = doPost();
			break;
		case GET:
			response = doGet();
			break;
		case OPTIONS:
		case HEAD:
		case TRACE:
		case CONNECT:
		default:
			LOGGER.error("Temporarily does not support this method.");
			break;
		}
		return response;
		
	}
	
////////////////////////// below methods are http request methods //////////////////////////////////////
	
	/**
	 * Send Put request
	 * @return  返回响应信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse doPut() throws ClientProtocolException, IOException{
		HttpPut httpPut = new HttpPut(uri);
		httpPut.setHeaders(headers);
		httpPut.setEntity(getHttpEntity(params, CommonConsts.AND));
		return sendRequest(httpPut, cookieStore);
	}

	/**
	 * Send Delete request
	 * @return  返回响应信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse doDelete() throws ClientProtocolException, IOException{
		HttpDelete httpDelete = new HttpDelete(uri + "?" + params);
		httpDelete.setHeaders(headers);
		return sendRequest(httpDelete, cookieStore);
	}
	
	/**
	 * Send POST request
	 * @return  返回响应信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse doPost() throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(uri); // +"?"+ params
		httpPost.setHeaders(headers);
        httpPost.setEntity(getHttpEntity(params, CommonConsts.AND));
		return sendRequest(httpPost, cookieStore);
	}
	
	/**
	 * Send Get request
	 * @return  返回响应信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse doGet() throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(uri + "?" + params);
		httpGet.setHeaders(headers);
		return sendRequest(httpGet, cookieStore);
	}
	
////////////////////////// below methods are file upload and file download //////////////////////////////////////
	
	/**
	 * file upload
	 * @param file  file path and file name
	 * @param desc  file description
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse uploadFile(String file, String desc) throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeaders(headers);
		FileBody fileBody = new FileBody(new File(file));
		// 相当于<input type="file" name="file"/>
		MultipartEntityBuilder meb = MultipartEntityBuilder.create().addPart(fileBody.getFilename(), fileBody);
        for(String param:params.split(CommonConsts.AND)){
			if(TextUtils.isEmpty(param.trim())) continue;
			String name = param.split("=")[0].trim();
			String value = param.split("=")[1].trim();
			StringBody sb = new StringBody(value, ContentType.create("text/plain", contentType.getCharset())); 
			// 相当于<input type="text" name="userName" value=userName>
			meb.addPart(name, sb);
		}
        httpPost.setEntity(meb.build());
        return sendRequest(httpPost, cookieStore);	
	}
	
	/**
	 * file download
	 * @param remoteFileName
	 * @param localFileName
	 * @throws IOException 
	 */
	public boolean downloadFile(String remoteFileName, String localFileName) throws IOException {
		HttpGet httpGet = new HttpGet(uri + "?" + params);
		httpGet.addHeader(new File(remoteFileName).getName(), remoteFileName);

		HttpResponse httpResponse = sendRequest(httpGet, cookieStore);
		HttpEntity entity = httpResponse.getEntity();
		InputStream in = entity.getContent();

		long length = entity.getContentLength();
		if (length <= 0) {
			return false;
		}

		File file = new File(localFileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		OutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[4096];
		int readLength = 0;
		while ((readLength = in.read(buffer)) > 0) {
			byte[] bytes = new byte[readLength];
			System.arraycopy(buffer, 0, bytes, 0, readLength);
			out.write(bytes);
		}
		out.flush();
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
		return true;
    }

}
