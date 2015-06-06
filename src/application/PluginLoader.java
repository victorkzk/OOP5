package application;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.*;

public class PluginLoader {

    private static Logger logger = Logger.getLogger(PluginLoader.class.getName());
    private File pluginFile;

    public PluginLoader(File file) {
        pluginFile = file;
    }

    public Class getFurnitureClass() {
        try {
            URL jarURL = pluginFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
            JarFile jarFile = new JarFile(pluginFile);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.endsWith(".class")) {
                    name = name.replaceAll("/", ".");
                    name = name.replaceAll(".class", "");
                    Class <?> plugClass = classLoader.loadClass(name);
                    Class <?> superClass = plugClass.getSuperclass();
                    if (superClass.getName().endsWith(".Furniture")) {
                        Class furnitureClass = classLoader.loadClass(plugClass.getName());
                        return furnitureClass;
                    }
                }
            }
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "MalformedURLException: " + e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException: " + e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "ClassNotFoundException: " + e);
        }
        return null;
    }

    public Class loadSerializer() {
        return loadPluginByInterfaceName("Serializer");
    }

    public Class loadDeserializer() {
        return  loadPluginByInterfaceName("Deserializer");
    }

    public Class loadPluginByInterfaceName(String interfaceName) {
        try {
            URL jarURL = pluginFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
            JarFile jarFile = new JarFile(pluginFile);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.endsWith(".class")) {
                    name = name.replaceAll("/", ".");
                    name = name.replaceAll(".class", "");
                    Class <?> plugClass = classLoader.loadClass(name);
                    Class <?>[] interfaces = plugClass.getInterfaces();
                    for (int i = 0; i < interfaces.length; i++) {
                        if (interfaces[i].getName().endsWith("." + interfaceName)) {
                            Class furnitureClass = classLoader.loadClass(plugClass.getName());
                            return furnitureClass;
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "MalformedURLException: " + e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException: " + e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "ClassNotFoundException: " + e);
        }
        return null;
    }
}
