package com.dhlee.http5.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

public class HttpClient5SSLContextFactory {
	private HttpClient5SSLContextFactory() {

	}

	public static SSLContext createMTLSContextFromFile(String type, String keyStorePath, String keyStorePassword,
			String trustStorePath, String trustStorePassword,
			String[] tlsVersions, String[] cipherSuites) throws SSLContextCreateException {
		try {
			KeyStore keyStore = KeyStore.getInstance(type);
			try (FileInputStream keyStoreInput = new FileInputStream(new File(keyStorePath))) {
				keyStore.load(keyStoreInput, keyStorePassword.toCharArray());
			}

			
			SSLContextBuilder sslContextBuilder = null;
			boolean withTrust = false; 
			if(withTrust) {
				KeyStore trustStore = KeyStore.getInstance(type);
				try (FileInputStream trustStoreInput = new FileInputStream(new File(trustStorePath))) {
					trustStore.load(trustStoreInput, trustStorePassword.toCharArray());
				}
				sslContextBuilder = SSLContextBuilder.create()
						.loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
						.loadTrustMaterial(trustStore, (TrustStrategy) null); // 신뢰할 모든 인증서
			}
			else {
		        TrustStrategy trustAllCertificates = new TrustStrategy() {
		            @Override
		            public boolean isTrusted(X509Certificate[] chain, String authType) {
		                // 모든 인증서를 신뢰함
		            	System.out.println("===> isTrusted " + authType );
		                return true;
		            }
		        };
		        // SSLContextBuilder를 사용하여 KeyMaterial을 로드하고, TrustMaterial로 모든 인증서 신뢰 설정
		        sslContextBuilder = SSLContextBuilder.create()
		                .loadKeyMaterial(keyStore, keyStorePassword.toCharArray()) // 클라이언트 키스토어 로드
		                .loadTrustMaterial(null, trustAllCertificates); // 모든 인증서를 신뢰하도록 설정
			}
			

            SSLContext sslContext = sslContextBuilder.build();

            // TLS 버전 및 Cipher Suite 설정
            SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
            if (tlsVersions != null) {
                sslParameters.setProtocols(tlsVersions);
            }
            if (cipherSuites != null) {
                sslParameters.setCipherSuites(cipherSuites);
            }

            return sslContext;
		} catch (Exception e) {
			String errorMessage = String.format("Failed to create SSLContext type(Algorithm): %s", type);
			throw new SSLContextCreateException(errorMessage, e);
		}
	}

	public static SSLContext createMTLSContextFromContent(String type, String keyStoreBase64Content,
			String keyStorePassword, String trustStoreBase64Content, String trustStorePassword,
			String[] tlsVersions, String[] cipherSuites)
			throws SSLContextCreateException {
		try {
			KeyStore keyStore = KeyStore.getInstance(type);
			byte[] keyBytes = Base64.getDecoder().decode(keyStoreBase64Content);
			try (ByteArrayInputStream bis = new ByteArrayInputStream(keyBytes)) {
				keyStore.load(bis, keyStorePassword.toCharArray());
			}

			KeyStore trustStore = KeyStore.getInstance(type);
			byte[] trustBytes = Base64.getDecoder().decode(trustStoreBase64Content);
			try (ByteArrayInputStream bis = new ByteArrayInputStream(trustBytes)) {
				trustStore.load(bis, trustStorePassword.toCharArray());
			}

			SSLContextBuilder sslContextBuilder = SSLContextBuilder.create()
					.loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
					.loadTrustMaterial(trustStore, (TrustStrategy) null); // 신뢰할 모든 인증서

            SSLContext sslContext = sslContextBuilder.build();

            // TLS 버전 및 Cipher Suite 설정
            SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
            if (tlsVersions != null) {
                sslParameters.setProtocols(tlsVersions);
            }
            if (cipherSuites != null) {
                sslParameters.setCipherSuites(cipherSuites);
            }

            return sslContext;
		} catch (Exception e) {
			String errorMessage = String.format("Failed to create SSLContext type(Algorithm): %s", type);
			throw new SSLContextCreateException(errorMessage, e);
		}
	}

	public static SSLContext createMTLSContextForJKS(String keyStorePath, String keyStorePassword,
			String trustStorePath, String trustStorePassword,
			String[] tlsVersions, String[] cipherSuites) throws SSLContextCreateException {
		return createMTLSContextFromFile("JKS", keyStorePath, keyStorePassword, trustStorePath, trustStorePassword
				,tlsVersions, cipherSuites);
	}

	public static SSLContext createMTLSContextForP12(String keyStorePath, String keyStorePassword,
			String trustStorePath, String trustStorePassword,
			String[] tlsVersions, String[] cipherSuites) throws SSLContextCreateException {
		return createMTLSContextFromFile("PKCS12", keyStorePath, keyStorePassword, trustStorePath, trustStorePassword
				,tlsVersions, cipherSuites);
	}
}
