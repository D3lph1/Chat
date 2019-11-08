package com.simbirsoft.chat.service.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application http response object. JSON-serialized representation of this
 * object used by frontend.
 */
@JsonSerialize
public class Response {
    public static final String STATUS_SUCCESS = "success";

    public static final String STATUS_FAILED = "failed";

    public static final String STATUS_VALIDATION_FAILED = "validation_failed";

    public static final String STATUS_INTERNAL_SERVER_ERROR = "internal_server_error";

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private Map<String, Object> data = new HashMap<>();

    @JsonProperty("errors")
    private Collection<String> errors = new ArrayList<>();

    public Response(String status) {
        this.status = status;
    }

    public Response(String status, Collection<String> errors) {
        this(status);
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Response add(String key, Object value) {
        data.put(key, value);

        return this;
    }

    public Collection<String> getErrors() {
        return errors;
    }

    public Response addError(String error) {
        errors.add(error);

        return this;
    }
}
