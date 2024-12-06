package org.example.tfintechgradproject.repository;

import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.model.ActivityCategory;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ActivityRequestRepositoryTest {

    @Autowired
    private ActivityRequestRepository repository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityCategoryRepository activityCategoryRepository;

    private final WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 4326));

    private static final double ONE_DEGREE_OF_LATITUDE_IN_KM = 112000;

    @Test
    public void getClosest_basedOnRadiusAndCoordinates_shouldReturnClosestActivityRequests() throws ParseException {
        var activity = activityRepository.save(
                new Activity(0L, "AR1", activityCategoryRepository.save(new ActivityCategory(1L, "name"))
        ));

        var activityRequests = repository.saveAll(List.of(
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 1.0)"))
                        .address("AR1")
                        .participantsRequired(1)
                        .build(),
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 2.0)"))
                        .address("AR2")
                        .participantsRequired(1)
                        .build(),
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 3.0)"))
                        .address("AR3")
                        .participantsRequired(1)
                        .build(),
                ActivityRequest.builder()
                        .activity(activity)
                        .coordinates((Point) wktReader.read("POINT (13.13 4.0)"))
                        .address("AR4")
                        .participantsRequired(1)
                        .build()));

        var closestActivityRequests = repository.findActiveClosestActivityRequests(activity.getId(), "POINT(13.13 2.0)", ONE_DEGREE_OF_LATITUDE_IN_KM);

        assertEquals(3, closestActivityRequests.size());
        assertTrue(closestActivityRequests.contains(activityRequests.get(0)));
        assertTrue(closestActivityRequests.contains(activityRequests.get(1)));
        assertTrue(closestActivityRequests.contains(activityRequests.get(2)));


        closestActivityRequests = repository.findActiveClosestActivityRequests(activity.getId(), "POINT(13.13 2.0)", ONE_DEGREE_OF_LATITUDE_IN_KM-2000);

        assertEquals(1, closestActivityRequests.size());
        assertTrue(closestActivityRequests.contains(activityRequests.get(1)));
    }
}
