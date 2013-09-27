package suite;

import com.tfl.api.stories.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    NWP925ServiceStatusUpdateTest.class,
    NWP2589BusServiceStatusTest.class,
    NWP2853StationStatusTest.class,
    NWP2589RoadDisruptionStatusTest.class,
    NWP2589CableCarServiceStatusTest.class,
    NWP2589TramServiceStatusTest.class,
    NWP2589RiverBusServiceStatusTest.class

})
public class AllStories {

}
