package com.dhlee.http5.test;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class SimplePostExample {

	public static void main(String[] args) {
//		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
//		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
//		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
//		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		SimplePostExample sample = new SimplePostExample();
		sample.callPostService();
	}
	
	public void callPostService() {
        String url = "http://127.0.0.1:10210/HTT/TstInHttSysBank";
        String charset = "euc-kr";
        String requestBodyText = "0000BASICBANK0EAI20230714213113610EAISVR99BANK0001S1SAMSLTEST BANKSEND!ÇÑ±Û";
        try (
        	CloseableHttpClient httpClient = HttpClients.createDefault();
        ){
        	HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Content-Type", "text/plain; charset="+charset);
//            StringEntity requestEntity = new StringEntity(requestBodyText);
//        	httpPost.setEntity(requestEntity);
        	httpPost.setHeader("Content-Type", "application/octet-stream");
        	byte[] binaryData = requestBodyText.getBytes(charset);
            ByteArrayEntity binaryEntity = new ByteArrayEntity(binaryData, ContentType.APPLICATION_OCTET_STREAM);
            httpPost.setEntity(binaryEntity);
            
            System.out.println("Request Body: " + new String(binaryData, charset));
            
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
//            	String responseBody = EntityUtils.toString(responseEntity);
                byte[] responseBodyData = EntityUtils.toByteArray(responseEntity);
                String responseBody = new String(responseBodyData,charset); 
                
                System.out.println("Response Body: " + responseBody);
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
