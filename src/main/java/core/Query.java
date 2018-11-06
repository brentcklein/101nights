package core;

public enum Query {
    GETALLNIGHTS("SELECT * FROM nights"),
    GETNIGHTBYID("SELECT * FROM nights WHERE id = ?"),
    NEXTID("SELECT nextval('nights_id_seq')"),
    SAVENIGHT("INSERT INTO " +
            "nights(\"id\",\"complete\",\"hasECard\",\"involvesFood\",\"involvesProps\",\"involvesTravel\",\"cost\",\"partner\") " +
            "VALUES(?,?,?,?,?,?,?,?) " +
            "ON CONFLICT (id) DO UPDATE " +
                "SET \"complete\" = EXCLUDED.\"complete\", " +
                    "\"hasECard\" = EXCLUDED.\"hasECard\", " +
                    "\"involvesFood\" = EXCLUDED.\"involvesFood\", " +
                    "\"involvesProps\" = EXCLUDED.\"involvesProps\", " +
                    "\"involvesTravel\" = EXCLUDED.\"involvesTravel\", " +
                    "\"cost\" = EXCLUDED.\"cost\", " +
                    "\"partner\" = EXCLUDED.\"partner\"");

    private final String value;

    Query(final String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
