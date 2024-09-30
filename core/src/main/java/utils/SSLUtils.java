package utils;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SSLUtils {

    public static SSLContext createServerSSLContext() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // Generate key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Generate Self-Signed Certificate
        X509Certificate certificate = generateSelfSignedCertificate(keyPair);

        // Create KeyStore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null); // Initialize empty keystore
        keyStore.setKeyEntry("server", keyPair.getPrivate(), "password".toCharArray(), new X509Certificate[]{certificate});

        // Initialize KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, "password".toCharArray());

        // Initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        return sslContext;
    }

    public static SSLContext createClientSSLContext() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { /* No-op */ }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { /* No-op */ }
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        return sslContext;
    }

    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        // Use new X509v3CertificateBuilder API for generating certificates
        X500Name issuer = new X500Name("CN=Server");
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date(System.currentTimeMillis() - 10000);
        Date notAfter = new Date(System.currentTimeMillis() + 365L * 86400000L); // 1 year
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic()
        );

        // Optionally, add extensions like KeyUsage, etc.
        certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature));

        // Create the content signer using SHA256 with RSA
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.getPrivate());

        // Build the certificate
        X509CertificateHolder certHolder = certBuilder.build(signer);

        // Convert it to X509Certificate object
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
    }
}
