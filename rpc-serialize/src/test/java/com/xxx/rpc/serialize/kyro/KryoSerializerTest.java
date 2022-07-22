package com.xxx.rpc.serialize.kyro;

import com.xxx.rpc.proto.RPCRequest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KryoSerializerTest {
    @Test
    public void hessianSerializerTest() {
        RPCRequest target = RPCRequest.builder().methodName("hello")
                .params(new Object[]{"sayhelooloo", "sayhelooloosayhelooloo"})
                .interfaceName("com.xxx.rpc.example.api.HelloService")
                .paramTypes(new Class<?>[]{String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .group("group1")
                .version("version1")
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] bytes = kryoSerializer.serialize(target);
        RPCRequest actual = kryoSerializer.deserialize(bytes, RPCRequest.class);
        assertEquals(target.getGroup(), actual.getGroup());
        assertEquals(target.getVersion(), actual.getVersion());
        assertEquals(target.getRequestId(), actual.getRequestId());
    }
}