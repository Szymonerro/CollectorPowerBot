package enums;

public enum CardPack {
    COLLECTION_AMAZON_S1("KqrL7Q4P/"),
    DRAGON_S1("GVx9zg5z/"),
    MONSTERS_S1("X2rmzxV6/"),
    EGYPT_OF_DESIRE("LWrwzQb0/");

    private final String path;

    CardPack(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
