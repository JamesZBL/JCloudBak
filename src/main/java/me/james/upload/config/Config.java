package me.james.upload.config;

import lombok.extern.slf4j.Slf4j;
import me.james.upload.utils.AESUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * 默认配置项读取实现
 *
 * @author James
 * @email 1146556298@qq.com
 * @date 2018-07-28
 */
@Slf4j
public class Config implements ConfigSupplier {

    private Properties configs;

    private static Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        this.configs = new Properties();
        try {
            configs.load(getClass().getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            log.error("Failed to load application configuration file.");
        }
    }

    @Override
    public long getLong(String s) {
        return Long.parseLong(configs.getProperty(s));
    }

    @Override
    public int getInt(String s) {
        return Integer.parseInt(configs.getProperty(s));
    }

    @Override
    public String getString(String s) {
        return configs.getProperty(s);
    }

    @Override
    public String getDecrypt(String s) {
        try {
            return AESUtil.decrypt(getString(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] getStringArray(String s) {
        return this.getString(s).split("\\b*[,，/]\\b*");
    }
}
