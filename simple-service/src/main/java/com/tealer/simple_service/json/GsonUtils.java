package com.tealer.simple_service.json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tealer.simple_service.json.factory.NullArrayTypeAdapterFactory;
import com.tealer.simple_service.json.factory.NullCollectionTypeAdapterFactory;
import com.tealer.simple_service.json.factory.NullMultiDateAdapterFactory;
import com.tealer.simple_service.json.factory.NullNumberAdapterFactory;
import com.tealer.simple_service.json.factory.NullStringAdapterFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 包含操作 {@code JSON} 数据的常用方法的工具类。
 * <p />
 * 该工具类使用的 {@code JSON} 转换引擎是 <a href="http://code.google.com/p/google-gson/"
 * mce_href="http://code.google.com/p/google-gson/" target="_blank"> {@code
 * Google Gson}</a>。
 *
 */
public class GsonUtils {
    private static Gson gson;

    private GsonUtils() {
        throw new AssertionError("no instance");
    }


    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    // json宽松
                    .setLenient()
                    //支持Map的key为复杂对象的形式
                    .enableComplexMapKeySerialization()
                    .registerTypeAdapterFactory(new NullStringAdapterFactory())
                    .registerTypeAdapterFactory(new NullNumberAdapterFactory())
                    //序列化日期格式化输出
                    .registerTypeAdapterFactory(new NullMultiDateAdapterFactory("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"))
                    .registerTypeAdapterFactory(new NullArrayTypeAdapterFactory())
                    .registerTypeAdapterFactory(new NullCollectionTypeAdapterFactory())
                    //默认是Gson把HTML转义的
                    .disableHtmlEscaping()
                    .create();
        }
        return gson;
    }

    /**
     * 根据jsonstr，获取JsonObject对象
     * @param jsonStr
     * @return
     */
    public static JsonObject getJsonObject(String jsonStr){
        return fromJson(jsonStr,JsonObject.class);
    }

    /**
     * 根据jsonstr，获取getJsonArray对象
     * @param jsonStr
     * @return
     */
    public static JsonArray getJsonArray(String jsonStr){
        return fromJson(jsonStr,JsonArray.class);
    }

    /**
     * 根据key,获取JsonObject里面的字符串值
     * @param key
     * @param jsonObject
     * @return 返回String类型数值,默认值或者当数据为null 返回null
     */
    public static String getString(String key, JsonObject jsonObject){
        return getString(key,jsonObject,null);
    }


    /**
     * 根据key,获取JsonObject里面的字符串值
     * @param key
     * @param jsonObject
     * @param defaultValue
     * @return 返回String类型数值,默认值或者当数据为null 返回@param defaultValue
     */
    public static String getString(String key, JsonObject jsonObject,String defaultValue){
        if (key == null || jsonObject == null) return defaultValue;
        String source = null;
        if (jsonObject.has(key)) {
            source = toJson(jsonObject.get(key),defaultValue);
        }
        return source!=null?source:defaultValue;
    }


    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     *
     * @param target
     *            要转换成 {@code JSON} 的目标对象。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target) {
        return getGson().toJson(target);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     *
     * @param target      要转换成 {@code JSON} 的目标对象。
     * @param typeOfSrc
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target,Type typeOfSrc) {
        return getGson().toJson(target,typeOfSrc);
    }

    /**
     * 将给定的JsonElement对象转换成 {@code JSON} 格式的字符串
     * @param jsonElement
     * @param defaultValue
     * @return
     */
    public static String toJson(JsonElement jsonElement,String defaultValue) {
        if (jsonElement == null) return defaultValue;
        String source = null;
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            if(jsonElement.isJsonPrimitive()){
                source = jsonElement.getAsString();
            }else {
                source = getGson().toJson(jsonElement);
            }
        }
        return source!=null?source:defaultValue;
    }


    /**
     * 将给定的JsonElement对象转换成 {@code JSON} 格式的字符串
     * @param jsonElement
     * @return
     */
    public static String toJson(JsonElement jsonElement) {
        return toJson(jsonElement, (String) null);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     *
     * @param <T>
     *            要转换的目标类型。
     * @param json
     *            给定的 {@code JSON} 字符串。
     * @param clazz
     *            要转换的目标类。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return getGson().fromJson(json, clazz);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     *
     * @param <T>
     *            要转换的目标类型。
     * @param json
     *            给定的 {@code JSON} 字符串。
     * @param type
     *            {@code java.lang.reflect.Type} 的类型指示类对象。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Type type) {
        return getGson().fromJson(json, type);
    }


    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     *
     * @param <T>
     *            要转换的目标类型。
     * @param json
     *            给定的 {@code JSON} 字符串。
     * @param token
     *            {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        return getGson().fromJson(json, token.getType());
    }



    /**
     * 将给定的 {@code JSON} 字符串转换成Map<String, V>的类型对象。
     * @param json map的序列化结果
     * @param <V>  v类型
     * @return Map<String,V>                                                                                                                              ,                                                                                                                               V>
     */
    public static <V> Map<String, V> fromJsonToMap(String json, Class<V> vClazz) {
        return fromJsonToMap(json,String.class, vClazz);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成Map<K, V>的类型对象。
     * @param json map的序列化结果
     * @param <K>  k类型
     * @param <V>  v类型
     * @return Map<K,V>                                                                                                                             ,                                                                                                                               V>
     */
    public static <K,V> Map<K, V> fromJsonToMap(String json,Class<K> kClazz, Class<V> vClazz) {
        return getGson().fromJson(json, TypeToken.getParameterized(Map.class, kClazz, vClazz).getType());
    }


    /**
     * 将给定的 {@code JSON} 字符串转换成List<T>的类型对象。
     *
     * @param json    给定的 {@code JSON} 字符串。
     * @param clazz
     * @return List<T> 给定的 {@code JSON} 字符串表示的指定的类型对象。
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
       List<T> ts = new ArrayList<>();
        try {
            ts = fromJson(json, new ListOfJson<>(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }


    private static class ListOfJson<T> implements ParameterizedType {
        private Class<?> wrapped;

        public ListOfJson(Class<T> wrapper) {
            this.wrapped = wrapper;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
