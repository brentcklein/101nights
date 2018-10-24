package core;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Selector {
    private static Function<Integer,Integer> randomizer = new Random()::nextInt;

    public static void setRandomizer(Function<Integer,Integer> randomizer) {
        Selector.randomizer = randomizer;
    }

    public static Optional<Night> getRandomNight(List<Predicate<Night>> predicates) throws IOException {
//        This approach works because the working set is always limited. What would the
//        approach be if the set could be arbitrarily large?
        List<Night> filteredList = NightRepository.getNights();

//        Is there a better way to protect against empty lists of predicates? Should we throw an exception?
        predicates = predicates.size() > 0 ? predicates : Collections.singletonList((night->true));

        filteredList = filteredList.stream().filter(
                predicates.stream().reduce(Predicate::and).orElse(t->false)
            )
            .collect(Collectors.toList());

        if (filteredList.size() > 0) {
            return Optional.of(filteredList.get(randomizer.apply(filteredList.size())));
        } else {
            return Optional.empty();
        }

    }

    public static Optional<Night> getRandomNight() throws IOException {
        return getRandomNight(Collections.singletonList((night -> true)));
    }
}