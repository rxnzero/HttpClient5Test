package com.dhlee.http5.test;

import java.io.FileInputStream;
import java.security.KeyStore;
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

    public static void main(String[] args) {
        // JKS ���� ���� ���
        System.out.println("JKS KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.keystore", "changeit", "JKS");

        // P12 ���� ���� ���
        System.out.println("PKCS12 KeyStore Information:");
        printKeyStoreInfo("d:/ssl-keystore/client.p12", "changeit", "PKCS12");
    }
}
