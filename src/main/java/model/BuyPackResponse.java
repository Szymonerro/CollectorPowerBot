package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BuyPackResponse {

    private boolean success;
    private List<Card> cards;
    private String message;

    @JsonProperty("token_count")
    private int tokenCount;

    @JsonProperty("max_tokens")
    private int maxTokens;

    @JsonProperty("completion_data")
    private CompletionData completionData;

//    @JsonProperty("error")
//    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public CompletionData getCompletionData() {
        return completionData;
    }

    public void setCompletionData(CompletionData completionData) {
        this.completionData = completionData;
    }

    // --- Nested Classes ---

    public static class Card {
        private String name;
        private String rarity;
        private String set;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("card_name")
        private String cardName;

        @JsonProperty("is_new")
        private boolean isNew;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRarity() {
            return rarity;
        }

        public void setRarity(String rarity) {
            this.rarity = rarity;
        }

        public String getSet() {
            return set;
        }

        public void setSet(String set) {
            this.set = set;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public boolean isNew() {
            return isNew;
        }

        public void setNew(boolean aNew) {
            isNew = aNew;
        }
    }

    public static class CompletionData {

        @JsonProperty("set_id")
        private int setId;

        @JsonProperty("owned_cards")
        private int ownedCards;

        @JsonProperty("total_cards")
        private int totalCards;

        @JsonProperty("completion_percentage")
        private int completionPercentage;

        public int getSetId() {
            return setId;
        }

        public void setSetId(int setId) {
            this.setId = setId;
        }

        public int getOwnedCards() {
            return ownedCards;
        }

        public void setOwnedCards(int ownedCards) {
            this.ownedCards = ownedCards;
        }

        public int getTotalCards() {
            return totalCards;
        }

        public void setTotalCards(int totalCards) {
            this.totalCards = totalCards;
        }

        public int getCompletionPercentage() {
            return completionPercentage;
        }

        public void setCompletionPercentage(int completionPercentage) {
            this.completionPercentage = completionPercentage;
        }
    }
}
