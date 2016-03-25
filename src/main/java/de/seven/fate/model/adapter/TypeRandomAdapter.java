package de.seven.fate.model.adapter;

public interface TypeRandomAdapter<T> {

    /**
     * @param propertyName
     * @return @return random value for given propertyName
     */
    T randomValue(String propertyName);

    /**
     * @return Type of property
     */
    Class<?> getValueType();
}