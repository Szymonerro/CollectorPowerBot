package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectResponse {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("token_count")
    private int tokenCount;

    @JsonProperty("time_until_next")
    private int timeUntilNext;

    @JsonProperty("message")
    private String message;

    @JsonProperty("auto_token_generated")
    private boolean autoTokenGenerated;

    @JsonProperty("error")
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public int getTimeUntilNext() {
        return timeUntilNext;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAutoTokenGenerated() {
        return autoTokenGenerated;
    }

    public String getError() {
        return error;
    }
}
