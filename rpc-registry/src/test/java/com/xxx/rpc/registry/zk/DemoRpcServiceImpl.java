package com.xxx.rpc.registry.zk;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuang.kou
 * @createTime 2020年05月10日 07:52:00
 */
@Slf4j
public class DemoRpcServiceImpl implements DemoRpcService {

    @Override
    public String hello() {
        return "hello";
    }
}
