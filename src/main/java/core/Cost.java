package core;

public enum Cost {
    FREE(0),LOW(1),MED(2),HIGH(3),EXP(4);

    private final int value;

    Cost(final int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
