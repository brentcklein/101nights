package core;

import java.util.Map;

public class DataSource {
    private Map<Integer,Night> nights;

    public Map<Integer,Night> getNights() {
        return nights;
    }

    public void setNights(Map<Integer,Night> nights) {
        this.nights = nights;
    }

    public void setNight(Integer index, Night night) {
        nights.put(index,night);
    }
}
