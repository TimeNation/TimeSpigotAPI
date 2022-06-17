package net.timenation.timespigotapi.manager.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class RequestManager {

    @SneakyThrows
    public void sendHttpRequestPost(String username, UUID uuid, String ip) {
        String urlParameters = "key=nmOGpnvkCrrIug8Hxdvot9VI2Jrfwz4ASI9O9zNQjIRihQwTqF&name=" + username + "&uuid=" + uuid + "&ip=" + ip.replace("/", "");
        System.out.println(urlParameters);

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://localhost:8080/player?" + urlParameters).openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();

        String line = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();
    }

    @SneakyThrows
    public void sendHttpRequestPut(UUID uuid, String username, int crystals, int lootboxes, String helmet, String gadget, String language) {
        String urlParameters = "key=nmOGpnvkCrrIug8Hxdvot9VI2Jrfwz4ASI9O9zNQjIRihQwTqF&type=update_player&uuid="+ uuid + "&name=" + username + "&crystals=" + crystals + "&lootboxes=" + lootboxes + "&helmet=" + helmet + "&gadget=" + gadget + "&language=" + language;
        System.out.println(urlParameters);

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://localhost:8080/player?" + urlParameters).openConnection();
        httpURLConnection.setRequestMethod("PUT");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();

        String line = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();
    }

    @SneakyThrows
    public JsonObject getHttpResponse(UUID playerUuid) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://localhost:8080/player?key=nmOGpnvkCrrIug8Hxdvot9VI2Jrfwz4ASI9O9zNQjIRihQwTqF&uuid=" + playerUuid + "&plugin=TimeSpigotAPI").openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();

        String line = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();

        if (response.toString().equals("Wrong key!")) return null;
        return new JsonParser().parse(response.toString()).getAsJsonObject();
    }
}
