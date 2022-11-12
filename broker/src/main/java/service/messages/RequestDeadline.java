package service.messages;

public class RequestDeadline {
    private int seedId;

    public RequestDeadline(int seed_id) {
        this.seedId = seed_id;
    }

    public RequestDeadline() {
    }

    public int getSeedId() {
        return seedId;
    }

    public void setSeed_id(int seedId) {
        this.seedId = seedId;
    }
}
