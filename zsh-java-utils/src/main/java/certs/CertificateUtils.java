package certs;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

/**
 * <p>
 * ����ǩ��/���ܽ��ܹ��߰�
 * </p>
 *
 * @author IceWee
 * @version 1.0
 * @date 2012-4-26
 */
public class CertificateUtils {

    /**
     * Java��Կ��(Java ��Կ�⣬JKS)KEY_STORE
     */
    public static final String KEY_STORE = "JKS";

    public static final String X509 = "X.509";

    /**
     * �ļ���ȡ��������С
     */
    private static final int CACHE_SIZE = 2048;

    /**
     * ����ļ����ܿ�
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * ����ļ����ܿ�
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * <p>
     * ������Կ����˽Կ
     * </p>
     *
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String keyStorePath, String alias, String password)
            throws Exception {
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        return privateKey;
    }

    /**
     * <p>
     * �����Կ��
     * </p>
     *
     * @param keyStorePath ��Կ��洢·��
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath, String password)
            throws Exception {
        FileInputStream in = new FileInputStream(keyStorePath);
        KeyStore keyStore = KeyStore.getInstance(KEY_STORE);
        keyStore.load(in, password.toCharArray());
        in.close();
        return keyStore;
    }

    /**
     * <p>
     * ����֤���ù�Կ
     * </p>
     *
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(String certificatePath)
            throws Exception {
        Certificate certificate = getCertificate(certificatePath);
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey;
    }

    /**
     * <p>
     * ���֤��
     * </p>
     *
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String certificatePath)
            throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
        FileInputStream in = new FileInputStream(certificatePath);
        Certificate certificate = certificateFactory.generateCertificate(in);
        in.close();
        return certificate;
    }

    /**
     * <p>
     * ������Կ����֤��
     * </p>
     *
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String keyStorePath, String alias, String password)
            throws Exception {
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate;
    }

    /**
     * <p>
     * ˽Կ����
     * </p>
     *
     * @param data         Դ����
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password)
            throws Exception {
        // ȡ��˽Կ
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // �����ݷֶμ���
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * �ļ�˽Կ����
     * </p>
     * <p>
     * ������ļ����ܻᵼ���ڴ����
     * </>
     *
     * @param filePath     �ļ�·��
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    public static byte[] encryptFileByPrivateKey(String filePath, String keyStorePath, String alias, String password)
            throws Exception {
        byte[] data = fileToByte(filePath);
        return encryptByPrivateKey(data, keyStorePath, alias, password);
    }

    /**
     * <p>
     * �ļ�����
     * </p>
     *
     * @param srcFilePath  Դ�ļ�
     * @param destFilePath ���ܺ��ļ�
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @throws Exception
     */
    public static void encryptFileByPrivateKey(String srcFilePath, String destFilePath, String keyStorePath, String alias, String password)
            throws Exception {
        // ȡ��˽Կ
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        File srcFile = new File(srcFilePath);
        FileInputStream in = new FileInputStream(srcFile);
        File destFile = new File(destFilePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] data = new byte[MAX_ENCRYPT_BLOCK];
        byte[] encryptedData;    // ���ܿ�
        while (in.read(data) != -1) {
            encryptedData = cipher.doFinal(data);
            out.write(encryptedData, 0, encryptedData.length);
            out.flush();
        }
        out.close();
        in.close();
    }

