package core;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Selector {
    private List<Night> nights;

    public Selector(List<Night> nights) {
        this.nights = nights;
    }

    public Night getRandomNight(List<Predicate<Night>> predicates) {
        List<Night> filteredList = nights;

        if (predicates != null) {
            filteredList = nights.stream().filter(
                    predicates.stream().reduce(Predicate::and).orElse(t->false)
                )
                .collect(Collectors.toList());
        }

        Random rand = new Random();

        return filteredList.get(rand.nextInt(filteredList.size()));
    }

    public Night getRandomNight() {
        return getRandomNight(null);
    }
}
