package core;

public enum Query {
    GETALLNIGHTS("SELECT * FROM nights"),
    SAVENIGHT("INSERT INTO nights(\"complete\",\"hasECard\",\"involvesFood\",\"involvesProps\",\"involvesTravel\",\"cost\",\"partner\") VALUES(?,?,?,?,?,?,?)");

    private final String value;

    Query(final String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
