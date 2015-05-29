package application;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
