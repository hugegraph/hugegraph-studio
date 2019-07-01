/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.baidu.hugegraph.studio.board.serializer;

import com.baidu.hugegraph.studio.board.model.Board;
import com.baidu.hugegraph.studio.config.StudioApiConfig;
import com.baidu.hugegraph.util.E;
import com.baidu.hugegraph.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository("boardSerializer")
public class BoardSerializer {

    private static final Logger LOG = Log.logger(BoardSerializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    private StudioApiConfig configuration;
    private String filePath;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public BoardSerializer() {
        initBoardRepository();
    }

    private void initBoardRepository() {
        configuration = StudioApiConfig.getInstance();
        String baseDir = configuration.getBaseDirectory();
        Preconditions.checkNotNull(baseDir);
        LOG.debug("The base directory is: {}", baseDir);
        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        E.checkArgument(dir.exists() && dir.isDirectory(),
                        "Please ensure the directory '%s' exist",
                        baseDir);

        filePath = configuration.getBoardFilePath();
    }

    private void ensureFileExist() {
        Preconditions.checkNotNull(filePath);
        LOG.debug("The board file path is: {}", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(String.format(
                          "Failed to create board file with path '%s'",
                          filePath), e);
            }
        }
        E.checkArgument(file.exists() && file.isFile(),
                        "Please ensure the board file '%s' exist",
                        filePath);
    }

    /**
     * Save board entity to disk as json.
     */
    // TODO: Support append and serialize in bytes
    public void save(Board board) {
        Preconditions.checkNotNull(board);

        ensureFileExist();
        board.setUpdateTime(Instant.now().getEpochSecond());

        /*
         * Should we flush and close for each buffered writing? Also, why
         * not throw runtime exception if write failed in this case?
         */
        writeLock.lock();
        try (OutputStream os = new FileOutputStream(filePath);
             DataOutputStream out = new DataOutputStream(os)) {
            byte[] all = mapper.writeValueAsString(board)
                               .getBytes(Charsets.UTF_8);
            out.writeInt(all.length);
            out.write(all);
            LOG.debug("Write board file: {}", filePath);
        } catch (IOException e) {
            LOG.error("Failed to write board file: {}", filePath, e);
            throw new RuntimeException(String.format(
                      "Failed to write board file: %s", filePath));
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Read board entity from disk.
     */
    public Board load() {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0L) {
            return new Board();
        }
        readLock.lock();
        try (InputStream is = new FileInputStream(filePath);
             DataInputStream input = new DataInputStream(is)) {
            int len = input.readInt();
            byte[] bytes = new byte[len];
            LOG.debug("Read total data: {} bytes", len);
            input.readFully(bytes);
            return mapper.readValue(bytes, Board.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format(
                      "Failed to read File: '%s'", filePath), e);
        } finally {
            readLock.unlock();
        }
    }
}
