package com.steelcloud.core.api;

import java.util.Collection;
import java.util.List;

public class ServiceUnavailableException extends IllegalStateException {
    private final List<Class<?>> missingTypes;

    public ServiceUnavailableException(Class<?> type) {
        super("Service not registered: " + type.getName());
        this.missingTypes = List.of(type);
    }

    public ServiceUnavailableException(Collection<Class<?>> types) {
        super("Services not registered: " +
                types.stream().map(Class::getName).reduce((a, b) -> a + ", " + b).orElse(""));
        this.missingTypes = List.copyOf(types);
    }

    public List<Class<?>> getMissingTypes() {
        return missingTypes;
    }
}