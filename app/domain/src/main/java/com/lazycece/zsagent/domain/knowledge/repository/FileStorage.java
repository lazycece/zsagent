package com.lazycece.zsagent.domain.knowledge.repository;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储接口（领域 SPI）。
 * domain 定义"我需要存取文件"，由 infrastructure 提供具体实现。
 *
 * @author lazycece
 */
public interface FileStorage {

    /**
     * 保存文件。
     *
     * @param path        文件路径（key）
     * @param inputStream 文件输入流
     * @return 文件大小（字节）
     * @throws IOException IO 异常
     */
    long store(String path, InputStream inputStream) throws IOException;

    /**
     * 读取文件。
     *
     * @param path 文件路径（key）
     * @return 文件输入流，调用方负责关闭
     * @throws IOException IO 异常
     */
    InputStream load(String path) throws IOException;

    /**
     * 删除文件。
     *
     * @param path 文件路径（key）
     * @return 是否删除成功
     */
    boolean delete(String path);
}
