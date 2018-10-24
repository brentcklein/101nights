import core.Night;
import core.NightRepository;
import core.Selector;
import org.junit.jupiter.api.AfterEach;
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

//        Test no nights at all
        try {
            assertEquals(0,NightRepository.getNights().size());
            Selector.getRandomNight()
            .ifPresentOrElse(
                    (n)->fail("There should not be any Nights."),
                    ()->{}
            );
        } catch (IOException ioe) {
            assertEquals(
                    "Could not get list of Nights.",
                    ioe.getMessage()
            );
        }


//        Make test nights
        NightRepository.saveNights(Arrays.asList(
            new Night(1),
            new Night(2),
            new Night(3)
        ));

        assertEquals(3,NightRepository.getNights().size());

        Selector.setRandomizer(integer -> 1);

//        get random night
        Selector.getRandomNight()
        .ifPresentOrElse(
//        confirm night is the second one we defined above (position 1 in 0-indexed list)
                (n)->assertEquals(2,(int)n.getId()),
                ()->fail("Could not find Night.")
        );
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
        Selector.getRandomNight(predicates)
        .ifPresentOrElse(
                (n)->assertEquals(2, (int)n.getId()),
                ()->fail("Could not get Night.")
        );


//        Test empty list of predicates as indicating a lack of filters
        Selector.getRandomNight(Collections.emptyList())
        .ifPresentOrElse(
                (randomNight)->assertEquals(1,(int)randomNight.getId()),
                ()->fail("Could not get Night.")
        );

        Selector.setRandomizer(new Random()::nextInt);

//        Test multiple predicates. Only remaining id should be 4.
        predicates.add(night->night.getId() > 2);
        Selector.getRandomNight(predicates)
        .ifPresentOrElse(
                (randomNight)->assertEquals(4, (int)randomNight.getId()),
                ()->fail("Could not get Night.")
        );

        predicates.add(n->n.getId()>5);
        Selector.getRandomNight(predicates)
        .ifPresentOrElse(
                (n)->fail("Found Night when should have returned none."),
                ()->{}
        );
    }

    @AfterEach
    private void clearNights() {
        NightRepository.saveNights(Collections.emptyList());
    }
}
