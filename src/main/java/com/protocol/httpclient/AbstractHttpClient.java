package com.protocol.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import com.CommonConsts;

public abstract class AbstractHttpClient {
	
	public ContentType contentType = ContentType.create("*/*", "UTF-8");
	private CloseableHttpClient client = HttpClients.createDefault();
	private CookieStore cookieStore = new BasicCookieStore();
	private String domain = null;
	
	/**
	 * Send Request
	 * @param request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse sendRequest(HttpUriRequest request) throws ClientProtocolException, IOException{
		domain = request.getURI().getHost();
		HttpResponse response = client.execute(request);
		if(response.containsHeader("Set-Cookie")){
			for(Header header : response.getHeaders("Set-Cookie")){
				setCookieStore(header.getValue());
			}
		}
		return response;
	}
	
	/**
	 * Send Request with cookie
	 * @param request
	 * @param cs
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse sendRequest(HttpUriRequest request, CookieStore cs) throws ClientProtocolException, IOException{
		HttpResponse response;
		if(null != cs){
			domain = request.getURI().getHost();
			client = HttpClients.custom().setDefaultCookieStore(cs).build();
			response = client.execute(request);
			if(response.containsHeader("Set-Cookie")){
				for(Header header : response.getHeaders("Set-Cookie")){
					setCookieStore(header.getValue());
				}
			}
		} else {
			response = sendRequest(request);
		}
		return response;
	}
	
	/**
	 * 关闭流并释放资源
	 * @throws IOException
	 */
	public void close() throws IOException{
		client.close();
	}
	
	/**
	 * get Headers from header String separate by separator
	 * @param headerStr : header String
	 * @param separator
	 * @return
	 */
	public Header[] getHeaders(String headerStr, String separator){
		headerStr = unifyCRLF(headerStr, separator);
		HeaderGroup headergroup = new HeaderGroup();
		if(!TextUtils.isEmpty(headerStr) && !TextUtils.isBlank(headerStr)){
			for(String header : headerStr.split(separator)){
				if(TextUtils.isEmpty(header) || TextUtils.isBlank(header)) continue;
				String name = header.split(":")[0].trim();
				String value = header.split(":")[1].trim();
				/**
				 * 解决以下异常
				 * Caused by: org.apache.http.ProtocolException: Content-Length header already present
				 * Caused by: org.apache.http.ProtocolException: Transfer-Encoding header already present
				 */
				if(!name.equals("Content-Length") && !name.equals("Transfer-Encoding")){
					Args.notNull(name, "Header name");
					headergroup.addHeader(new BasicHeader(name, value));
				} else if(name.equals("Content-Type")){
					String mimeType = null; 
					String charset = "UTF-8";
					String sp = ";";
					if(value.contains(sp)){
						mimeType = value.split(sp)[0].trim();
						charset = value.split(sp)[1].split("=")[1].trim();
					} else {
						mimeType = value.trim();
					}
					contentType = ContentType.create(mimeType, charset);
				}
			}
		}
		return headergroup.getAllHeaders();
	}
	
	/**
	 * 统一字符串中的换行符
	 * @param headerStr  待统一换行符的字符串
	 * @param separator  统一后的换行符
	 * @return
	 */
	private static String unifyCRLF(String headerStr, String separator) {
		String result = headerStr.replaceAll(CommonConsts.CRLF, separator);
		result = headerStr.replaceAll(CommonConsts.LINE_SEPARATOR, separator);
		result = headerStr.replaceAll(CommonConsts.CR, separator);
		result = headerStr.replaceAll(CommonConsts.LF, separator);
		return result;
	}

	/**
	 * get HttpEntity from params separate by separator
	 * @param params
	 * @param separator
	 * @return
	 */
	public HttpEntity getHttpEntity(String params, String separator){
		if(TextUtils.isEmpty(params) && TextUtils.isBlank(params)) return null;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(String param:params.split(separator)){
			if(TextUtils.isEmpty(param.trim())) continue;
			String name = param.split("=")[0].trim();
			String value = param.split("=")[1].trim();
			nvps.add(new BasicNameValuePair(name, value));
		}
        return new UrlEncodedFormEntity(nvps, contentType.getCharset());  
	}
	
	/**
	 * 销毁HttpEntity
	 * @param entity
	 * @throws IOException 
	 */
	public void destroyEntity(HttpEntity entity) throws IOException{
		EntityUtils.consume(entity);
	}
	
	/**
	 * 设置存储cookie，参数中不同的cookie用英文版的分号分隔
	 * @param cookies cookies string, different field is separate by ";";
	 */
	public void setCookieStore(String cookies) {
		if(TextUtils.isBlank(cookies) || TextUtils.isEmpty(cookies)) return;
		String[] cookiePairs = cookies.split(";");
		String name = cookiePairs[0].split("=")[0].trim();
		String value = cookiePairs[0].split("=").length > 1 ? cookiePairs[0].split("=")[1].trim() : null;
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		
		String path = "/";
		int version = 0;
		for(int i = 1; i < cookiePairs.length; i++){
			if(!cookiePairs[i].contains("=")) continue;
			String k = cookiePairs[i].split("=")[0].trim();
			String v = cookiePairs[i].split("=").length > 1 ? cookiePairs[i].split("=")[1].trim() : "";
	    	if("Path".equalsIgnoreCase(k)){
	    		path = v;
	    	} else if("Domain".equalsIgnoreCase(k)){
	    		domain = v;
	    	} else if("Comment".equalsIgnoreCase(k)){
	    		cookie.setComment(v);
	    	} else if("Version".equalsIgnoreCase(k)){
	    		version = Integer.valueOf(v);
	    	}
		}
		cookie.setVersion(version);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookieStore.addCookie(cookie);
	  }
	
	/**
	 * 获取cookies
	 * @return
	 */
	public CookieStore getCookieStore(){
		return cookieStore;
	}
	
//	public static void main(String[] args) {
//		String str = "abc\rcd\r\nasdjfk\nsdfg\recd";
//		System.out.println(unifyCRLF(str, "\n"));
//	}
}
