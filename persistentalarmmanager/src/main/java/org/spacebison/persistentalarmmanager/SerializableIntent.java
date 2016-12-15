package org.spacebison.persistentalarmmanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wmatuszewski on 6/29/16.
 */
public class SerializableIntent implements Serializable {
    private String mAction;
    private String mType;
    private HashMap<String, Extra> mExtras;
    private Class<?> mClass;
    private Context mPackageContext;
    private int mFlags;
    private Uri mData;
    private ComponentName mComponent;
    private Set<String> mCategories = new HashSet<>();
    private String mScheme;

    public SerializableIntent(Context context, Intent intent) {
        mPackageContext = context;

    }

    public void addCategory(String category) {
        mCategories.add(category);
    }

    public void addFlags(int flags) {
        mFlags |= flags;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public boolean[] getBooleanArrayExtra(String name) {
        return (boolean[]) getExtra(name, ExtraType.BOOLEAN_ARRAY);
    }

    public boolean getBooleanExtra(String name, boolean defaultValue) {
        return (boolean) getExtra(name, ExtraType.BOOLEAN, defaultValue);
    }

    public byte[] getByteArrayExtra(String name) {
        return (byte[]) getExtra(name, ExtraType.BYTE_ARRAY);
    }

    public byte getByteExtra(String name, byte defaultValue) {
        return (byte) getExtra(name, ExtraType.BYTE, defaultValue);
    }

    public Set<String> getCategories() {
        return mCategories;
    }

    public char[] getCharArrayExtra(String name) {
        return (char[]) getExtra(name, ExtraType.CHAR_ARRAY);
    }

    public char getCharExtra(String name, char defaultValue) {
        return (char) getExtra(name, ExtraType.CHAR, defaultValue);
    }

    public CharSequence[] getCharSequenceArrayExtra(String name) {
        return (CharSequence[]) getExtra(name, ExtraType.CHAR_SEQUENCE_ARRAY);
    }

    public ArrayList<CharSequence> getCharSequenceArrayListExtra(String name) {
        return (ArrayList<CharSequence>) getExtra(name, ExtraType.CHAR_SEQUENCE_ARRAY_LIST);
    }

    public CharSequence getCharSequenceExtra(String name) {
        return (CharSequence) getExtra(name, ExtraType.CHAR_SEQUENCE);
    }

    public ComponentName getComponent() {
        return mComponent;
    }

    public void setComponent(ComponentName component) {
        mComponent = component;
    }

    public Uri getData() {
        return mData;
    }

    public void setData(Uri data) {
        mData = data;
    }

    public double[] getDoubleArrayExtra(String name) {
        return (double[]) getExtra(name, ExtraType.DOUBLE_ARRAY);
    }

    public double getDoubleExtra(String name, double defaultValue) {
        return (double) getExtra(name, ExtraType.DOUBLE, defaultValue);
    }

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    public float[] getFloatArrayExtra(String name) {
        return (float[]) getExtra(name, ExtraType.FLOAT_ARRAY);
    }

    public float getFloatExtra(String name, float defaultValue) {
        return (float) getExtra(name, ExtraType.FLOAT, defaultValue);
    }

    public int[] getIntArrayExtra(String name) {
        return (int[]) getExtra(name, ExtraType.INT_ARRAY);
    }

    public int getIntExtra(String name, int defaultValue) {
        return (int) getExtra(name, ExtraType.INT, defaultValue);
    }

    public ArrayList<Integer> getIntegerArrayListExtra(String name) {
        return (ArrayList<Integer>) getExtra(name, ExtraType.INT_ARRAY_LIST);
    }

    public long[] getLongArrayExtra(String name) {
        return (long[]) getExtra(name, ExtraType.LONG_ARRAY);
    }

    public long getLongExtra(String name, long defaultValue) {
        return (long) getExtra(name, ExtraType.LONG, defaultValue);
    }

    public String getScheme() {
        return mScheme;
    }

    public Serializable getSerializableExtra(String name) {
        return getExtra(name, ExtraType.SERIALIZABLE);
    }

    public short[] getShortArrayExtra(String name) {
        return (short[]) getExtra(name, ExtraType.SHORT_ARRAY);
    }

    public short getShortExtra(String name, short defaultValue) {
        return (short) getExtra(name, ExtraType.SHORT, defaultValue);
    }

    public String[] getStringArrayExtra(String name) {
        return (String[]) getExtra(name, ExtraType.STRING_ARRAY);
    }

    public ArrayList<String> getStringArrayListExtra(String name) {
        return (ArrayList<String>) getExtra(name, ExtraType.STRING_ARRAY_LIST);
    }

    public String getStringExtra(String name) {
        return (String) getExtra(name, ExtraType.STRING);
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public boolean hasCategory(String category) {
        return !mCategories.isEmpty();
    }

    public boolean hasExtra(String name) {
        return mExtras.containsKey(name);
    }

    public void putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        putExtra(name, value, ExtraType.CHAR_SEQUENCE_ARRAY_LIST);
    }

    public void putExtra(String name, Serializable value) {
        putExtra(name, value);
    }

    public void putExtra(String name, boolean[] value) {
        putExtra(name, value, ExtraType.BOOLEAN_ARRAY);
    }

    public void putExtra(String name, String value) {
        putExtra(name, value, ExtraType.STRING);
    }

    public void putExtra(String name, char value) {
        putExtra(name, value, ExtraType.CHAR);
    }

    public void putExtra(String name, CharSequence[] value) {
        putExtra(name, value, ExtraType.CHAR_SEQUENCE_ARRAY);
    }

    public void putExtra(String name, double[] value) {
        putExtra(name, value, ExtraType.DOUBLE_ARRAY);
    }

    public void putExtra(String name, char[] value) {
        putExtra(name, value, ExtraType.CHAR_ARRAY);
    }

    public void putExtra(String name, long[] value) {
        putExtra(name, value, ExtraType.LONG_ARRAY);
    }

    public void putExtra(String name, byte[] value) {
        putExtra(name, value, ExtraType.BYTE_ARRAY);
    }

    public void putExtra(String name, short[] value) {
        putExtra(name, value, ExtraType.SHORT_ARRAY);
    }

    public void putExtra(String name, float value) {
        putExtra(name, value, ExtraType.FLOAT);
    }

    public void putExtra(String name, long value) {
        putExtra(name, value, ExtraType.LONG);
    }

    public void putExtra(String name, int[] value) {
        putExtra(name, value, ExtraType.INT_ARRAY);
    }

    public void putExtra(String name, byte value) {
        putExtra(name, value, ExtraType.BYTE);
    }

    public void putExtra(String name, double value) {
        putExtra(name, value, ExtraType.DOUBLE);
    }

    public void putExtra(String name, int value) {
        putExtra(name, value, ExtraType.INT);
    }

    public void putExtra(String name, boolean value) {
        putExtra(name, value, ExtraType.BOOLEAN);
    }

    public void putExtra(String name, String[] value) {
        putExtra(name, value, ExtraType.STRING_ARRAY);
    }

    public void putExtra(String name, short value) {
        putExtra(name, value, ExtraType.SHORT);
    }

    public void putExtra(String name, float[] value) {
        putExtra(name, value, ExtraType.FLOAT_ARRAY);
    }

    public void putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        putExtra(name, value, ExtraType.INT_ARRAY_LIST);
    }

