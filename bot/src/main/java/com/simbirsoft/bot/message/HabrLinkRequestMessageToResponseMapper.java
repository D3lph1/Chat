package com.simbirsoft.bot.message;

import com.simbirsoft.bot.db.DatabaseService;

import java.util.Random;

/**
 * This implementation returns random habr.com post link on any accepted message.
 */
public class HabrLinkRequestMessageToResponseMapper implements RequestMessageToResponseMapper {
    private static final String HABR_URL = "https://habr.com/";

    private static final int MAX_POST_ID = 474776;

    private final DatabaseService databaseIoService;

    private final Random random;

    public HabrLinkRequestMessageToResponseMapper(DatabaseService databaseIoService) {
        this(databaseIoService, new Random());
    }

    public HabrLinkRequestMessageToResponseMapper(DatabaseService databaseIoService, Random random) {
        this.databaseIoService = databaseIoService;
        this.random = random;
    }

    @Override
    public String map(Message message) throws Exception {
        int post;
        do {
            post = random.nextInt(MAX_POST_ID) + 1;
        } while (databaseIoService.existsForUser(message.getSender(), post));
        databaseIoService.insert(message.getSender(), post);

        return buildLink(post);
    }

    private String buildLink(int post) {
        return HABR_URL + post;
    }
}
