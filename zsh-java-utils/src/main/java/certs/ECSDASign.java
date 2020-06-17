package certs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECSDASign {

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static final int BLOCK_SIZE = 4*1024;

//    openssl pkcs8 -in key.pem -inform PEM -topk8 -nocrypt -out key-pkcs8.der -outform DER
//    openssl dgst -sha256 -verify zsh.pub.pem -signature aaa 8888694109B.vbf

    public static void main(String[] args) throws Exception {
        byte[] signature = signature("1122334455.txt", "private.der");
        boolean isValid = verify(signature, "1122334455.txt", "public.pem");
        System.out.println(isValid);
    }

    public static byte[] signature(String filePath, String priKeyPath) throws Exception {

        Signature ecsda = Signature.getInstance("SHA256withECDSA", "BC");
        PrivateKey priKey = getPemPrivateKey(priKeyPath);
        ecsda.initSign(priKey);

        File vbf = new File(filePath);
        int size = (int) vbf.length();
        int read = 0;
        byte buff[] = new byte[BLOCK_SIZE];

        InputStream is = new FileInputStream(vbf);
        while ( read < size ) {
            int actual = IOUtils.read(is, buff, 0, BLOCK_SIZE);
            ecsda.update(buff, 0, actual);
            read += actual;
        }
        is.close();

        byte[] signature = ecsda.sign();

        String hex = String.format("%064x", new BigInteger(1, signature));
        System.out.println(hex);

        return signature;
    }

    public static boolean verify(byte[] signature, String filePath, String pubKeyPath ) throws Exception {

        Signature ecsda = Signature.getInstance("SHA256withECDSA", "BC");
        PublicKey pubKey = getPemPublicKey(pubKeyPath);
        ecsda.initVerify(pubKey);

        File vbf = new File(filePath);
        int size = (int) vbf.length();
        int read = 0;
        byte buff[] = new byte[BLOCK_SIZE];

        InputStream is = new FileInputStream(vbf);
        while ( read < size ) {
            int actual = IOUtils.read(is, buff, 0, BLOCK_SIZE);
            ecsda.update(buff, 0, actual);
            read += BLOCK_SIZE;
        }
        is.close();

        return ecsda.verify(signature);
    }


    public static PrivateKey getPemPrivateKey(String priKeyPath) throws Exception {
//        String b64pk = FileUtils.readFileToString(new File(filename), "UTF-8");
//
//        b64pk = b64pk.replace("-----BEGIN EC PRIVATE KEY-----\n", "");
//        b64pk = b64pk.replace("-----END EC PRIVATE KEY-----", "");
//        Base64 b64 = new Base64();
//        byte [] decoded = b64.decode(b64pk);

//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
//        KeyFactory kf = KeyFactory.getInstance(algorithm);
//        return kf.generatePrivate(spec);

        byte[] pk = FileUtils.readFileToByteArray(new File(priKeyPath));
        PKCS8EncodedKeySpec priSpec = new PKCS8EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePrivate(priSpec);
    }


    public static PublicKey getPemPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        String b64pk = FileUtils.readFileToString(new File(filename), "UTF-8");
        b64pk = b64pk.replace("-----BEGIN PUBLIC KEY-----\n", "");
        b64pk = b64pk.replace("-----END PUBLIC KEY-----", "");

        byte[] pk = Base64.decode(b64pk);
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePublic(pubSpec);
    }

}
