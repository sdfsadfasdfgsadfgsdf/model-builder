package de.seven.fate.model.adapter;

import de.seven.fate.model.adapter.bool.BooleanRandomAdapter;
import de.seven.fate.model.adapter.date.DateRandomAdapter;
import de.seven.fate.model.adapter.decimal.BigDecimalRandomAdapter;
import de.seven.fate.model.adapter.integer.IntegerRandomAdapter;
import de.seven.fate.model.adapter.longv.LongRandomAdapter;
import de.seven.fate.model.adapter.string.StringRandomAdapter;
import de.seven.fate.model.util.ClassUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mario on 24.03.2016.
 */
public final class TypeRandomAdapterFactory {

    private static final Map<Class<?>, AbstractTypeRandomAdapter<?>> MAP = Collections.synchronizedMap(new HashMap<Class<?>, AbstractTypeRandomAdapter<?>>());

    private static final AbstractTypeRandomAdapter DEFAULT_ADAPTER = new DefaultTypeRandomAdapter();

    static {
        registerAdapter(new StringRandomAdapter());
        registerAdapter(new BooleanRandomAdapter());
        registerAdapter(new IntegerRandomAdapter());
        registerAdapter(new DateRandomAdapter());
        registerAdapter(new BigDecimalRandomAdapter());
        registerAdapter(new LongRandomAdapter());
    }

    private TypeRandomAdapterFactory() {
        throw new UnsupportedOperationException(getClass().getName() + " should not be called with new!");
    }

    public static void initPropertiesWithRandomValues(Object model) {
        assert model != null;

        List<String> propertyNames = ClassUtil.getPropertyNames(model.getClass());

        for (String propertyName : propertyNames) {
            setProperty(model, propertyName);
        }
    }

    public static void registerAdapter(AbstractTypeRandomAdapter<?> valueAdapter) {
        assert valueAdapter != null;

        synchronized (MAP) {
            MAP.put(valueAdapter.getValueType(), valueAdapter);
        }
    }

    public static void unregisterAdapter(Class<?> valueAdapterType) {
        assert valueAdapterType != null;

        synchronized (MAP) {
            MAP.remove(valueAdapterType);
        }
    }

    public static <T> AbstractTypeRandomAdapter<T> lookupRandomAdapter(Class<T> valueType) {
        assert valueType != null;

        return (AbstractTypeRandomAdapter<T>) MAP.get(valueType);
    }

    private static void setProperty(Object model, String propertyName) {
        assert model != null;
        assert propertyName != null;

        Class<?> modelType = model.getClass();

        Class<?> propertyType = ClassUtil.getPropertyType(propertyName, modelType);

        TypeRandomAdapter<?> typeRandomAdapter = getRandomPropertyValueAdapter(propertyType);

        Object propertyValue = typeRandomAdapter.randomValue(propertyName);

        if (propertyValue == null) {
            return;
        }

        try {
            BeanUtils.setProperty(model, propertyName, propertyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TypeRandomAdapter<?> getRandomPropertyValueAdapter(Class<?> propertyType) {
        assert propertyType != null;

        if (MAP.containsKey(propertyType)) {
            return MAP.get(propertyType);
        }

        return DEFAULT_ADAPTER;
    }
}
