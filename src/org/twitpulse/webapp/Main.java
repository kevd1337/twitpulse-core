package org.twitpulse.webapp;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application.
 * The Java command that is used for launching should fire this main method.
 * 
 * @author kevd1337
 */
public class Main {

    /**
     * Main entry point of web application
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String webappDirLocation = "webapp/";

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "5000";
        }

        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);
        root.setParentLoaderPriority(true);

        Server server = new Server(Integer.valueOf(webPort));
        server.setHandler(root);
        server.start();
        server.join();
    }
}
