import core.Night;
import core.Selector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SelectorTests {

    /**
     * It should return a random Night object from a predetermined list.
     */
    @Test
    void getRandomNight() throws Exception {

//        Make test nights
        List<Night> nights = new ArrayList<>();
        nights.add(new Night(1));
        nights.add(new Night(2));
        nights.add(new Night(3));

//        Instantiate selector with test nights
        Selector selector = new Selector(nights);

//        get random night
        Night randomNight = selector.getRandomNight();

//        confirm night is one of the ones we defined above
        assertEquals(
                randomNight,
                nights
                        .stream()
                        .filter((n) -> n.getId().equals(randomNight.getId()))
                        .findFirst()
                        .get());
    }

}
