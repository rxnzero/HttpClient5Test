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
						.loadTrustMaterial(trustStore, (TrustStrategy) null); // �ŷ��� ��� ������
			}
			else {
		        TrustStrategy trustAllCertificates = new TrustStrategy() {
		            @Override
		            public boolean isTrusted(X509Certificate[] chain, String authType) {
		                // ��� �������� �ŷ���
		            	System.out.println("===> isTrusted " + authType );
		                return true;
		            }
		        };
		        // SSLContextBuilder�� ����Ͽ� KeyMaterial�� �ε��ϰ�, TrustMaterial�� ��� ������ �ŷ� ����
		        sslContextBuilder = SSLContextBuilder.create()
		                .loadKeyMaterial(keyStore, keyStorePassword.toCharArray()) // Ŭ���̾�Ʈ Ű����� �ε�
		                .loadTrustMaterial(null, trustAllCertificates); // ��� �������� �ŷ��ϵ��� ����
			}
			

            SSLContext sslContext = sslContextBuilder.build();

            // TLS ���� �� Cipher Suite ����
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
					.loadTrustMaterial(trustStore, (TrustStrategy) null); // �ŷ��� ��� ������

            SSLContext sslContext = sslContextBuilder.build();

            // TLS ���� �� Cipher Suite ����
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
