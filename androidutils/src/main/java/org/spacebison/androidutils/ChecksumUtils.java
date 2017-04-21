package org.spacebison.androidutils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cmb on 11.12.16.
 */

public class ChecksumUtils {
    public static String getDigestString(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


    public static byte[] getMd5Sum(File file) throws NoSuchAlgorithmException, FileNotFoundException {
        return getDigest(new FileInputStream(file), "MD5");
    }

    public static byte[] getMd5Sum(Context context, Uri uri) throws FileNotFoundException, NoSuchAlgorithmException {
        return getDigest(context.getContentResolver().openInputStream(uri), "MD5");
    }

    public static byte[] getDigest(InputStream inputStream, String algorithm) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(algorithm);
        DigestInputStream dis = null;
        try {
            byte[] buf = new byte[8 * 1024];
            dis = new DigestInputStream(inputStream, md);
			//noinspection StatementWithEmptyBody
            while (dis.read(buf) >= 0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(dis);
        }

        return md.digest();
    }
}