    public void putStringArrayListExtra(String name, ArrayList<String> value) {
        putExtra(name, value, ExtraType.STRING_ARRAY_LIST);
    }

    public void removeCategory(String category) {
        mCategories.remove(category);
    }

    public void removeExtra(String name) {
        mExtras.remove(name);
    }

    public void setClass(Context packageContext, Class<?> cls) {
        mPackageContext = packageContext;
        mClass = cls;
    }

    public void setDataAndType(Uri data, String type) {
        setData(data);
        setType(type);
    }

    private Serializable getExtra(String name, ExtraType type) {
        final Extra extra = mExtras.get(name);
        if (extra != null && extra.getType() == type) {
            return extra.getObject();
        } else {
            return null;
        }
    }

    private Serializable getExtra(String name, ExtraType type, Serializable defaultValue) {
        final Serializable extra = getExtra(name, type);

        if (extra != null) {
            return extra;
        } else {
            return defaultValue;
        }
    }

    private void putExtra(String name, Serializable value, ExtraType type) {
        mExtras.put(name, new Extra(value, type));
    }

    private enum ExtraType {
        STRING,
        STRING_ARRAY,
        INT,
        INT_ARRAY,
        SERIALIZABLE,
        SERIALIZABLE_ARRAY,
        DOUBLE,
        DOUBLE_ARRAY,
        BOOLEAN,
        BOOLEAN_ARRAY,
        LONG,
        LONG_ARRAY,
        CHAR,
        CHAR_SEQUENCE,
        CHAR_SEQUENCE_ARRAY,
        CHAR_SEQUENCE_ARRAY_EXTRA,
        FLOAT,
        FLOAT_ARRAY,
        BYTE,
        SHORT_ARRAY, STRING_ARRAY_LIST, CHAR_ARRAY, SHORT, CHAR_SEQUENCE_ARRAY_LIST, BYTE_ARRAY, INT_ARRAY_LIST,
    }

    private class Extra implements Serializable {
        private final Serializable mObject;
        private final ExtraType mType;

        private Extra(Serializable object, ExtraType type) {
            mObject = object;
            mType = type;
        }

        public Serializable getObject() {
            return mObject;
        }

        public ExtraType getType() {
            return mType;
        }
    }
}
