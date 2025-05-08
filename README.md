# Generic Registry

An object-storing type-based modify-on-fly single-end-origin registry.

## Feature

- Store any object
- Add or remove at any time

## Registry

### Register into Registry

```java
Registries.register(Identifier id, Object o);
```

### Get from Registry

```java
Registries.get(Class<T> type, Identifier id);
```
