package core;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Selector {
    private Function<Integer,Integer> randomizer = new Random()::nextInt;
    private NightRepository repository;

    public Selector(Mode mode) throws DataException {
        this.repository = new NightRepository(mode);
    }

    public Selector() throws DataException {
        this(Mode.DEV);
    }

    public void setRandomizer(Function<Integer,Integer> randomizer) {
        this.randomizer = randomizer;
    }

    public Optional<Night> getRandomNight(List<Predicate<Night>> predicates) {
//        This approach works because the working set is always limited. What would the
//        approach be if the set could be arbitrarily large?
        List<Night> filteredList = repository.getNights();

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

    public Optional<Night> getRandomNight() {
        return getRandomNight(Collections.singletonList((night -> true)));
    }

    public NightRepository getRepository() {
        return repository;
    }
}