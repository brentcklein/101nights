import core.Night;
import core.NightRepository;
import core.Selector;
import static core.Cost.*;
import static core.Partner.*;

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
        assertEquals(0,NightRepository.getNights().size());
        Selector.getRandomNight()
        .ifPresentOrElse(
                (n)->fail("There should not be any Nights."),
                ()->{}
        );


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

    @Test
    void getComplexFilteredNights() throws IOException {
        NightRepository.saveNights(Arrays.asList(
                new Night(1, false, false, true, false, true, MED, HIM),
                new Night(2, true, true, false, false, false, LOW, HER),
                new Night(3, false, true, false, false, false, FREE, HIM),
                new Night(4, false, false, false, true, false, HIGH, HER),
                new Night(5, false, true, true, true, false, MED, HIM),
                new Night(6, false, true, false, false, false, FREE, HER),
                new Night(7),
                new Night(8, false, false, true, false, false, LOW, HIM),
                new Night(9),
                new Night(10)
        ));

        assertEquals(10, NightRepository.getNights().size());

        Predicate<Night> open = n->!n.isComplete();
        Predicate<Night> eCard = Night::hasECard;
        Predicate<Night> food = Night::involvesFood;
        Predicate<Night> props = Night::involvesProps;
        Predicate<Night> travel = Night::involvesTravel;
        Predicate<Night> free = n->n.getCost().equals(FREE);
        Predicate<Night> expensive = n->n.getCost().getValue() > 1;
        Predicate<Night> his = n->n.getPartner().equals(HIM);
        Predicate<Night> hers = n->n.getPartner().equals(HER);
        Predicate<Night> both = n->n.getPartner().equals(BOTH);

        Selector.setRandomizer(i->1);

        Selector.getRandomNight(Collections.singletonList(open))
        .ifPresentOrElse(
                (night -> assertEquals(3, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        Selector.getRandomNight(Collections.singletonList(eCard))
        .ifPresentOrElse(
                (night -> assertEquals(3, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        Selector.getRandomNight(Arrays.asList(free,travel))
        .ifPresent(
                (night -> fail("Should not have found Night."))
        );

        Selector.getRandomNight(Collections.singletonList(food))
        .ifPresentOrElse(
                (night -> assertEquals(5, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        Selector.getRandomNight(Collections.singletonList(expensive))
        .ifPresentOrElse(
                (night -> assertEquals(4, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        Selector.getRandomNight(Collections.singletonList(his))
        .ifPresentOrElse(
                (night -> assertEquals(3, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        Selector.getRandomNight(Collections.singletonList(hers))
        .ifPresentOrElse(
                (night -> assertEquals(4, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        Selector.getRandomNight(Collections.singletonList(both))
        .ifPresentOrElse(
                (night -> assertEquals(9, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        Selector.setRandomizer(new Random()::nextInt);

        Selector.getRandomNight(Arrays.asList(eCard,food,props))
        .ifPresentOrElse(
                (night -> assertEquals(5,(int)night.getId())),
                () -> fail("Could not get Night.")
        );
    }

    @AfterEach
    private void clearNights() {
        NightRepository.saveNights(Collections.emptyList());
    }
}
