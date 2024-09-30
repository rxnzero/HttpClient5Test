package com.dhlee.http5.test;

import java.io.FileInputStream;
import java.security.KeyStore;
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

    public static void main(String[] args) {
        // JKS 파일 정보 출력
        System.out.println("JKS KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.keystore", "changeit", "JKS");

        // P12 파일 정보 출력
        System.out.println("PKCS12 KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.p12", "changeit", "PKCS12");
    }
}
