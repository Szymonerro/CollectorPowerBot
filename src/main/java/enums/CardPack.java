package enums;

public enum CardPack {
    COLLECTION_AMAZON_S1("/buy-pack/KqrL7Q4P/"),
    DRAGON_S1("/buy-pack/GVx9zg5z/"),
    MONSTERS_S1("/buy-pack/X2rmzxV6/"),
    EGYPT_OF_DESIRE("/buy-pack/LWrwzQb0/");

    private final String path;

    CardPack(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
