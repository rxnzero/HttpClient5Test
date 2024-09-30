package com.dhlee.http5.test;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class KeyStoreInfo {

    public static void printKeyStoreInfo(String keyStorePath, String keyStorePassword, String keyStoreType) {
        try {
            // KeyStore 객체 생성
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            
            // 키스토어 파일 로드
            try (FileInputStream fis = new FileInputStream(keyStorePath)) {
                keyStore.load(fis, keyStorePassword.toCharArray());
            }

            // 키스토어의 별칭 (alias)들을 가져옴
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                System.out.println("Alias: " + alias);

                // 인증서 가져오기
                Certificate cert = keyStore.getCertificate(alias);
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) cert;
                    
                    // Subject 정보 출력
                    System.out.println("Subject: " + x509Cert.getSubjectDN());

                    // Subject에서 CN만 추출해서 출력
                    String subjectCN = getCommonName(x509Cert.getSubjectDN().getName());
                    System.out.println("Subject CN: " + subjectCN);
                    
                    // Fingerprint (SHA-256 해시) 출력
                    System.out.println("SHA-256 Fingerprint: " + getFingerprint(x509Cert, "SHA-256"));
                    
                    System.out.println("Owner: " + x509Cert.getSubjectDN());
                    System.out.println("Issuer: " + x509Cert.getIssuerDN());
                    System.out.println("Serial Number: " + x509Cert.getSerialNumber());
                    System.out.println("Valid From: " + x509Cert.getNotBefore());
                    System.out.println("Valid Until: " + x509Cert.getNotAfter());
                    System.out.println("Signature Algorithm: " + x509Cert.getSigAlgName());
                    System.out.println("Certificate Type: " + cert.getType());
                }

                // 개인 키 정보 확인 (존재할 경우)
                if (keyStore.isKeyEntry(alias)) {
                    System.out.println("Private Key Exists for Alias: " + alias);
                }
                System.out.println("-----------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // CN 추출 함수
    public static String getCommonName(String subjectDN) {
        // subjectDN에서 "CN=" 부분을 찾아 CN 값을 추출
        String[] dnComponents = subjectDN.split(",");
        for (String component : dnComponents) {
            if (component.trim().startsWith("CN=")) {
                return component.trim().substring(3);  // "CN=" 이후의 값을 반환
            }
        }
        return "CN not found";  // CN이 없는 경우
    }
    
    // Fingerprint 계산 함수
    public static String getFingerprint(X509Certificate cert, String hashAlgorithm) {
        try {
            // 인증서의 바이트 데이터를 가져와 해시 계산
            byte[] encodedCert = cert.getEncoded();
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            byte[] digest = md.digest(encodedCert);

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder fingerprint = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                fingerprint.append(String.format("%02X", digest[i]));
                if (i < digest.length - 1) {
                    fingerprint.append(":");
                }
            }
            return fingerprint.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        // JKS 파일 정보 출력
        System.out.println("JKS KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.keystore", "changeit", "JKS");

        // P12 파일 정보 출력
        System.out.println("PKCS12 KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.p12", "changeit", "PKCS12");
    }
}
