package com.expensetracker;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class EmbeddedTomcatServer {
    
    public static void main(String[] args) throws Exception {
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        
        // Set port
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.parseInt(webPort));
        
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/expense-tracker", 
                new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + 
                new File("./" + webappDirLocation).getAbsolutePath());
        
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        
        tomcat.start();
        
        System.out.println("Tomcat started on port " + webPort);
        System.out.println("Application available at http://localhost:" + webPort + "/expense-tracker");
        System.out.println("Press Ctrl+C to stop the server");
        
        tomcat.getServer().await();
    }
} 