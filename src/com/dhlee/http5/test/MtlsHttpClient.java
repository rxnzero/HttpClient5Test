package com.dhlee.http5.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Base64;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

public class MtlsHttpClient {

	public MtlsHttpClient() {
		
	}

	public static String loadStoreFileToString(String filePath) throws Exception {
		File storeFile = new File(filePath);
		byte[] contentBytes = new byte[(int) storeFile.length()];
		try (InputStream inputStream = new FileInputStream(storeFile)) {
			inputStream.read(contentBytes);
		}
		return Base64.getEncoder().encodeToString(contentBytes);
	}
	
	public static void main(String[] args) throws Exception {
		System.setProperty("use.test.trust", "y");
		
		String mtlsTestUrl = "https://localhost:8443/case/001/transactions"; //"https://localhost:8443";
		
		SSLContext sslContext = null;
		String[] tlsVersions = null;
//		{
//				"TLSv1.0"
//				, "TLSv1.1"
//				, "TLSv1.2"
//				, "TLSv1.3"
//			};
		String[] cipherSuites = null; 
//			{
//				"TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", 
//				"TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", 
//				"TLS_RSA_WITH_AES_256_CBC_SHA256", 
//				"TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384", 
//				"TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", 
//				"TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", 
//				"TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", 
//				"TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", 
//				"TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", 
//				"TLS_RSA_WITH_AES_256_CBC_SHA", 
//				"TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA", 
//				"TLS_ECDH_RSA_WITH_AES_256_CBC_SHA", 
//				"TLS_DHE_RSA_WITH_AES_256_CBC_SHA", 
//				"TLS_DHE_DSS_WITH_AES_256_CBC_SHA", 
//				"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", 
//				"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", 
//				"TLS_RSA_WITH_AES_128_CBC_SHA256", 
//				"TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", 
//				"TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", 
//				"TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", 
//				"TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", 
//				"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", 
//				"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", 
//				"TLS_RSA_WITH_AES_128_CBC_SHA", 
//				"TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA", 
//				"TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", 
//				"TLS_DHE_RSA_WITH_AES_128_CBC_SHA", 
//				"TLS_DHE_DSS_WITH_AES_128_CBC_SHA", 
//				"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", 
//				"TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", 
//				"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", 
//				"TLS_RSA_WITH_AES_256_GCM_SHA384", 
//				"TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", 
//				"TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", 
//				"TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", 
//				"TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", 
//				"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", 
//				"TLS_RSA_WITH_AES_128_GCM_SHA256", 
//				"TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", 
//				"TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", 
//				"TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", 
//				"TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", 
//				"TLS_EMPTY_RENEGOTIATION_INFO_SCSV"
//		};
		
		 String type = "P12";
		if("JSK".equals(type)) {
			// 클라이언트 인증서가 서버에 등록되지 않은 경우
			//		Exception in thread "main" javax.net.ssl.SSLHandshakeException: Received fatal alert: bad_certificate
	        
			// 클라이언트 인증서 및 키스토어 파일 경로 설정
			String keyStorePath = "d:/ssl-keystore/client.keystore";
	        
			String trustStorePath = "d:/ssl-keystore/client.truststore";
	        String keyStorePassword = "changeit";
	        String trustStorePassword = "changeit";
	        sslContext= HttpClient5SSLContextFactory.createMTLSContextForJKS(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword
	        		,tlsVersions, cipherSuites);
		}
		else if("P12".equals(type)) {
			 String keyStorePath = "d:/ssl-keystore/client.p12";
		        String keyStorePassword = "changeit";
		        String trustStorePath = "d:/ssl-keystore/truststore.p12";
		        String trustStorePassword = "changeit";
	        
		        sslContext= HttpClient5SSLContextFactory.createMTLSContextForP12(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword
		        		,tlsVersions, cipherSuites);
		    	
		}
        // SSLConnectionSocketFactory 생성
//        Exception in thread "main" javax.net.ssl.SSLPeerUnverifiedException: Certificate for <localhost> doesn't match common name of the certificate subject: elink
//    	at org.apache.hc.client5.http.ssl.DefaultHostnameVerifier.matchCN(DefaultHostnameVerifier.java:180)
//    	at org.apache.hc.client5.http.ssl.DefaultHostnameVerifier.verify(DefaultHostnameVerifier.java:118)
//    	at org.apache.hc.client5.http.ssl.TlsSessionValidator.verifySession(TlsSessionValidator.java:112)
//    	=> NoopHostnameVerifier.INSTANCE
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
        		sslContext
//        		,NoopHostnameVerifier.INSTANCE // Hostname verifier 비활성화
        		);

        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslSocketFactory).build();

        // HttpClient 빌드
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build()) {

            // HTTP GET 요청 실행
            HttpGet httpGet = new HttpGet(mtlsTestUrl);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println("Response Status: " + response.getCode());
                System.out.println("Response Body: " + EntityUtils.toString(response.getEntity()));
            }
        }
	}
}
