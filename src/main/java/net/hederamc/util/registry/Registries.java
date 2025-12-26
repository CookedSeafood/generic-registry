package net.hederamc.util.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper class containing a map maps the type of registered object to a
 * {@linkplain Registry registry} of the type. A registries enforce the same type parameter
 * on both generics in a pair.
 *
 * <h4>Register to Registry</h4>
 * <ul>
 * <li>{@link #register(Identifier, Object)}</li>
 * </ul>
 * <h4>Get from Registry</h4>
 * <ul>
 * <li>{@link #get(Class, Identifier)}</li>
 * </ul>
 * <h4>Unregister from Registry</h4>
 * <ul>
 * <li>{@link #unregister(Class, Identifier)}</li>
 * <li>{@link #unregister(Identifier, Object)}</li>
 * </ul>
 */
public final class Registries {
    private static final Map<Class<?>, Registry<?>> registries;

    private Registries() {
    }

    /**
     * Register an object. Registered object is mapped with type, then id.
     *
     * @param <T> <b>T</b> object type as key
     * @param id as key
     * @param object as value
     * @return the registered object
     */
    @SuppressWarnings("unchecked")
    public static <T> T register(Identifier id, T object) {
        getOrPut((Class<T>)object.getClass()).put(id, object);
        return object;
    }

    /**
     * Get an object mapped with given type key and id key.
     *
     * @param <T> <b>T</b>
     * @param type key
     * @param id key
     * @return the mapped object
     */
    @Nullable
    public static <T> T get(Class<T> type, Identifier id) {
        Registry<T> registry;
        return (registry = get(type)) == null ? null : registry.get(id);
    }

    /**
     * Unregister a registry mapped with given type key.
     *
     * @param <T> <b>T</b>
     * @param type key
     * @return
     */
    public static <T> Registry<T> unregister(Class<T> type) {
        return remove(type);
    }

    /**
     * Unregister an object mapped with given type key and id key.
     *
     * @param <T> <b>T</b>
     * @param type key
     * @param id key
     * @return the mapped object
     */
    @Nullable
    public static <T> T unregister(Class<T> type, Identifier id) {
        Registry<T> registry;
        T object = (registry = get(type)) == null ? null : registry.remove(id);
        removeIfEmpty(type);
        return object;
    }

    /**
     * Unregister an object mapped with given id key and object value.
     *
     * @param <T> <b>T</b>
     * @param id key
     * @param object value
     * @return {@code true} if successful unregistered
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean unregister(Identifier id, T object) {
        Class<T> type = (Class<T>)object.getClass();
        Registry<T> registry;
        boolean b = (registry = get(type)) == null ? false : registry.remove(id, object);
        removeIfEmpty(type);
        return b;
    }

    public static boolean isRegistered(Class<?> type, Identifier id) {
        Registry<?> registry;
        return (registry = get(type)) != null && registry.isRegistered(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isRegistered(T object) {
        Class<T> type = (Class<T>)object.getClass();
        Registry<T> registry;
        return (registry = get(type)) != null && registry.isRegistered(object);
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isRegistered(Identifier id, T object) {
        Class<T> type = (Class<T>)object.getClass();
        Registry<T> registry;
        return (registry = get(type)) != null && registry.isRegistered(id, object);
    }

    public static <T> Registry<T> getOrPut(Class<T> type, Registry<T> registry) {
        Registry<T> presented = get(type);
        if (presented != null) {
            return presented;
        }

        put(type, registry);
        return registry;
    }

    public static <T> Registry<T> getOrPut(Class<T> type) {
        return getOrPut(type, new Registry<>());
    }

    @Nullable
    public static <T> Registry<T> removeIfEmpty(Class<T> type) {
        Registry<T> registry;
        return (registry = get(type)) != null && registry.isEmpty() ? remove(type) : null;
    }

    public static Map<Class<?>, Registry<?>> getRegistries() {
        return registries;
    }

    public static int size() {
        return registries.size();
    }

    public static boolean isEmpty() {
        return registries.isEmpty();
    }

    public static boolean containsKey(Class<?> type) {
        return registries.containsKey(type);
    }

    public static boolean containsValue(Registry<?> registry) {
        return registries.containsValue(registry);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> get(Class<T> type) {
        return (Registry<T>)registries.get(type);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> put(Class<T> type, Registry<T> registry) {
        return (Registry<T>)registries.put(type, registry);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> remove(Class<T> type) {
        return (Registry<T>)registries.remove(type);
    }

    public static void putAll(Map<Class<?>, Registry<?>> m) {
        registries.putAll(m);
    }

    public static void clear() {
        registries.clear();
    }

    public static Set<Class<?>> keySet() {
        return registries.keySet();
    }

    public static Collection<Registry<?>> values() {
        return registries.values();
    }

    public static Set<Map.Entry<Class<?>, Registry<?>>> entrySet() {
        return registries.entrySet();
    }

    @SuppressWarnings("unchecked")
    public static <T> Registry<T> getOrDefault(Class<T> type, Registry<T> defaultValue) {
        return (Registry<T>)registries.getOrDefault(type, defaultValue);
    }

    public static void forEach(BiConsumer<Class<?>, Registry<?>> action) {
        registries.forEach(action);
    }

    public static void replaceAll(BiFunction<Class<?>, Registry<?>, Registry<?>> function) {
        registries.replaceAll(function);
    }

    @SuppressWarnings("unchecked")
    public static <T> Registry<T> putIfAbsent(Class<T> type, Registry<T> registry) {
        return (Registry<T>)registries.putIfAbsent(type, registry);
    }

    public static <T> boolean remove(Class<T> type, Registry<T> registry) {
        return registries.remove(type, registry);
    }

    public static <T> boolean replace(Class<T> type, Registry<T> oldRegistries, Registry<T> newRegistries) {
        return registries.replace(type, oldRegistries, newRegistries);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> replace(Class<T> type, Registry<T> registry) {
        return (Registry<T>)registries.replace(type, registry);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> computeIfAbsent(Class<T> type,
            Function<Class<?>, Registry<T>> mappingFunction) {
        return (Registry<T>)registries.computeIfAbsent(type, mappingFunction);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> computeIfPresent(Class<T> type,
            BiFunction<Class<?>, Registry<?>, Registry<T>> remappingFunction) {
        return (Registry<T>)registries.computeIfPresent(type, remappingFunction);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> compute(Class<T> type,
            BiFunction<Class<?>, Registry<?>, Registry<T>> remappingFunction) {
        return (Registry<T>)registries.compute(type, remappingFunction);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> merge(Class<T> type, Registry<T> registry,
            BiFunction<Registry<?>, Registry<?>, Registry<T>> remappingFunction) {
        return (Registry<T>)registries.merge(type, registry, remappingFunction);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getType(Registry<T> registry) {
        for (Entry<Class<?>, Registry<?>> entry : entrySet()) {
            if (entry.getValue() == registry) {
                return (Class<T>)entry.getKey();
            }
        };

        return null;
    }

    public static String toTypesString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for(Class<?> type : keySet()) {
            joiner.add(type.getName());
        }

        return joiner.toString();
    }

    static {
        registries = new HashMap<>();
    }
}
