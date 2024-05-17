package com.yijiawencoder.codesandbox.security;

import java.security.Permission;

/**
 * 定义一个默认的SecurityManager
 */
public class DefaultSecurityManager extends  SecurityManager {
    /**
     *
     * @param perm   the requested permission.
     */
    @Override
    public void checkPermission(Permission perm) {
        //默认不做任何处理

    }
}
