import core.Night;
import core.NightRepository;
import core.Selector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class SelectorTests {

    /**
     * It should return a random Night object from a predetermined list.
     */
    @Test
    void getRandomNight() throws IOException {

//        Make test nights
        NightRepository.saveNights(Arrays.asList(
            new Night(1),
            new Night(2),
            new Night(3)
        ));

        assertEquals(3,NightRepository.getNights().size());

        Selector.setRandomizer(integer -> 1);

//        get random night
        Night randomNight = Selector.getRandomNight();

//        confirm night is the second one we defined above (position 1 in 0-indexed list)
        assertEquals(2,(int)randomNight.getId());
    }

    /**
    * It should return a random Night object from a filtered list
    */
    @Test
    void getFilteredNight() throws IOException {
        NightRepository.saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4),
                new Night(5)
        ));

        assertEquals(5,NightRepository.getNights().size());

        Selector.setRandomizer(i->0);

//        filter out all odd ids and all ids <= 2 (should leave only id == 4)
        List<Predicate<Night>> predicates = new ArrayList<>();
        predicates.add(night->night.getId() % 2 == 0);
        Night randomNight = Selector.getRandomNight(predicates);
        assertEquals(2, (int)randomNight.getId());

//        Test empty list of predicates as indicating a lack of filters
        randomNight = Selector.getRandomNight(Collections.emptyList());
        assertEquals(1,(int)randomNight.getId());

        Selector.setRandomizer(new Random()::nextInt);

//        Test multiple predicates. Only remaining id should be 4.
        predicates.add(night->night.getId() > 2);
        randomNight = Selector.getRandomNight(predicates);
        assertEquals(4, (int)randomNight.getId());
    }

}
