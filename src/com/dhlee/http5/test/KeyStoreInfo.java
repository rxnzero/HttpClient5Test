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
            // KeyStore ��ü ����
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            
            // Ű����� ���� �ε�
            try (FileInputStream fis = new FileInputStream(keyStorePath)) {
                keyStore.load(fis, keyStorePassword.toCharArray());
            }

            // Ű������� ��Ī (alias)���� ������
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                System.out.println("Alias: " + alias);

                // ������ ��������
                Certificate cert = keyStore.getCertificate(alias);
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) cert;
                    
                    // Subject ���� ���
                    System.out.println("Subject: " + x509Cert.getSubjectDN());

                    // Subject���� CN�� �����ؼ� ���
                    String subjectCN = getCommonName(x509Cert.getSubjectDN().getName());
                    System.out.println("Subject CN: " + subjectCN);
                    
                    // Fingerprint (SHA-256 �ؽ�) ���
                    System.out.println("SHA-256 Fingerprint: " + getFingerprint(x509Cert, "SHA-256"));
                    
                    System.out.println("Owner: " + x509Cert.getSubjectDN());
                    System.out.println("Issuer: " + x509Cert.getIssuerDN());
                    System.out.println("Serial Number: " + x509Cert.getSerialNumber());
                    System.out.println("Valid From: " + x509Cert.getNotBefore());
                    System.out.println("Valid Until: " + x509Cert.getNotAfter());
                    System.out.println("Signature Algorithm: " + x509Cert.getSigAlgName());
                    System.out.println("Certificate Type: " + cert.getType());
                }

                // ���� Ű ���� Ȯ�� (������ ���)
                if (keyStore.isKeyEntry(alias)) {
                    System.out.println("Private Key Exists for Alias: " + alias);
                }
                System.out.println("-----------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // CN ���� �Լ�
    public static String getCommonName(String subjectDN) {
        // subjectDN���� "CN=" �κ��� ã�� CN ���� ����
        String[] dnComponents = subjectDN.split(",");
        for (String component : dnComponents) {
            if (component.trim().startsWith("CN=")) {
                return component.trim().substring(3);  // "CN=" ������ ���� ��ȯ
            }
        }
        return "CN not found";  // CN�� ���� ���
    }
    
    // Fingerprint ��� �Լ�
    public static String getFingerprint(X509Certificate cert, String hashAlgorithm) {
        try {
            // �������� ����Ʈ �����͸� ������ �ؽ� ���
            byte[] encodedCert = cert.getEncoded();
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            byte[] digest = md.digest(encodedCert);

            // ����Ʈ �迭�� 16���� ���ڿ��� ��ȯ
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
        // JKS ���� ���� ���
        System.out.println("JKS KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.keystore", "changeit", "JKS");

        // P12 ���� ���� ���
        System.out.println("PKCS12 KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.p12", "changeit", "PKCS12");
    }
}
