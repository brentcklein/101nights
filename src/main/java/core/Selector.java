package core;

import java.util.List;
import java.util.Random;

public class Selector {
    private List<Night> nights;

    public Selector(List<Night> nights) {
        this.nights = nights;
    }

    public Night getRandomNight() {
        Random rand = new Random();

        return nights.get(rand.nextInt(nights.size()));
    }
}
