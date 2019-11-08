package com.simbirsoft.bot.http;

import java.io.IOException;
import java.net.URL;

/**
 * ApiRequestPerformer is interface that executes request to the API.
 */
public interface ApiRequestPerformer {
    String perform(URL url) throws IOException;
}
