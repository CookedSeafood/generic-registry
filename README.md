# Generic Registry

An object-storing type-based modify-on-fly single-end-origin registry.

## Feature

- Store any object
- Add, modify, or remove on fly

## Registry

### Register into Registry

```java
Registries.register(Identifier id, Object o);
```

### Get from Registry

```java
Registries.get(Class<T> type, Identifier id);
```
