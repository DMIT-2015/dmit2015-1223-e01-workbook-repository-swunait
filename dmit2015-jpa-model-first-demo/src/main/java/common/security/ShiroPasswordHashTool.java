package common.security;

import org.apache.commons.cli.Option;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.SimpleHashRequest;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HashFormatFactory;
import org.apache.shiro.crypto.hash.format.Shiro2CryptFormat;
import org.apache.shiro.lang.codec.Base64;
import org.apache.shiro.lang.codec.Hex;
import org.apache.shiro.lang.util.ByteSource;

import static java.util.Collections.emptyMap;

/**
 * Helper class with a method for hashing a password for Shiro.
 * Code is taken from <a href="https://github.com/apache/shiro/blob/main/tools/hasher/src/main/java/org/apache/shiro/tools/hasher/Hasher.java">HasherTool.java</a>
 * and modified to support only hashing of password.
 *
 */
public class ShiroPasswordHashTool {

    private static final String HEX_PREFIX = "0x";
    private static final String DEFAULT_PASSWORD_ALGORITHM_NAME = DefaultPasswordService.DEFAULT_HASH_ALGORITHM;
    private static final int DEFAULT_GENERATED_SALT_SIZE = 128;

    private static final Option SALT = new Option("s", "salt", true, "use the specified salt.  <arg> is plaintext.");
    private static final Option SALT_BYTES = new Option("sb", "saltbytes", true, "use the specified salt bytes.  <arg> is hex or base64 encoded text.");
    private static final String SALT_MUTEX_MSG = createMutexMessage(SALT, SALT_BYTES);
    private static final HashFormatFactory HASH_FORMAT_FACTORY = new DefaultHashFormatFactory();

    private static String createMutexMessage(Option... options) {
        StringBuilder sb = new StringBuilder();
        sb.append("The ");

        for (int i = 0; i < options.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Option o = options[0];
            sb.append("-").append(o.getOpt()).append("/--").append(o.getLongOpt());
        }
        sb.append(" and generated salt options are mutually exclusive.  Only one of them may be used at a time");
        return sb.toString();
    }
    private static ByteSource getSalt(String saltString, String saltBytesString, boolean generateSalt, int generatedSaltSize) {

        if (saltString != null) {
            if (generateSalt || (saltBytesString != null)) {
                throw new IllegalArgumentException(SALT_MUTEX_MSG);
            }
            return ByteSource.Util.bytes(saltString);
        }

        if (saltBytesString != null) {
            if (generateSalt) {
                throw new IllegalArgumentException(SALT_MUTEX_MSG);
            }

            String value = saltBytesString;
            boolean base64 = true;
            if (saltBytesString.startsWith(HEX_PREFIX)) {
                //hex:
                base64 = false;
                value = value.substring(HEX_PREFIX.length());
            }
            byte[] bytes;
            if (base64) {
                bytes = Base64.decode(value);
            } else {
                bytes = Hex.decode(value);
            }
            return ByteSource.Util.bytes(bytes);
        }

        if (generateSalt) {
            SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
            int byteSize = generatedSaltSize / 8; //generatedSaltSize is in *bits* - convert to byte size:
            return generator.nextBytes(byteSize);
        }

        //no salt used:
        return null;
    }

    public static String hashPassword(String plainTextPassword) {
        boolean generateSalt = true;
        int generatedSaltSize = DEFAULT_GENERATED_SALT_SIZE;
        String algorithm = null; //user unspecified
        String formatString = null;
        algorithm = DEFAULT_PASSWORD_ALGORITHM_NAME;

        ByteSource publicSalt = getSalt(null, null, generateSalt, generatedSaltSize);
        HashRequest hashRequest = new SimpleHashRequest(algorithm, ByteSource.Util.bytes(plainTextPassword), publicSalt, emptyMap());
        DefaultHashService hashService = new DefaultHashService();
        Hash hash = hashService.computeHash(hashRequest);
        formatString = Shiro2CryptFormat.class.getName();
        HashFormat format = HASH_FORMAT_FACTORY.getInstance(formatString);
        return format.format(hash);
    }

}