package core;

import javax.servlet.http.Part;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public Optional<Night> getRandomNight(List<Predicate<Night>> predicates) throws DataException {
//        This approach works because the working set is always limited. What would the
//        approach be if the set could be arbitrarily large?
        List<Night> filteredList = repository.getNights();

        predicates = predicates != null && predicates.size() > 0 ? predicates : Collections.singletonList((night->true));

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
    
    public Optional<Night> getRandomNight(Map<String,?> filters) throws DataException {
        filters = filters != null ? filters : Collections.emptyMap();

        List<Predicate<Night>> predicates = new ArrayList<>();

        filters.forEach((k,v) -> {
            try {
                Method getter = new PropertyDescriptor(k, Night.class).getReadMethod();

                if (getter != null) {
                    predicates.add(n -> {
                        try {
                            if (Enum.class.isAssignableFrom(getter.getReturnType())) {
                                Enum enumValue = (Enum)getter.invoke(n);
                                return v.equals(enumValue.name());
                            }
                            return v.equals(getter.invoke(n));
                        } catch (IllegalAccessException|InvocationTargetException ignored){}
//                        If the client passes a filter that doesn't apply, ignore it (return true for this Predicate)
                        return true;
                    });
                }
            } catch (IntrospectionException ie) {
//                If they pass a property that doesn't exist, just ignore it.
            }
        });

        return getRandomNight(predicates);
    }

    public Optional<Night> getRandomNight() throws DataException {
        return getRandomNight(Collections.singletonList((night -> true)));
    }

    public NightRepository getRepository() {
        return repository;
    }
}