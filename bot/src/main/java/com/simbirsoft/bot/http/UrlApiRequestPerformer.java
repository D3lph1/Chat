package com.simbirsoft.bot.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * This implementation uses {@link URL} built-in tools to execute HTTP requests.
 */
public class UrlApiRequestPerformer implements ApiRequestPerformer {
    public String perform(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }

        in.close();

        return sb.toString();
    }
}
