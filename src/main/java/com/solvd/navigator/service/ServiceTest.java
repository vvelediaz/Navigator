package com.solvd.navigator.service;

import com.solvd.navigator.App;
import com.solvd.navigator.dao.*;
import com.solvd.navigator.factory.AbstractFactory;
import com.solvd.navigator.factory.DaoType;
import com.solvd.navigator.factory.FactoryGenerator;
import com.solvd.navigator.factory.FactoryType;
import com.solvd.navigator.model.*;
import com.solvd.navigator.service.imple.LocationService;
import com.solvd.navigator.service.imple.RouteService;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServiceTest {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(App.class);
    private static final Random random = new Random();
    private  static AbstractFactory daoFactory = FactoryGenerator.getFactory(FactoryType.JDBC);
    private static final double EARTH_RADIUS = 6371;

    public static void DLTest(){

        IDriverLicenseDAO driverLicenseDAO = daoFactory.getDao(DaoType.DRIVER_LICENSE);
        DriverLicense driverLicense = createDriverLicense();
        DriverLicense driverLicense2 = createDriverLicense();

        try {
            driverLicenseDAO.insert(driverLicense);
            logger.info(driverLicenseDAO.getById(driverLicense.getId()).toString() + "\n");

            driverLicense.setNumber(Math.abs(random.nextInt()));
            driverLicenseDAO.update(driverLicense);
            logger.info(driverLicenseDAO.getById(driverLicense.getId()).toString() + "\n");

            driverLicenseDAO.insert(driverLicense2);
            logger.info(driverLicenseDAO.getAll().toString() + "\n");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            driverLicenseDAO.delete(driverLicense.getId());
            driverLicenseDAO.delete(driverLicense2.getId());
        }
    }

    public static void TransportationTypeTest(){


        ITransportationTypeDAO transportationTypeDAO = daoFactory.getDao(DaoType.TRANSPORTATION_TYPE);

        TransportationType transportationType = createTransportationType("Bus");
        TransportationType transportationType2 = createTransportationType("Plane");

        try {
            transportationTypeDAO.insert(transportationType);
            logger.info(transportationTypeDAO.getById(transportationType.getId()).toString() + "\n");

            transportationType.setType("Train");
            transportationTypeDAO.update(transportationType);
            logger.info(transportationTypeDAO.getById(transportationType.getId()).toString() + "\n");

            transportationTypeDAO.insert(transportationType2);
            logger.info(transportationTypeDAO.getAll().toString() + "\n");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            transportationTypeDAO.delete(transportationType.getId());
            transportationTypeDAO.delete(transportationType2.getId());
        }
    }

    public static void ReviewTest(){
        IReviewDAO reviewDAO = daoFactory.getDao(DaoType.REVIEW);
        ILocationDAO locationDAO = daoFactory.getDao(DaoType.LOCATION);

        Location location = createLocation("Los Angeles", new Coordinate(34.0522, -118.2437));

        Review review = createReview("content from review", location);
        Review review2 = createReview("content from review2", location);

        try {
            locationDAO.insert(location);
            reviewDAO.insert(review);
            logger.info(reviewDAO.getById(review.getId()).toString() + "\n");

            review.setContent("updated content from review");
            System.out.println();
            reviewDAO.update(review);
            logger.info(reviewDAO.getById(review.getId()).toString() + "\n");

            reviewDAO.insert(review2);
            logger.info(reviewDAO.getAll().toString() + "\n");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            reviewDAO.delete(review.getId());
            reviewDAO.delete(review2.getId());
            locationDAO.delete(location.getId());
        }
    }

    public static void PersonTest(){

        //Test PersonDAO without DriverLicense
        IPersonDAO personDAO = daoFactory.getDao(DaoType.PERSON);


        Person person = createPerson("John");
        Person person2 = createPerson("Jack");


        try{

            personDAO.insert(person);
            logger.info(personDAO.getById(person.getId()).toString() + "\n");

            person.setName("Bob");
            personDAO.update(person);
            logger.info(personDAO.getById(person.getId()).toString() + "\n");

            personDAO.insert(person2);
            logger.info(personDAO.getAll().toString() + "\n");
        }catch (Exception e){
            logger.error(e);
        }
        finally {
            personDAO.delete(person.getId());
            personDAO.delete(person2.getId());
        }
    }

    public static void PersonDriverTest(){
        // Test PersonDAO with DriverLicense
        IPersonDAO personDAO = daoFactory.getDao(DaoType.PERSON);
        IDriverLicenseDAO driverLicenseDAO = daoFactory.getDao(DaoType.DRIVER_LICENSE);

        DriverLicense driverLicense = createDriverLicense();
        DriverLicense driverLicense2 = createDriverLicense();

        Person driver = createDriver("John", driverLicense);
        Person driver2 = createDriver("Jack", driverLicense2);

        try{
            driverLicenseDAO.insert(driverLicense);
            driverLicenseDAO.insert(driverLicense2);

            personDAO.insertDriver(driver);
            logger.info(personDAO.getDriverById(driver.getId()).toString() + "\n");

            driver.setName("Bob");
            personDAO.updateDriver(driver);
            logger.info(personDAO.getDriverById(driver.getId()).toString() + "\n");

            personDAO.insertDriver(driver2);
            logger.info(personDAO.getAllDrivers().toString() + "\n");
        }catch (Exception e){
            logger.error(e);
        }finally {
            personDAO.delete(driver.getId());
            personDAO.delete(driver2.getId());
            driverLicenseDAO.delete(driverLicense.getId());
            driverLicenseDAO.delete(driverLicense2.getId());
        }
    }

    public static void LocationTest() {
        // Test LocationDAO, route list is initialized in service layer
        //ILocationDAO locationDAO = daoFactory.getDao(DaoType.LOCATION);

        IService<Location> locationService = new LocationService();

        Location location1 = createLocation("New York", new Coordinate(40.7128, 74.0060));
        Location location2 = createLocation("Los Angeles", new Coordinate(34.0522, 118.2437));

        //locationDAO.insert(location1);

        try {
            locationService.insert(location1);
            logger.info(locationService.getById(location1.getId()).toString() + "\n");

            location1.setName("Chicago");
            locationService.update(location1);
            logger.info(locationService.getById(location1.getId()).toString() + "\n");

            locationService.insert(location2);
            logger.info(locationService.getAll().toString() + "\n");
        }catch (Exception e){
            logger.error(e);
        }finally {
            locationService.delete(location1.getId());
            locationService.delete(location2.getId());
        }
    }

    public static void TransportationTest(){
        // Test TransportationDAO
        ITransportationTypeDAO transportationTypeDAO = daoFactory.getDao(DaoType.TRANSPORTATION_TYPE);
        ITransportationDAO transportationDAO = daoFactory.getDao(DaoType.TRANSPORTATION);
        IDriverLicenseDAO driverLicenseDAO = daoFactory.getDao(DaoType.DRIVER_LICENSE);
        IPersonDAO driverDAO = daoFactory.getDao(DaoType.PERSON);

        DriverLicense driverLicense = createDriverLicense();
        DriverLicense driverLicense2 = createDriverLicense();

        Person driver = createDriver("John", driverLicense);
        Person driver2 = createDriver("Jack", driverLicense2);

        TransportationType transportationType = createTransportationType("Bus");
        TransportationType transportationType2 = createTransportationType("Plane");

        Transportation transportation1 = createTransportation(driver, transportationType);
        Transportation transportation2 = createTransportation(driver2, transportationType2);

        try {
            transportationTypeDAO.insert(transportationType);
            transportationTypeDAO.insert(transportationType2);
            driverLicenseDAO.insert(driverLicense);
            driverLicenseDAO.insert(driverLicense2);
            driverDAO.insert(driver);
            driverDAO.insert(driver2);

            transportationDAO.insert(transportation1);
            logger.info(transportationDAO.getById(transportation1.getId()).toString() + "\n");

            transportation1.setVehicleNumber(102);
            transportationDAO.update(transportation1);
            logger.info(transportationDAO.getById(transportation1.getId()).toString() + "\n");

            transportationDAO.insert(transportation2);
            logger.info(transportationDAO.getAll().toString() + "\n");
        }catch (Exception e){
            logger.error(e);
        }finally {
            transportationDAO.delete(transportation1.getId());
            transportationDAO.delete(transportation2.getId());
            driverDAO.delete(driver.getId());
            driverDAO.delete(driver2.getId());
            driverLicenseDAO.delete(driverLicense.getId());
            driverLicenseDAO.delete(driverLicense2.getId());
            transportationTypeDAO.delete(transportationType.getId());
            transportationTypeDAO.delete(transportationType2.getId());
        }
    }

    public static void RouteTest(){
        IDriverLicenseDAO driverLicenseDAO = daoFactory.getDao(DaoType.DRIVER_LICENSE);
        DriverLicense driverLicense = createDriverLicense();
        DriverLicense driverLicense2 = createDriverLicense();

        ITransportationTypeDAO transportationTypeDAO = daoFactory.getDao(DaoType.TRANSPORTATION_TYPE);
        TransportationType transportationType = createTransportationType("Bus");
        TransportationType transportationType2 = createTransportationType("Plane");

        ILocationDAO locationDAO = daoFactory.getDao(DaoType.LOCATION);
        Location locationA = createLocation("Chicago", new Coordinate(41.8781, 87.6298));
        Location locationB = createLocation("San Francisco", new Coordinate(37.7749, 122.4194));
        Location locationC = createLocation("Houston", new Coordinate(29.7604, 95.3698));

        IPersonDAO personDAO = daoFactory.getDao(DaoType.PERSON);
        Person driver = createDriver("Charlie", driverLicense);
        Person driver2 = createDriver("Jack", driverLicense2);

        ITransportationDAO transportationDAO = daoFactory.getDao(DaoType.TRANSPORTATION);
        Transportation transportation = createTransportation(driver, transportationType);
        Transportation transportation2 = createTransportation(driver2, transportationType2);

        //IRouteDAO routeDAO = daoFactory.getDao(DaoType.ROUTE);
        IService<Route> routeService = new RouteService();
        Route route = createRoute(locationA, locationB, transportation,100, 1000);
        Route route2 = createRoute(locationA, locationC, transportation2,250, 1500);

        try{
            driverLicenseDAO.insert(driverLicense);
            driverLicenseDAO.insert(driverLicense2);
            transportationTypeDAO.insert(transportationType);
            transportationTypeDAO.insert(transportationType2);
            locationDAO.insert(locationA);
            locationDAO.insert(locationB);
            locationDAO.insert(locationC);
            personDAO.insertDriver(driver);
            personDAO.insertDriver(driver2);
            transportationDAO.insert(transportation);
            transportationDAO.insert(transportation2);

            routeService.insert(route);
            logger.info(routeService.getById(route.getId()) + "\n");

            route.setLocationA(locationC);
            routeService.update(route);
            logger.info(routeService.getById(route.getId()) + "\n");

            routeService.insert(route2);
            logger.info(routeService.getAll() + "\n");
        }catch (Exception e) {
            logger.error(e);
        }finally {
            routeService.delete(route.getId());
            routeService.delete(route2.getId());
            transportationDAO.delete(transportation.getId());
            transportationDAO.delete(transportation2.getId());
            personDAO.delete(driver.getId());
            personDAO.delete(driver2.getId());
            locationDAO.delete(locationA.getId());
            locationDAO.delete(locationB.getId());
            locationDAO.delete(locationC.getId());
            transportationTypeDAO.delete(transportationType.getId());
            transportationTypeDAO.delete(transportationType2.getId());
            driverLicenseDAO.delete(driverLicense.getId());
            driverLicenseDAO.delete(driverLicense2.getId());
        }
    }

    //    // Helpers for creating model objects
    private static DriverLicense createDriverLicense(){
        long id = Math.abs(random.nextLong());
        int number = Math.abs(random.nextInt());
        DriverLicense driverLicense = new DriverLicense();
        driverLicense.setId(id);
        driverLicense.setNumber(number);
        return driverLicense;
    }

    private static Person createDriver(String name, DriverLicense driverLicense){
        long id = Math.abs(random.nextLong());
        Person driver = new Person();
        driver.setId(id);
        driver.setName(name);
        driver.setDriverLicense(driverLicense);
        return driver;
    }

    private static TransportationType createTransportationType(String type){
        long id = Math.abs(random.nextLong());
        TransportationType transportationType = new TransportationType();
        transportationType.setId(id);
        transportationType.setType(type);
        return transportationType;
    }

    private static Location createLocation(String name, Coordinate coordinate){
        long id = Math.abs(random.nextLong());
        Location location = new Location();
        location.setId(id);
        location.setName(name);
        location.setCoordinate(coordinate);
        return location;
    }

    private static Review createReview(String content, Location location){
        long id = Math.abs(random.nextLong());
        Review review = new Review();
        review.setId(id);
        review.setContent(content);
        review.setLocation(location);
        return review;
    }

    private static Person createPerson(String name){
        long id = Math.abs(random.nextLong());
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        return person;
    }

    private static Transportation createTransportation(Person driver, TransportationType transportationType){
        long id = Math.abs(random.nextLong());
        Transportation transportation = new Transportation();
        transportation.setId(id);
        transportation.setCost(100);
        transportation.setVehicleNumber(101);
        transportation.setDriver(driver);
        transportation.setTransportationType(transportationType);
        return transportation;
    }

    private static Route createRoute(Location locationA, Location locationB, Transportation transportation,  int duration, int distance){
        long id = Math.abs(random.nextLong());
        Route route = new Route();
        route.setId(id);
        route.setLocationA(locationA);
        route.setLocationB(locationB);
        route.setTransportation(transportation);
        route.setDuration(duration);
        route.setDistance(distance);
        return route;
    }

    public static void addCompleteDataToDatabase(){
        //*** this method will add data to the database to use the Dijkstra Algorithm ***.
        // it is recommended to erase all the data inside the database to avoid duplicates.

        List<Location> locationList = new ArrayList<>();
        List<Route> routeList = new ArrayList<>();

        IDriverLicenseDAO driverLicenseDAO = daoFactory.getDao(DaoType.DRIVER_LICENSE);
        DriverLicense driverLicense = createDriverLicense();
        driverLicense.setId(1L);
        DriverLicense driverLicense2 = createDriverLicense();
        driverLicense2.setId(2L);

        ITransportationTypeDAO transportationTypeDAO = daoFactory.getDao(DaoType.TRANSPORTATION_TYPE);
        TransportationType transportationType = createTransportationType("Bus");
        transportationType.setId(1L);
        TransportationType transportationType2 = createTransportationType("Plane");
        transportationType2.setId(2L);
        TransportationType transportationType3 = createTransportationType("Boat");
        transportationType3.setId(3L);
        TransportationType transportationType4 = createTransportationType("Taxi");
        transportationType4.setId(4L);
        TransportationType transportationType5 = createTransportationType("Train");
        transportationType5.setId(5L);

        ILocationDAO locationDAO = daoFactory.getDao(DaoType.LOCATION);
        Location locationA = createLocation("Sacramento", new Coordinate(38.5816, 121.4944));
        locationA.setId(1L);
        Location locationB = createLocation("Salt Lake City", new Coordinate(40.7608, 111.8910));
        locationB.setId(2L);
        Location locationC = createLocation("Austin", new Coordinate(30.2672, 97.7431));
        locationC.setId(3L);
        Location locationD = createLocation("Washington DC", new Coordinate(38.9072, 77.0369));
        locationD.setId(4L);
        Location locationE = createLocation("Miami", new Coordinate(25.7617, 80.1918));
        locationE.setId(5L);
        Location locationF = createLocation("Charleston", new Coordinate(32.7765, 79.9311));
        locationF.setId(6L);
        locationList.add(locationA);
        locationList.add(locationB);
        locationList.add(locationC);
        locationList.add(locationD);
        locationList.add(locationE);
        locationList.add(locationF);


        IPersonDAO personDAO = daoFactory.getDao(DaoType.PERSON);
        Person driver = createDriver("Charlie", driverLicense);
        driver.setId(1L);
        Person driver2 = createDriver("Jack", driverLicense2);
        driver2.setId(2L);

        ITransportationDAO transportationDAO = daoFactory.getDao(DaoType.TRANSPORTATION);
        Transportation transportation = createTransportation(driver, transportationType);
        transportation.setId(1L);
        Transportation transportation2 = createTransportation(driver2, transportationType2);
        transportation2.setId(2L);
        Transportation transportation3 = createTransportation(driver, transportationType3);
        transportation3.setId(3L);
        Transportation transportation4 = createTransportation(driver2, transportationType4);
        transportation4.setId(4L);
        Transportation transportation5 = createTransportation(driver, transportationType5);
        transportation5.setId(5L);




        //IRouteDAO routeDAO = daoFactory.getDao(DaoType.ROUTE);
        IService<Route> routeService = new RouteService();
        Route route = createRoute(locationA, locationB, transportation,7, calculateDistance(locationA.getCoordinate().getLatitude(), locationA.getCoordinate().getLongitude(), locationB.getCoordinate().getLatitude(), locationB.getCoordinate().getLongitude()));
        route.setId(1L);
        Route route2 = createRoute(locationA, locationC, transportation,12, calculateDistance(locationA.getCoordinate().getLatitude(), locationA.getCoordinate().getLongitude(), locationC.getCoordinate().getLatitude(), locationC.getCoordinate().getLongitude()));
        route2.setId(2L);
        Route route3 = createRoute(locationB, locationC, transportation4,2, calculateDistance(locationB.getCoordinate().getLatitude(), locationB.getCoordinate().getLongitude(), locationC.getCoordinate().getLatitude(), locationC.getCoordinate().getLongitude()));
        route3.setId(3L);
        Route route4 = createRoute(locationD, locationE, transportation2,4, calculateDistance(locationD.getCoordinate().getLatitude(), locationD.getCoordinate().getLongitude(), locationE.getCoordinate().getLatitude(), locationE.getCoordinate().getLongitude()));
        route4.setId(4L);
        Route route5 = createRoute(locationB, locationD, transportation3,9, calculateDistance(locationB.getCoordinate().getLatitude(), locationB.getCoordinate().getLongitude(), locationD.getCoordinate().getLatitude(), locationD.getCoordinate().getLongitude()));
        route5.setId(5L);
        Route route6 = createRoute(locationC, locationE, transportation,10, calculateDistance(locationC.getCoordinate().getLatitude(), locationC.getCoordinate().getLongitude(), locationE.getCoordinate().getLatitude(), locationE.getCoordinate().getLongitude()));
        route6.setId(6L);
        Route route7 = createRoute(locationE, locationF, transportation4,5, calculateDistance(locationE.getCoordinate().getLatitude(), locationE.getCoordinate().getLongitude(), locationF.getCoordinate().getLatitude(), locationF.getCoordinate().getLongitude()));
        route7.setId(7L);
        Route route8 = createRoute(locationD, locationF, transportation2,1, calculateDistance(locationD.getCoordinate().getLatitude(), locationD.getCoordinate().getLongitude(), locationF.getCoordinate().getLatitude(), locationF.getCoordinate().getLongitude()));
        route8.setId(8L);

        Route route9 = createRoute(locationB, locationA, transportation,7, calculateDistance(locationA.getCoordinate().getLatitude(), locationA.getCoordinate().getLongitude(), locationB.getCoordinate().getLatitude(), locationB.getCoordinate().getLongitude()));
        route9.setId(9L);
        Route route10 = createRoute(locationC, locationA, transportation,12, calculateDistance(locationA.getCoordinate().getLatitude(), locationA.getCoordinate().getLongitude(), locationC.getCoordinate().getLatitude(), locationC.getCoordinate().getLongitude()));
        route10.setId(10L);
        Route route11 = createRoute(locationC, locationB, transportation4,2, calculateDistance(locationB.getCoordinate().getLatitude(), locationB.getCoordinate().getLongitude(), locationC.getCoordinate().getLatitude(), locationC.getCoordinate().getLongitude()));
        route11.setId(11L);
        Route route12 = createRoute(locationE, locationD, transportation2,4, calculateDistance(locationD.getCoordinate().getLatitude(), locationD.getCoordinate().getLongitude(), locationE.getCoordinate().getLatitude(), locationE.getCoordinate().getLongitude()));
        route12.setId(12L);
        Route route13 = createRoute(locationD, locationB, transportation3,9, calculateDistance(locationB.getCoordinate().getLatitude(), locationB.getCoordinate().getLongitude(), locationD.getCoordinate().getLatitude(), locationD.getCoordinate().getLongitude()));
        route13.setId(13L);
        Route route14 = createRoute(locationE, locationC, transportation,10, calculateDistance(locationC.getCoordinate().getLatitude(), locationC.getCoordinate().getLongitude(), locationE.getCoordinate().getLatitude(), locationE.getCoordinate().getLongitude()));
        route14.setId(14L);
        Route route15 = createRoute(locationF, locationE, transportation4,5, calculateDistance(locationE.getCoordinate().getLatitude(), locationE.getCoordinate().getLongitude(), locationF.getCoordinate().getLatitude(), locationF.getCoordinate().getLongitude()));
        route15.setId(15L);
        Route route16 = createRoute(locationF, locationD, transportation2,1, calculateDistance(locationD.getCoordinate().getLatitude(), locationD.getCoordinate().getLongitude(), locationF.getCoordinate().getLatitude(), locationF.getCoordinate().getLongitude()));
        route16.setId(16L);

        routeList.add(route);
        routeList.add(route2);
        routeList.add(route3);
        routeList.add(route4);
        routeList.add(route5);
        routeList.add(route6);
        routeList.add(route7);
        routeList.add(route8);
        routeList.add(route9);
        routeList.add(route10);
        routeList.add(route11);
        routeList.add(route12);
        routeList.add(route13);
        routeList.add(route14);
        routeList.add(route15);
        routeList.add(route16);
        try{

            driverLicenseDAO.insert(driverLicense);
            driverLicenseDAO.insert(driverLicense2);

            transportationTypeDAO.insert(transportationType);
            transportationTypeDAO.insert(transportationType2);
            transportationTypeDAO.insert(transportationType3);
            transportationTypeDAO.insert(transportationType4);
            transportationTypeDAO.insert(transportationType5);

            personDAO.insertDriver(driver);
            personDAO.insertDriver(driver2);

            transportationDAO.insert(transportation);
            transportationDAO.insert(transportation2);
            transportationDAO.insert(transportation3);
            transportationDAO.insert(transportation4);
            transportationDAO.insert(transportation5);

            for(Location loc: locationList){

                locationDAO.insert(loc);
            }
            for(Route r: routeList){
                routeService.insert(r);
            }


        }catch (Exception e) {
            logger.error(e);
//        }finally {
//
//
//
//
//            for(Route r: routeList){
//                routeService.delete(r.getId());
//            }
//
//            transportationDAO.delete(transportation.getId());
//            transportationDAO.delete(transportation2.getId());
//            personDAO.delete(driver.getId());
//            personDAO.delete(driver2.getId());
//
//            for(Location loc: locationList){
//                locationDAO.delete(loc.getId());
//            }
//
//            transportationTypeDAO.delete(transportationType.getId());
//            transportationTypeDAO.delete(transportationType2.getId());
//            driverLicenseDAO.delete(driverLicense.getId());
//            driverLicenseDAO.delete(driverLicense2.getId());
        }
    }

    public static int calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;
        return (int) Math.round(distance);
    }
}



