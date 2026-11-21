package com.steelcloud.core.api.data;

import com.google.gson.JsonObject;

public interface DataTableCodec<T> {
    T decode(JsonObject json) throws DataTableException;
    JsonObject encode(T value);
}