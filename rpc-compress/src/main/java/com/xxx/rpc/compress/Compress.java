package com.xxx.rpc.compress;

import com.xxx.rpc.common.annoation.SPI;

/**
 * 数据压缩模块
 */
@SPI
public interface Compress {

    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);
}
