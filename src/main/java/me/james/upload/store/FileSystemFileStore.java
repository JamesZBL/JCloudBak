package me.james.upload.store;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * 磁盘文件读写实现
 *
 * @author James
 * @email 1146556298@qq.com
 * @date 2018-07-28
 */
@Slf4j
public class FileSystemFileStore implements FileStore {

    private static FileSystemFileStore ourInstance = new FileSystemFileStore();

    public static FileSystemFileStore getInstance() {
        return ourInstance;
    }

    private final Set<String> repo = new TreeSet<>();
    private static final String FILE_NAME = "app.repo";
    private File file;

    private FileSystemFileStore() {
        file = new File(FILE_NAME);
        if (!file.exists()) {
            boolean success = true;
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Error while creating repo file.");
                file = null;
            } finally {
                if (!success) file = null;
            }
        }
        try {
            Optional.ofNullable(file).orElseThrow(IOException::new);
            FileUtils.readLines(file, Charset.forName("utf-8"))
                    .forEach(c -> repo.add(String.valueOf(c)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store(String path) throws IOException {
        Optional.ofNullable(file).orElseThrow(IOException::new);
        FileUtils.writeStringToFile(file, path + '\n', Charset.forName("utf8"), true);
        repo.add(path);
    }

    @Override
    public boolean exist(String path) {
        try {
            Optional.ofNullable(file).orElseThrow(IOException::new);
            return repo.contains(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
