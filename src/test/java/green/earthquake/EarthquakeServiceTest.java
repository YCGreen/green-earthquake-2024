package green.earthquake;

import green.earthquake.json.FeatureCollection;
import green.earthquake.json.Properties;
import green.earthquake.json.Geometry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EarthquakeServiceTest {

    @Test
    void oneHour() {
        //given
        EarthquakeService service = new EarthquakeServiceFactory().getService();

        //when
        FeatureCollection collection = service.oneHour().blockingGet();

        //then
        Properties properties = collection.features[0].properties;
        assertNotNull(properties.getPlace());
        assertNotEquals(0, properties.getMag());
        assertNotEquals(0, properties.getTime());

        Geometry geo = collection.features[0].geometry;
        assertNotEquals(0, geo.getLatitude());
        assertNotEquals(0, geo.getLongitude());
    }

    @Test
    void thirtyDays() {
        //given
        EarthquakeService service = new EarthquakeServiceFactory().getService();

        //when
        FeatureCollection collection = service.thirtyDays().blockingGet();

        //then
        Properties properties = collection.features[0].properties;


        assertNotNull(properties.getPlace());
        assertNotEquals(0, properties.getMag());
        assertNotEquals(0, properties.getTime());
    }

}