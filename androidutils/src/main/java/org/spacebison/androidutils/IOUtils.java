package org.spacebison.androidutils;

import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOUtils {
    public static <T> T readObjectFromFile(Context context, String fileName) throws IOException {
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(context.openFileInput(fileName));
            //noinspection unchecked
            return (T) is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IOException("Could not read object from file " + fileName, e);
        } finally {
            closeQuietly(is);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    public static <T> void writeObjectToFile(Context context, String fileName, T object) throws IOException {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            os.writeObject(object);
        } catch (IOException e) {
            throw new IOException("Could not save object of type " + object.getClass().getName() + " to file " + fileName, e);
        } finally {
            closeQuietly(os);
        }
    }
}
