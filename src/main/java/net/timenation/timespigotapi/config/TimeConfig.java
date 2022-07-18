package net.timenation.timespigotapi.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by EinsLuca
 * Class create at 17.07.2022 19:02 @TimeSpigotAPI
 **/
public class TimeConfig {
    private MySQLConfigObject credentials;

    public static TimeConfig loadConfig(File file) {
        if (!file.exists()) {
            TimeConfig timeConfig = new TimeConfig();
            timeConfig.credentials = new MySQLConfigObject("admin", "test", "timenation", "127.0.0.1");

            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                fileOutputStream.write(gson.toJson(timeConfig).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return timeConfig;
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return new Gson().fromJson(new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8), TimeConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MySQLConfigObject getCredentials() {
        return credentials;
    }

}