    /**
     * <p>
     * �ļ����ܳ�BASE64������ַ���
     * </p>
     *
     * @param filePath     �ļ�·��
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    public static String encryptFileToBase64ByPrivateKey(String filePath, String keyStorePath, String alias, String password)
            throws Exception {
        byte[] encryptedData = encryptFileByPrivateKey(filePath, keyStorePath, alias, password);
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * <p>
     * ˽Կ����
     * </p>
     *
     * @param encryptedData �Ѽ�������
     * @param keyStorePath  ��Կ��洢·��
     * @param alias         ��Կ�����
     * @param password      ��Կ������
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String keyStorePath, String alias, String password)
            throws Exception {
        // ȡ��˽Կ
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // ����byte������󳤶�����: 128
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // �����ݷֶν���
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * ��Կ����
     * </p>
     *
     * @param data            Դ����
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
            throws Exception {
        // ȡ�ù�Կ
        PublicKey publicKey = getPublicKey(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // �����ݷֶμ���
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * ��Կ����
     * </p>
     *
     * @param encryptedData   �Ѽ�������
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String certificatePath)
            throws Exception {
        PublicKey publicKey = getPublicKey(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // �����ݷֶν���
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * �ļ�����
     * </p>
     *
     * @param srcFilePath     Դ�ļ�
     * @param destFilePath    Ŀ���ļ�
     * @param certificatePath ֤��洢·��
     * @throws Exception
     */
    public static void decryptFileByPublicKey(String srcFilePath, String destFilePath, String certificatePath)
            throws Exception {
        PublicKey publicKey = getPublicKey(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        File srcFile = new File(srcFilePath);
        FileInputStream in = new FileInputStream(srcFile);
        File destFile = new File(destFilePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] data = new byte[MAX_DECRYPT_BLOCK];
        byte[] decryptedData;    // ���ܿ�
        while (in.read(data) != -1) {
            decryptedData = cipher.doFinal(data);
            out.write(decryptedData, 0, decryptedData.length);
            out.flush();
        }
        out.close();
        in.close();
    }

    /**
     * <p>
     * ��������ǩ��
     * </p>
     *
     * @param data         Դ����
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] data, String keyStorePath, String alias, String password)
            throws Exception {
        // ���֤��
        X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias, password);
        // ��ȡ˽Կ
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        // ȡ��˽Կ
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        // ����ǩ��
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * <p>
     * ��������ǩ������BASE64����
     * </p>
     *
     * @param data         Դ����
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    public static String signToBase64(byte[] data, String keyStorePath, String alias, String password)
            throws Exception {
        return Base64.getEncoder().encodeToString(sign(data, keyStorePath, alias, password));
    }

    /**
     * <p>
     * �����ļ�����ǩ��(BASE64)
     * </p>
     * <p>
     * ��Ҫ�Ƚ��ļ�˽Կ���ܣ��ٸ��ݼ��ܺ����������ǩ��(BASE64)��������С�ļ�
     * </p>
     *
     * @param filePath     Դ�ļ�
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    public static String signFileToBase64WithEncrypt(String filePath, String keyStorePath, String alias, String password)
            throws Exception {
        byte[] encryptedData = encryptFileByPrivateKey(filePath, keyStorePath, alias, password);
        return signToBase64(encryptedData, keyStorePath, alias, password);
    }

    /**
     * <p>
     * �����ļ�ǩ��
     * </p>
     * <p>
     * ע�⣺<br>
     * ������ʹ����FileChannel����޴�Bug���ǲ����ͷ��ļ����������ǩ�����ļ��޷�����(�ƶ���ɾ����)<br>
     * �÷����ѱ�generateFileSignȡ��
     * </p>
     *
     * @param filePath     �ļ�·��
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     * @throws Exception
     */
    @Deprecated
    public static byte[] signFile(String filePath, String keyStorePath, String alias, String password)
            throws Exception {
        byte[] sign = new byte[0];
        // ���֤��
        X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias, password);
        // ��ȡ˽Կ
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        // ȡ��˽Կ
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        // ����ǩ��
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            FileChannel fileChannel = in.getChannel();
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            signature.update(byteBuffer);
            fileChannel.close();
            in.close();
            sign = signature.sign();
        }
        return sign;
    }

    /**
     * <p>
     * �����ļ�����ǩ��
     * </p>
     *
     * <p>
     * <b>ע�⣺</b><br>
     * ����ǩ��ʱupdate��byte�����С����֤ǩ��ʱ�Ĵ�СӦ��ͬ��������֤�޷�ͨ��
     * </p>
     *
     * @param filePath
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] generateFileSign(String filePath, String keyStorePath, String alias, String password)
            throws Exception {
        byte[] sign = new byte[0];
        // ���֤��
        X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias, password);
        // ��ȡ˽Կ
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        // ȡ��˽Կ
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        // ����ǩ��
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                signature.update(cache, 0, nRead);
            }
            in.close();
            sign = signature.sign();
        }
        return sign;
    }

    /**
     * <p>
     * �ļ�ǩ����BASE64�����ַ���
     * </p>
     *
     * @param filePath
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    public static String signFileToBase64(String filePath, String keyStorePath, String alias, String password)
            throws Exception {
        return Base64.getEncoder().encodeToString(generateFileSign(filePath, keyStorePath, alias, password));
    }

    /**
     * <p>
     * ��֤ǩ��
     * </p>
     *
     * @param data            �Ѽ�������
     * @param sign            ����ǩ��[BASE64]
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    public static boolean verifySign(byte[] data, String sign, String certificatePath)
            throws Exception {
        // ���֤��
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
        // ��ù�Կ
        PublicKey publicKey = x509Certificate.getPublicKey();
        // ����ǩ��
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * <p>
     * У���ļ�������
     * </p>
     * <p>
     * ����FileChannel���ڵľ޴�Bug���÷�����ͣ�ã���validateFileSignȡ��
     * </p>
     *
     * @param filePath        �ļ�·��
     * @param sign            ����ǩ��[BASE64]
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    @Deprecated
    public static boolean verifyFileSign(String filePath, String sign, String certificatePath)
            throws Exception {
        boolean result = false;
        // ���֤��
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
        // ��ù�Կ
        PublicKey publicKey = x509Certificate.getPublicKey();
        // ����ǩ��
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        File file = new File(filePath);
        if (file.exists()) {
            byte[] decodedSign = Base64.getDecoder().decode(sign);
            FileInputStream in = new FileInputStream(file);
            FileChannel fileChannel = in.getChannel();
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            signature.update(byteBuffer);
            in.close();
            result = signature.verify(decodedSign);
        }
        return result;
    }

    /**
     * <p>
     * У���ļ�ǩ��
     * </p>
     *
     * @param filePath
     * @param sign
     * @param certificatePath
     * @return
     * @throws Exception
     */
    public static boolean validateFileSign(String filePath, String sign, String certificatePath)
            throws Exception {
        boolean result = false;
        // ���֤��
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
        // ��ù�Կ
        PublicKey publicKey = x509Certificate.getPublicKey();
        // ����ǩ��
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        File file = new File(filePath);
        if (file.exists()) {
            byte[] decodedSign = Base64.getDecoder().decode(sign);
            FileInputStream in = new FileInputStream(file);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                signature.update(cache, 0, nRead);
            }
            in.close();
            result = signature.verify(decodedSign);
        }
        return result;
    }

    /**
     * <p>
     * BASE64����->ǩ��У��
     * </p>
     *
     * @param base64String    BASE64�����ַ���
     * @param sign            ����ǩ��[BASE64]
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    public static boolean verifyBase64Sign(String base64String, String sign, String certificatePath)
            throws Exception {
        byte[] data = Base64.getDecoder().decode(base64String);
        return verifySign(data, sign, certificatePath);
    }

    /**
     * <p>
     * BASE64����->��Կ����-ǩ��У��
     * </p>
     *
     * @param base64String    BASE64�����ַ���
     * @param sign            ����ǩ��[BASE64]
     * @param certificatePath ֤��洢·��
     * @return
     * @throws Exception
     */
    public static boolean verifyBase64SignWithDecrypt(String base64String, String sign, String certificatePath)
            throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(base64String);
        byte[] data = decryptByPublicKey(encryptedData, certificatePath);
        return verifySign(data, sign, certificatePath);
    }

    /**
     * <p>
     * �ļ���Կ����->ǩ��У��
     * </p>
     *
     * @param encryptedFilePath �����ļ�·��
     * @param sign              ����֤��[BASE64]
     * @param certificatePath
     * @return
     * @throws Exception
     */
    public static boolean verifyFileSignWithDecrypt(String encryptedFilePath, String sign, String certificatePath)
            throws Exception {
        byte[] encryptedData = fileToByte(encryptedFilePath);
        byte[] data = decryptByPublicKey(encryptedData, certificatePath);
        return verifySign(data, sign, certificatePath);
    }

    /**
     * <p>
     * У��֤�鵱ǰ�Ƿ���Ч
     * </p>
     *
     * @param certificate ֤��
     * @return
     */
    public static boolean verifyCertificate(Certificate certificate) {
        return verifyCertificate(new Date(), certificate);
    }

    /**
     * <p>
     * ��֤֤���Ƿ���ڻ���Ч
     * </p>
     *
     * @param date        ����
     * @param certificate ֤��
     * @return
     */
    public static boolean verifyCertificate(Date date, Certificate certificate) {
        boolean isValid = true;
        try {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            x509Certificate.checkValidity(date);
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * <p>
     * ��֤����֤�����ڸ����������Ƿ���Ч
     * </p>
     *
     * @param date            ����
     * @param certificatePath ֤��洢·��
     * @return
     */
    public static boolean verifyCertificate(Date date, String certificatePath) {
        Certificate certificate;
        try {
            certificate = getCertificate(certificatePath);
            return verifyCertificate(certificate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>
     * ��֤����֤�����ڸ����������Ƿ���Ч
     * </p>
     *
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     */
    public static boolean verifyCertificate(Date date, String keyStorePath, String alias, String password) {
        Certificate certificate;
        try {
            certificate = getCertificate(keyStorePath, alias, password);
            return verifyCertificate(certificate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>
     * ��֤����֤�鵱ǰ�Ƿ���Ч
     * </p>
     *
     * @param keyStorePath ��Կ��洢·��
     * @param alias        ��Կ�����
     * @param password     ��Կ������
     * @return
     */
    public static boolean verifyCertificate(String keyStorePath, String alias, String password) {
        return verifyCertificate(new Date(), keyStorePath, alias, password);
    }

    /**
     * <p>
     * ��֤����֤�鵱ǰ�Ƿ���Ч
     * </p>
     *
     * @param certificatePath ֤��洢·��
     * @return
     */
    public static boolean verifyCertificate(String certificatePath) {
        return verifyCertificate(new Date(), certificatePath);
    }

    /**
     * <p>
     * �ļ�ת��Ϊbyte����
     * </p>
     *
     * @param filePath �ļ�·��
     * @return
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
        }
        return data;
    }

    /**
     * <p>
     * ����������д�ļ�
     * </p>
     *
     * @param bytes    ����������
     * @param filePath �ļ�����Ŀ¼
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }

}