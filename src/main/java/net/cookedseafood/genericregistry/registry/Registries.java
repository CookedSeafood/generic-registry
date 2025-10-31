package net.cookedseafood.genericregistry.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * A registries holds the mapping between the class type of registered object and
 * {@linkplain Registry registry}.
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
public abstract class Registries {
    private static final Map<Class<?>, Registry<?>> registries;

    /**
     * Register an object to registry.
     * 
     * @param <T> the type of the object to register
     * @param id the key to map with
     * @param object the object to register
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public static <T> T register(Identifier id, T object) {
        getOrPut((Class<T>)object.getClass()).put(id, object);
        return object;
    }

    /**
     * Get an object from registry.
     * 
     * @param <T> the type of the object to get
     * @param type the type of the object to get
     * @param id the key mapped with
     * @return the object
     */
    @Nullable
    public static <T> T get(Class<T> type, Identifier id) {
        Registry<T> registry = get(type);
        return registry == null ? null : registry.get(id);
    }

    /**
     * Unregister an object from registry.
     * 
     * @param <T> the type of the object to unregister
     * @param type the type of the object to unregister
     * @param id the key mapped with
     * @return the object
     */
    @Nullable
    public static <T> T unregister(Class<T> type, Identifier id) {
        Registry<T> registry;
        T object = (registry = get(type)) == null ? null : registry.remove(id);
        removeIfEmpty(type);
        return object;
    }

    /**
     * Unregister an object from registry.
     * 
     * @param <T> the type of the object to unregister
     * @param id the key mapped with
     * @param object the object to unregister
     * @return true if the object was unregistered
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
        Registry<?> registry = get(type);
        return registry != null && registry.containsKey(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isRegistered(Identifier id, T object) {
        Registry<T> registry = get((Class<T>)object.getClass()); T o;
        return registry != null && ((o = registry.get(id)) == object || object != null && object.equals(o));
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isRegistered(T object) {
        Registry<T> registry = get((Class<T>)object.getClass());
        return registry != null && registry.containsValue(object);
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

    public static Set<Map.Entry<Class<?>, Registry<?>>> entrySet() {
        return registries.entrySet();
    }

    public static Set<Class<?>> keySet() {
        return registries.keySet();
    }

    public static Collection<Registry<?>> values() {
        return registries.values();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> get(Class<T> type) {
        return (Registry<T>)registries.get(type);
    }

    public static <T> Registry<T> getOrPut(Class<T> type, Registry<T> registry) {
        Registry<T> registry2 = get(type);

        if (registry2 != null) {
            return registry2;
        }

        put(type, registry);
        return registry;
    }

    public static <T> Registry<T> getOrPut(Class<T> type) {
        return getOrPut(type, new Registry<>());
    }

    public static <T> Registry<?> put(Class<T> type, Registry<T> registry) {
        return registries.put(type, registry);
    }

    public static void putAll(Map<Class<?>, Registry<?>> registries) {
        registries.forEach(Registries.registries::put);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Registry<T> remove(Class<T> type) {
        return (Registry<T>)registries.remove(type);
    }

    @Nullable
    public static <T> Registry<T> removeIfEmpty(Class<T> type) {
        Registry<T> registry = get(type);
        return registry != null && registry.isEmpty() ? remove(type) : null;
    }

    public static <T> boolean remove(Class<T> type, Registry<T> registry) {
        return registries.remove(type, registry);
    }

    public static void clear() {
        registries.clear();
    }

    public static <T> Registry<?> replace(Class<T> type, Registry<T> registry) {
        return registries.replace(type, registry);
    }

    public static <T> boolean replace(Class<T> type, Registry<T> oldRegistries, Registry<T> newRegistries) {
        return registries.replace(type, oldRegistries, newRegistries);
    }

    public static void replaceAll(BiFunction<Class<?>, Registry<?>, Registry<?>> function) {
        registries.replaceAll(function);
    }

    static {
        registries = new HashMap<>();
    }
}
