package com.zhaomiao.core.index;

import com.zhaomiao.core.interceptor.FileInterceptor;

public interface FileScan {
    /**
     * 遍历path
     * @param path
     */
    void index(String path);

    /**
     * 拦截器
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);

}