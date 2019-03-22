package com.zhaomiao.core.interceptor;
import java.io.File;

//函数式编程
@FunctionalInterface
public interface FileInterceptor {

    void apply(File file);
}
