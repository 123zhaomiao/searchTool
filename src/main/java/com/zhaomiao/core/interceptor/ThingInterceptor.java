package com.zhaomiao.core.interceptor;
import com.zhaomiao.core.model.Thing;

@FunctionalInterface
public interface ThingInterceptor {
    void apply(Thing thing);
}
