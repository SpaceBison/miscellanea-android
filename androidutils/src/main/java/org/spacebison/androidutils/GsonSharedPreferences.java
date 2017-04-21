package org.spacebison.androidutils;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cmb on 09.12.16.
 */

public class GsonSharedPreferences implements SharedPreferences {
    private final Gson mGson;
    private final SharedPreferences mDelegate;

    public GsonSharedPreferences(SharedPreferences delegate) {
        this(delegate, new Gson());
    }

    public GsonSharedPreferences(SharedPreferences delegate, Gson gson) {
        mGson = gson;
        mDelegate = delegate;
    }

    @Override
    public boolean contains(String key) {
        return mDelegate.contains(key);
    }

    @Override
    public Editor edit() {
        return new Editor(mDelegate.edit());
    }

    @Override
    public Map<String, ?> getAll() {
        return mDelegate.getAll();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mDelegate.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return mDelegate.getFloat(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return mDelegate.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mDelegate.getLong(key, defValue);
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return mDelegate.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return mDelegate.getStringSet(key, defValues);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mDelegate.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mDelegate.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public <T> T getObject(String key, T defValue, Class<T> classOfValue) {
        return mGson.fromJson(mDelegate.getString(key, mGson.toJson(defValue)), classOfValue);
    }

    public <T> Set<T> getSet(String key, Set<T> defValue, Class<T> classOfValue) {
        final Set<String> stringSet = mDelegate.getStringSet(key, null);

        if (stringSet == null) {
            if (defValue != null) {
                return new HashSet<>(defValue);
            } else {
                return null;
            }
        }

        HashSet<T> set = new HashSet<>();

        for (String s : stringSet) {
            set.add(mGson.fromJson(s, classOfValue));
        }

        return set;
    }

    public static class Editor implements SharedPreferences.Editor {
        private final Gson mGson;
        private final SharedPreferences.Editor mDelegate;

        public Editor(SharedPreferences.Editor delegate) {
            this(delegate, new Gson());
        }

        public Editor(SharedPreferences.Editor delegate, Gson gson) {
            mGson = gson;
            mDelegate = delegate;
        }

        @Override
        public void apply() {
            mDelegate.apply();
        }

        @Override
        public Editor clear() {
            mDelegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return mDelegate.commit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            mDelegate.putBoolean(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            mDelegate.putFloat(key, value);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            mDelegate.putInt(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            mDelegate.putLong(key, value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            mDelegate.putString(key, value);
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            mDelegate.putStringSet(key, values);
            return this;
        }

        @Override
        public Editor remove(String key) {
            mDelegate.remove(key);
            return this;
        }

        public Editor putObject(String key, Object object) {
            mDelegate.putString(key, mGson.toJson(object));
            return this;
        }

        public Editor putSet(String key, Set<Object> objectSet) {
            HashSet<String> jsonSet = new HashSet<>();

            for (Object o : objectSet) {
                jsonSet.add(mGson.toJson(o));
            }

            mDelegate.putStringSet(key, jsonSet);
            return this;
        }
    }
}
