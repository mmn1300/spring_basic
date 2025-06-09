package project.spring_basic.constant;

import java.io.File;
import java.nio.file.Paths;

public class UserDefinePath {
    public static final String ABS_PATH = System.getProperty("user.dir");
    public static final String FILE_STORAGE_PATH = File.separator + Paths.get("src", "main", "resources", "static", "files").toString();
}
