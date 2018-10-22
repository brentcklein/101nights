import core.Night;
import core.Selector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class SelectorTests {

    /**
     * It should return a random Night object from a predetermined list.
     */
    @Test
    void getRandomNight() {

//        Make test nights
        List<Night> nights = new ArrayList<>();
        nights.add(new Night(1));
        nights.add(new Night(2));
        nights.add(new Night(3));

//        Instantiate selector with test nights
        Selector selector = new Selector(nights);

//        get random night
        Night randomNight = selector.getRandomNight();
        System.out.println("[TEST] Random night id: " + randomNight.getId());

//        confirm night is one of the ones we defined above
        assertEquals(
                randomNight,
                nights
                        .stream()
                        .filter((n) -> n.getId().equals(randomNight.getId()))
                        .findFirst()
                        .get());
    }

    /**
    * It should return a random Night object from a filtered list
    */
    @Test
    void getFilteredNight() {
        //        Make test nights
        List<Night> nights = new ArrayList<>();
        nights.add(new Night(1));
        nights.add(new Night(2));
        nights.add(new Night(3));
        nights.add(new Night(4));
        nights.add(new Night(5));

//        Instantiate selector with test nights
        Selector selector = new Selector(nights);

        assertEquals(5,nights.size());

//        filter out all odd ids and all ids <= 2 (should leave only id == 4)
        List<Predicate<Night>> predicates = new ArrayList<>();
        predicates.add(night -> night.getId() % 2 == 0);
        predicates.add(night -> night.getId() > 2);

//        get random night using filters
        Night randomNight = selector.getRandomNight(predicates);
        System.out.println("[TEST] Random night id: " + randomNight.getId());

//        confirm night has id of 4
        assertEquals(4, (int)randomNight.getId());
    }

}
