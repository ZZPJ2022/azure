import com.facecloud.facecloudserverapp.entity.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    Location location;

    @BeforeEach
    void initializeLocation()
    {
        location = new Location(0L, "user", 10, 15, LocalDateTime.of(2022, 01, 01, 0,0));
    }

    @Test
    @DisplayName("checking initialization of location")
    void ensureLocationInitializationWorks()
    {
        assertEquals(location.getId(), 0L);
        assertEquals(location.getUserName(), "user");
        assertEquals(location.getLatitude(), 10);
        assertEquals(location.getLongitude(), 15);
        assertEquals(location.getTime(), LocalDateTime.of(2022, 01, 01, 0,0));
    }

}
