# Generic Registry

An object-storing type-based modify-on-fly single-end-origin registry.

## Feature

- Store any object
- Add or remove at any time

## Registry

### Register to Registry

```java
Registries.register(Identifier id, T object);
```

### Get from Registry

```java
Registries.get(Class<T> type, Identifier id);
```

### Unregister from Registry

```java
Registries.unregister(Class<T> type, Identifier id);
Registries.unregister(Identifier id, T object);
```
