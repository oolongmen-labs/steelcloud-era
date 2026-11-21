package com.steelcloud.core.api.compat;

import java.util.Optional;

public interface ModCompatService {
    boolean isLoaded(String modId);
    Optional<String> getVersion(String modId);
    boolean isVersionAtLeast(String modId, String minimum);

    /** 未满足时记 WARN 日志，返回 false；不抛异常。 */
    boolean requireOrWarn(String modId, String minimumVersion, String featureName);
}