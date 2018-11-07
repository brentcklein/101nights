import core.DataException;
import core.Mode;
import core.Night;
import core.Selector;
import static core.Cost.*;
import static core.Partner.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectorTests {

    private Selector selector;

    @BeforeAll
    void getSelector() {
        try {
            this.selector = new Selector(Mode.TEST);
        } catch (DataException de) {
            fail("Could not connect to database.");
        }
    }

    /**
     * It should return a random Night object from a predetermined list.
     */
    @Test
    void getRandomNight() {

//        Test no nights at all
        assertEquals(0,selector.getRepository().getNights().size());
        selector.getRandomNight()
        .ifPresentOrElse(
                (n)->fail("There should not be any Nights."),
                ()->{}
        );


//        Make test nights
        selector.getRepository().saveNights(Arrays.asList(
            new Night(1),
            new Night(2),
            new Night(3)
        ));

        assertEquals(3,selector.getRepository().getNights().size());

        selector.setRandomizer(integer -> 1);

//        get random night
        selector.getRandomNight()
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
    void getFilteredNight() {
        selector.getRepository().saveNights(Arrays.asList(
                new Night(1),
                new Night(2),
                new Night(3),
                new Night(4),
                new Night(5)
        ));

        assertEquals(5,selector.getRepository().getNights().size());

        selector.setRandomizer(i->0);

//        filter out all odd ids and all ids <= 2 (should leave only id == 4)
        List<Predicate<Night>> predicates = new ArrayList<>();
        predicates.add(night->night.getId() % 2 == 0);
        selector.getRandomNight(predicates)
        .ifPresentOrElse(
                (n)->assertEquals(2, (int)n.getId()),
                ()->fail("Could not get Night.")
        );


//        Test empty list of predicates as indicating a lack of filters
        selector.getRandomNight(Collections.emptyList())
        .ifPresentOrElse(
                (randomNight)->assertEquals(1,(int)randomNight.getId()),
                ()->fail("Could not get Night.")
        );

        selector.setRandomizer(new Random()::nextInt);

//        Test multiple predicates. Only remaining id should be 4.
        predicates.add(night->night.getId() > 2);
        selector.getRandomNight(predicates)
        .ifPresentOrElse(
                (randomNight)->assertEquals(4, (int)randomNight.getId()),
                ()->fail("Could not get Night.")
        );

        predicates.add(n->n.getId()>5);
        selector.getRandomNight(predicates)
        .ifPresentOrElse(
                (n)->fail("Found Night when should have returned none."),
                ()->{}
        );
    }

    @Test
    void getComplexFilteredNights() {
        selector.getRepository().saveNights(Arrays.asList(
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

        assertEquals(10, selector.getRepository().getNights().size());

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

        selector.setRandomizer(i->1);

        selector.getRandomNight(Collections.singletonList(open))
        .ifPresentOrElse(
                (night -> assertEquals(3, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        selector.getRandomNight(Collections.singletonList(eCard))
        .ifPresentOrElse(
                (night -> assertEquals(3, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        selector.getRandomNight(Arrays.asList(free,travel))
        .ifPresent(
                (night -> fail("Should not have found Night."))
        );

        selector.getRandomNight(Collections.singletonList(food))
        .ifPresentOrElse(
                (night -> assertEquals(5, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        selector.getRandomNight(Collections.singletonList(expensive))
        .ifPresentOrElse(
                (night -> assertEquals(4, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        selector.getRandomNight(Collections.singletonList(his))
        .ifPresentOrElse(
                (night -> assertEquals(3, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        selector.getRandomNight(Collections.singletonList(hers))
        .ifPresentOrElse(
                (night -> assertEquals(4, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        selector.getRandomNight(Collections.singletonList(both))
        .ifPresentOrElse(
                (night -> assertEquals(9, (int)night.getId())),
                () -> fail("Could not get Night.")
        );

        selector.setRandomizer(new Random()::nextInt);

        selector.getRandomNight(Arrays.asList(eCard,food,props))
        .ifPresentOrElse(
                (night -> assertEquals(5,(int)night.getId())),
                () -> fail("Could not get Night.")
        );
    }

    @AfterEach
    private void clearNights() {
        selector.getRepository().saveNights(Collections.emptyList());
    }
}
