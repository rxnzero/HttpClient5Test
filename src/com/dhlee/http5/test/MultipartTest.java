package com.dhlee.http5.test;

import java.io.File;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class MultipartTest {

	public MultipartTest() {
		// TODO Auto-generated constructor stub
	}

	public static void sendMultipart() {
		String url = "http://localhost:8080/TestWeb/multipart";
		String json = "{\"loanKey\":\"123456\", \"bankAccountNumber\":\"123456789012\"}";
		String filePath = "d:/user.jpg.txt";
		final File file = new File(filePath);
		final FileBody fileBody = new FileBody(file, ContentType.DEFAULT_TEXT);
		final StringBody jsonBody = new StringBody(json, ContentType.APPLICATION_JSON);
		final StringBody stringBody = new StringBody("This is message", ContentType.MULTIPART_FORM_DATA);

		final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.LEGACY);
		builder.addPart("image-file", fileBody);
		builder.addPart("json-body", jsonBody);
		builder.addPart("text2", stringBody);
		final HttpEntity entity = builder.build();

		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try (
			CloseableHttpResponse postResponse = httpClient.execute(post);
			){
			System.out.println("getCode = " + postResponse.getCode());
			HttpEntity postResponseEntity = postResponse.getEntity();
			if (postResponseEntity != null) {
				System.out.println(EntityUtils.toString(postResponseEntity));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		sendMultipart();
	}

}
