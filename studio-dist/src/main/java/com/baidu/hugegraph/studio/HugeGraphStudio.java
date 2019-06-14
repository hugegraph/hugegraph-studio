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

package com.baidu.hugegraph.studio;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.servlet.ServletException;

import org.apache.catalina.Host;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Server;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;

import com.baidu.hugegraph.studio.config.StudioServerConfig;
import com.baidu.hugegraph.util.Log;

/**
 * The Bootstrap of HugeGraphStudio.
 */
public class HugeGraphStudio {

    private static final Logger LOG = Log.logger(HugeGraphStudio.class);

    private static final String DEFAULT_CONFIG_FILE =
            "hugegraph-studio.properties";

    // The embed tomcat server
    private static Server server;

    /**
     * The entry point of application.
     *
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        StudioServerConfig config = new StudioServerConfig(DEFAULT_CONFIG_FILE);
        run(config);
        // Add shutdown hook to close studio server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (server != null) {
                    server.stop();
                }
            } catch (Exception e) {
                LOG.warn("Failed to stop studio server", e);
            }
        }));
        server.await();
    }

    /**
     * Run tomcat with configuration
     *
     * @param config the studio configuration
     * @throws Exception the exception
     */
    public static void run(StudioServerConfig config) throws Exception {

        String address = config.getHttpBindAddress();
        int port = config.getHttpPort();
        validateHttpPort(address, port);

        String baseDir = config.getServerBasePath();
        String uiDir = String.format("%s/%s", baseDir,
                                     config.getServerUIDirectory());
        String apiWarFile = String.format("%s/%s", baseDir,
                                          config.getServerWarDirectory());
        validatePathExists(new File(uiDir));
        validateFileExists(new File(apiWarFile));

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(config.getHttpPort());

        ProtocolHandler ph = tomcat.getConnector().getProtocolHandler();
        if (ph instanceof AbstractProtocol) {
            ((AbstractProtocol) ph).setAddress(InetAddress.getByName(address));
        }
        tomcat.setHostname(address);

        StandardContext ui = configureUi(tomcat, uiDir);
        StandardContext api = configureWarFile(tomcat, apiWarFile, "/api");

        tomcat.start();

        server = tomcat.getServer();
        while (!server.getState().equals(LifecycleState.STARTED)) {
            Thread.sleep(100L);
        }

        if (!ui.getState().equals(LifecycleState.STARTED)) {
            LOG.error("Studio-ui failed to start. " +
                      "Please check logs for details");
            System.exit(1);
        }
        if (!api.getState().equals(LifecycleState.STARTED)) {
            LOG.error("Studio-api failed to start. " +
                      "Please check logs for details");
            System.exit(1);
        }

        String upMessage = String.format("HugeGraphStudio is now running on: " +
                                         "http://%s:%d\n", address, port);
        LOG.info(upMessage);
    }


    private static StandardContext configureUi(Tomcat tomcat, String uiLocation)
            throws ServletException {

        ErrorPage errorPage = new ErrorPage();
        errorPage.setErrorCode(404);
        errorPage.setLocation("/index.html");
        String loc = new File(uiLocation).getAbsolutePath();

        StandardContext context = (StandardContext) tomcat.addWebapp("", loc);
        context.addWelcomeFile("/index.html");
        context.addErrorPage(errorPage);

        return context;
    }

    private static StandardContext configureWarFile(Tomcat tomcat,
                                                    final String warFile,
                                                    final String appBase)
            throws ServletException, IOException {

        if (warFile != null && warFile.length() > 0) {
            StandardContext context = (StandardContext) tomcat.addWebapp(appBase,
                                      new File(warFile).getAbsolutePath());
            Host host = (Host) context.getParent();
            File appBaseDirectory = host.getAppBaseFile();
            if (!appBaseDirectory.exists()) {
                appBaseDirectory.mkdirs();
            }
            context.setUnpackWAR(true);
            if (context.getJarScanner() instanceof StandardJarScanner) {
                ((StandardJarScanner) context.getJarScanner())
                        .setScanAllDirectories(true);
            }
            return context;
        }

        return null;
    }

    /**
     * To validate if the given http port is available or not. Exit if the port
     * is being used.
     */
    private static void validateHttpPort(String addr, int httpPort) {
        try (ServerSocket tmp = new ServerSocket(httpPort, 1,
                                                 InetAddress.getByName(addr))) {
        } catch (IOException e) {
            LOG.error("Can't start Studio on port {}: {}", httpPort, e);
            System.exit(1);
        }
    }

    private static void validatePathExists(File file) {
        if (!file.exists() || !file.isDirectory()) {
            LOG.error("Can't start Studio, directory {} doesn't exist",
                      file.getPath());
            System.exit(1);
        }
    }

    private static void validateFileExists(File file) {
        if (!file.exists() || !file.isFile()) {
            LOG.error("Can't start Studio, file {} doesn't exist",
                      file.getPath());
            System.exit(1);
        }
    }
}
