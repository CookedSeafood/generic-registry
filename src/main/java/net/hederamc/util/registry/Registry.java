package net.hederamc.util.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper class containing a map maps string to object.
 *
 * <p>This should <i>not</i> be instanced manualy.</p>
 *
 * @see Registries#register(Identifier, Object)
 */
public class Registry<T> {
    private final Map<Identifier, T> registry;

    public Registry(Map<Identifier, T> registry) {
        this.registry = registry;
    }

    public Registry() {
        this(new HashMap<>());
    }

    public static <T> Registry<T> of(Map<Identifier, T> registry) {
        return new Registry<>(registry);
    }

    public boolean isRegistered(Identifier id) {
        return this.containsKey(id);
    }

    public boolean isRegistered(T object) {
        return this.containsValue(object);
    }

    public boolean isRegistered(Identifier id, T object) {
        T o;
        return (o = registry.get(id)) == object || object != null && object.equals(o);
    }

    public T getOrPut(Identifier id, T object) {
        T presented = this.get(id);
        if (presented != null) {
            return presented;
        }

        this.put(id, object);
        return object;
    }

    public Map<Identifier, T> getRegistry() {
        return this.registry;
    }

    public int size() {
        return this.registry.size();
    }

    public boolean isEmpty() {
        return this.registry.isEmpty();
    }

    public boolean containsKey(Identifier id) {
        return this.registry.containsKey(id);
    }

    public boolean containsValue(T object) {
        return this.registry.containsValue(object);
    }

    @Nullable
    public T get(Identifier id) {
        return this.registry.get(id);
    }

    @Nullable
    public T put(Identifier id, T object) {
        return this.registry.put(id, object);
    }

    @Nullable
    public T remove(Identifier id) {
        return this.registry.remove(id);
    }

    public void putAll(Map<Identifier, T> registry) {
        this.registry.putAll(registry);
    }

    public void clear() {
        this.registry.clear();
    }

    public Set<Identifier> keySet() {
        return this.registry.keySet();
    }

    public Collection<T> values() {
        return this.registry.values();
    }

    public Set<Map.Entry<Identifier, T>> entrySet() {
        return this.registry.entrySet();
    }

    public T getOrDefault(Identifier id, T defaultValue) {
        return this.registry.getOrDefault(id, defaultValue);
    }

    public void forEach(BiConsumer<Identifier, T> action) {
        this.registry.forEach(action);
    }

    public void replaceAll(BiFunction<Identifier, T, T> function) {
        this.registry.replaceAll(function);
    }

    public T putIfAbsent(Identifier id, T object) {
        return this.registry.putIfAbsent(id, object);
    }

    public boolean remove(Identifier id, T object) {
        return this.registry.remove(id, object);
    }

    public boolean replace(Identifier id, T oldRegistry, T newRegistry) {
        return this.registry.replace(id, oldRegistry, newRegistry);
    }

    @Nullable
    public T replace(Identifier id, T object) {
        return this.registry.replace(id, object);
    }

    @Nullable
    public T computeIfAbsent(Identifier id, Function<Identifier, T> mappingFunction) {
        return this.registry.computeIfAbsent(id, mappingFunction);
    }

    @Nullable
    public T computeIfPresent(Identifier id, BiFunction<Identifier, T, T> remappingFunction) {
        return this.registry.computeIfPresent(id, remappingFunction);
    }

    @Nullable
    public T compute(Identifier id, BiFunction<Identifier, T, T> remappingFunction) {
        return this.registry.compute(id, remappingFunction);
    }

    @Nullable
    public T merge(Identifier id, T object, BiFunction<T, T, T> remappingFunction) {
        return this.registry.merge(id, object, remappingFunction);
    }

    @Nullable
    public Class<T> getActualTypeParameter() {
        return Registries.getType(this);
    }

    @Override
    public String toString() {
        return "Registry<" + getActualTypeParameter().getName() + ">";
    }
}
