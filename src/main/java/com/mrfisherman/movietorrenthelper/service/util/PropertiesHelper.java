package com.mrfisherman.movietorrenthelper.service.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesHelper {

    public static Properties loadPropertiesFileForFilename(String fileName) {
        Properties properties = new Properties();
        try {
            Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
            File file = path.toFile();
            properties.load(new FileReader(file));
        } catch (IOException e) {
            System.out.println("There was a problem with property file!");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem with properties file path!");
            e.printStackTrace();
        }
        return properties;
    }

}
