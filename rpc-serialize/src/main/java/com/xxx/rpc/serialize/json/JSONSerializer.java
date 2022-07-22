package com.xxx.rpc.serialize.json;

import com.alibaba.fastjson.JSON;
import com.xxx.rpc.serialize.Serializer;

public class JSONSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
