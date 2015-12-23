package com.catenoid.dashbd.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URIException;
//import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpNetAgent {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private int netTimeoutConn = 0;  				//ms
	private int netTimeoutSock = 0;  				//ms
	
	
	public HttpNetAgent(){
		this.netTimeoutConn = 3000;					//1초
		this.netTimeoutSock = 6000;					//3초
	}
	
	public HttpNetAgent(String netTimeoutConn, String netTimeoutSock ){
		this.netTimeoutConn = Integer.parseInt(netTimeoutConn);
		this.netTimeoutSock = Integer.parseInt(netTimeoutSock);
	}
	
	
	/**
	 * http 통신을 한다.GET 방식
	 * 
	 * @param url
	 * @param queryString 
	 * @return 서버에서 받은 body 스트링
	 */
	public String execute(String url, boolean sslExceptionIgnore) throws HttpNetAgentException{
		
		GetMethod getMethod = new GetMethod(url);
		String responseBody = send(getMethod, sslExceptionIgnore);
		
		return responseBody;
	}
	
	/**
	 * http 통신을 한다.GET 방식
	 * 
	 * @param url
	 * @param queryString 
	 * @return 서버에서 받은 body 스트링
	 */
	public String execute(String url, String queryString, boolean sslExceptionIgnore) throws HttpNetAgentException{
		
		GetMethod getMethod = new GetMethod(url);
		getMethod.setQueryString(queryString);
		String responseBody = send(getMethod, sslExceptionIgnore);
		
		return responseBody;
	}
	
	/**
	 * http 통신을 한다.POST 방식
	 * 
	 * @param url
	 * @param params 
	 * @return 서버에서 받은 body 스트링
	 */
	public String execute(String url, String queryString, Map params, boolean sslExceptionIgnore) throws HttpNetAgentException{

		PostMethod postMethod = null;
		String responseBody = null;
		
		postMethod = new PostMethod(url);

		java.util.Iterator ith = params.keySet().iterator();
		while(ith.hasNext()) {
			String paramName = (String)ith.next();
			String paramValue = (String)params.get(paramName);
			postMethod.addParameter(paramName, paramValue);
		}
		
		postMethod.setQueryString(queryString);
		responseBody = send(postMethod, sslExceptionIgnore);
		
		return responseBody;
	}
	
	public String execute(String url, String queryString, String body, boolean sslExceptionIgnore) throws HttpNetAgentException, UnsupportedEncodingException{

		PostMethod postMethod = null;
		String responseBody = null;
		
		postMethod = new PostMethod(url);
		postMethod.setQueryString(queryString);
		
		postMethod.setRequestEntity(new StringRequestEntity(body, null, "UTF-8"));
		responseBody = send(postMethod, sslExceptionIgnore);
		
		return responseBody;
	}
	
	private String send(HttpMethod method, boolean sslExceptionIgnore) throws HttpNetAgentException{
		String responseBody = null;
		HttpClient hc = null;
		
		try {
			this.hashCode();
			String reqLog = ">> " + (method instanceof PostMethod ? "POST" : "GET") + " [" + method.getURI() + "]";
			
			if (method instanceof PostMethod){
				NameValuePair[] params = ((PostMethod)method).getParameters();
				for(int i = 0; i < params.length; i++){
					NameValuePair param = params[i];
					reqLog += "\n" + (param.getName() + "=" + param.getValue());
				}
			}
			reqLog += ", hscd[" + this.hashCode() + "], sslExcIgnore[" + sslExceptionIgnore + "], hs[" + this.hashCode() + "]";
			
			logger.info(reqLog);
			logger.info(getHttpInfoDumy(method));
			
			HttpConnectionManager httpConnMgr = new SimpleHttpConnectionManager();
			HttpConnectionManagerParams httpConnMgrParams = new HttpConnectionManagerParams();

			// Connection Timeout 설정
			httpConnMgrParams.setConnectionTimeout(netTimeoutConn);

			// Socket Timeout 설정
			httpConnMgrParams.setSoTimeout(netTimeoutSock);
			httpConnMgr.setParams(httpConnMgrParams);

			hc = new HttpClient(httpConnMgr);
			/*
			if (sslExceptionIgnore){
				try {
					Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
			}
			*/
			
			int status =  hc.executeMethod(method);
			
			if(200 == status){
				responseBody = method.getResponseBodyAsString();
				logger.info("<< responseBody ["+ responseBody + "], hs[" + this.hashCode() + "]");
				if (responseBody == null)
					responseBody = "";
				
			}else{
				logger.error("<< http status Not Success [" + status + "], hs[" + this.hashCode() + "]," + getHttpInfoDumy(method));
				throw new HttpNetAgentException(HttpNetAgentException.HTTP_HAEDER_NOT_SUCCESS, "Http Status[" + status + "]");
			}
			
		} catch (HttpException e) {
			logger.error("<< HttpException msg [" + e.getMessage() + "], hs[" + this.hashCode() + "]," + getHttpInfoDumy(method), e);
			throw new HttpNetAgentException(HttpNetAgentException.HTTP_NET_ERROR, e.getMessage());
		} catch (IOException e) {
			logger.error("<< IOException msg [" + e.getMessage() + "], hs[" + this.hashCode() + "]," + getHttpInfoDumy(method), e);
			throw new HttpNetAgentException(HttpNetAgentException.HTTP_NET_ERROR, e.getMessage());
		}
		
		return responseBody;	
	}
	
	/**
	 * parameter와 queryString 를 가져온다.
	 * @param method
	 * @return
	 */
	private String getHttpInfoDumy(HttpMethod method){
		NameValuePair[] params = null;
		String methodType = "GET";
		String reqBody = null;
		if (method instanceof PostMethod){
			params = ((PostMethod)method).getParameters();
			methodType = "POST";
			StringRequestEntity sre = (StringRequestEntity)((PostMethod)method).getRequestEntity();
			reqBody = sre.getContent();
		}
		
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("#### getHttpInfoDumy ####");
			sb.append("\n## " + methodType + " [" + method.getURI() + "], hscd[" + this.hashCode() + "]");
		} catch (URIException e) {
			sb.append("\n## getParamsQueryStr- URIException " + e.getMessage() + "]");
			return sb.toString();
		}
		
		
		if (method.getQueryString() != null && method.getQueryString().length() > 0 )
			sb.append("\n"+ "## queryString[" + method.getQueryString() + "]");
		
		
		if (params != null){
			for(int i = 0; i < params.length; i++){
				NameValuePair param = params[i];
				sb.append("\n"+ "## POST body param[" + i + "], name[" + param.getName() + "], value[" + param.getValue() + "]");
			}
		}
		
		if (reqBody != null){
			sb.append("\n"+ "## POST body String [" + reqBody + "]");
		}
		
		
		sb.append("\n##########");
		
		return sb.toString();
	}
}
