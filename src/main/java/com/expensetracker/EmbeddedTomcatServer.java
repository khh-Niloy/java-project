package com.expensetracker;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.jasper.servlet.JasperInitializer;

import java.io.File;
import java.util.Set;

public class EmbeddedTomcatServer {
    
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Embedded Tomcat Server...");
        
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        System.out.println("Configuring Tomcat to use port: " + webPort);
        tomcat.setPort(Integer.parseInt(webPort));
        
        tomcat.setHostname("localhost");
        
        String baseDirPath = System.getProperty("java.io.tmpdir") + "/tomcat-embedded";
        System.out.println("Base directory: " + baseDirPath);
        File baseDir = new File(baseDirPath);
        baseDir.mkdirs();
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        
        tomcat.getConnector();
        
        System.out.println("Adding webapp context at /expense-tracker");
        System.out.println("Webapp dir: " + new File(webappDirLocation).getAbsolutePath());
        
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/expense-tracker", 
                new File(webappDirLocation).getAbsolutePath());
        
        System.out.println("Configuring app with basedir: " + 
                new File("./" + webappDirLocation).getAbsolutePath());
        
        ctx.addServletContainerInitializer(new JasperInitializer(), null);
        
        String jspCompilerPath = System.getProperty("user.dir") + "/lib/ecj-3.18.0.jar";
        System.out.println("JSP Compiler path: " + jspCompilerPath);
        if (new File(jspCompilerPath).exists()) {
            System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");
        } else {
            System.out.println("WARNING: JSP compiler jar not found at " + jspCompilerPath);
            System.out.println("Please run download_embedded_tomcat.bat to download dependencies");
        }
        
        File additionWebInfClasses = new File("target/classes");
        System.out.println("Classes directory: " + additionWebInfClasses.getAbsolutePath());
        
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        
        System.out.println("Starting Tomcat...");
        tomcat.start();
        
        System.out.println("Tomcat started successfully");
        System.out.println("Tomcat started on port " + webPort);
        System.out.println("Application available at http://localhost:" + webPort + "/expense-tracker");
        System.out.println("Press Ctrl+C to stop the server");
        
        tomcat.getServer().await();
    }
} 