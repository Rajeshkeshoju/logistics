package com.example.logstics.service;

import com.example.logstics.model.Journey;
import com.example.logstics.model.Postal;
import com.example.logstics.model.Route;
import com.example.logstics.model.TollPlaza;
import com.example.logstics.model.TollPlazaCsv;
import com.example.logstics.model.TollPlazaResponse;
import com.example.logstics.util.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RouterService {

    private List<TollPlazaCsv> getTollPlazaList(String filePath) {
        List<TollPlazaCsv> tollPlazaCsvList = new ArrayList<>();
        String[] HEADERS = {"longitude", "latitude", "toll_name", "geo_state"};

        try (Reader in = new FileReader(filePath)) {
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true)
                    .get().parse(in);

            for(CSVRecord record: csvRecords) {
                tollPlazaCsvList.add(new TollPlazaCsv(
                        Double.parseDouble(record.get(HEADERS[0])),
                        Double.parseDouble(record.get(HEADERS[1])),
                        record.get(HEADERS[2]),
                        record.get(HEADERS[3])
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tollPlazaCsvList;
    }

    private List<Postal> getCoordinates(String filePath, String pinCode) {
        List<Postal> postalList = new ArrayList<>();
        String[] HEADERS = {"circlename","regionname","divisionname","officename","pincode","officetype","delivery","district","statename","latitude","longitude"};

        try (Reader in = new FileReader(filePath)) {
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true)
                    .get().parse(in);

            for(CSVRecord record: csvRecords) {
                String pin = record.get(HEADERS[4]);
                if(pin.equals(pinCode)) {
                    postalList.add(new Postal(
                            record.get(HEADERS[0]),
                            record.get(HEADERS[1]),
                            record.get(HEADERS[2]),
                            record.get(HEADERS[3]),
                            Integer.parseInt(record.get(HEADERS[4])),
                            record.get(HEADERS[5]),
                            record.get(HEADERS[6]),
                            record.get(HEADERS[7]),
                            record.get(HEADERS[8]),
                            Double.parseDouble(record.get(HEADERS[9])),
                            Double.parseDouble(record.get(HEADERS[10]))
                    ));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return postalList;
    }

    private static final double THRESHOLD_DISTANCE = 0.1; // Define a threshold for close proximity to the route
    private static boolean isNearLine(double lat, double lon, double srcLat, double srcLon, double destLat, double destLon) {
        // Check if the toll plaza is within a small distance from the line connecting the two points
        double distanceToSource = calculateDistance(lat, lon, srcLat, srcLon);
        double distanceToDestination = calculateDistance(lat, lon, destLat, destLon);
        double lineDistance = calculateDistance(srcLat, srcLon, destLat, destLon);

        // Basic check to see if it's within the specified threshold
        return (distanceToSource + distanceToDestination <= lineDistance + THRESHOLD_DISTANCE);
    }

    public static List<TollPlazaCsv> findMinimalRoute(List<TollPlazaCsv> filteredTollPlazaCsvList,
                                                      double srcLat, double srcLon,
                                                      double destLat, double destLon) {
        List<TollPlazaCsv> route = new ArrayList<>();

        for (TollPlazaCsv toll : filteredTollPlazaCsvList) {
            if (isNearLine(toll.getLatitude(), toll.getLongitude(), srcLat, srcLon, destLat, destLon)) {
                route.add(toll);
            }
        }

        return route;
    }


    // Method to calculate distance using Haversine formula
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Earth's radius in kilometers
        final double R = 6371;

        // TODO: calculate distance from src to toll
        /***
         * Haversine Formula
         *
         * The distance d in kilometers between two points (lat1,long1) and (lat2,long2) is given by:
         *
         * d=R⋅c
         *
         * where:
         *
         *     R is the Earth's radius (approximately 6371 kilometers).
         *     c is the central angle in radians, calculated as:
         *
         * c=2⋅arcsin(sqrt(a))
         *
         * with
         *
         * a=sin^2((lat2−lat1)/2)+cos(lat1)⋅cos(lat2)⋅sin^2((long2−long1)/2)
         */

        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate differences
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        // Calculate distance
        return R * c; // Distance in kilometers
    }

    public TollPlazaResponse getTollPlazasInRoute(Journey journey) {
        TollPlazaResponse tollPlazaResponse = new TollPlazaResponse();

        if(!Utils.isValidPinCode(journey.getSourcePincode())) {
            tollPlazaResponse.setError("Invalid source PinCode");
            return tollPlazaResponse;
        }

        if(!Utils.isValidPinCode(journey.getDestinationPincode())) {
            tollPlazaResponse.setError("Invalid destination PinCode");
            return tollPlazaResponse;
        }

        if(journey.getSourcePincode().equals(journey.getDestinationPincode())) {
            tollPlazaResponse.setError("Source and destination PinCodes cannot be same");
            return tollPlazaResponse;
        }


        List<TollPlazaCsv> tollPlazaCsvList = getTollPlazaList("src/main/resources/toll_plaza_india.csv");
        List<Postal> sourcePostalList = getCoordinates("src/main/resources/pincode_to_coordinates.csv", journey.getSourcePincode());
        List<Postal> destinationPostalList = getCoordinates("src/main/resources/pincode_to_coordinates.csv", journey.getDestinationPincode());

        double sLatitude = sourcePostalList.stream()
                .mapToDouble(Postal::getLatitude)
                .average()
                .orElse(0.0);

        double sLongitude = sourcePostalList.stream()
                .mapToDouble(Postal::getLongitude)
                .average()
                .orElse(0.0);


        double dLatitude = destinationPostalList.stream()
                .mapToDouble(Postal::getLatitude)
                .average()
                .orElse(0.0);

        double dLongitude = destinationPostalList.stream()
                .mapToDouble(Postal::getLongitude)
                .average()
                .orElse(0.0);


        // TODO: filter

        double minLatitude = Math.min(sLatitude, dLatitude);
        double maxLatitude = Math.max(sLatitude, dLatitude);

        double minLongitude = Math.min(sLongitude, dLongitude);
        double maxLongitude = Math.max(sLongitude, dLongitude);

        List<TollPlazaCsv> filteredTollPlazaCsvList = tollPlazaCsvList.stream()
                .filter(toll -> (minLatitude <= toll.getLatitude() && toll.getLatitude() <= maxLatitude &&
                        minLongitude <= toll.getLongitude() && toll.getLongitude() <= maxLongitude))
                .toList();

        System.out.println(sLatitude + ", " + sLongitude);
        System.out.println(dLatitude + ", " + dLongitude);

        System.out.println();
        List<TollPlazaCsv> minimalRouteTolls = findMinimalRoute(filteredTollPlazaCsvList, sLatitude, sLongitude, dLatitude, dLongitude);
        minimalRouteTolls.stream().sorted(
                        Comparator.comparingDouble(TollPlazaCsv::getLatitude)
                        .thenComparingDouble(TollPlazaCsv::getLongitude)
                ).forEach(System.out::println);



        Route route = new Route();
        route.setSourcePincode(journey.getSourcePincode());
        route.setDestinationPincode(journey.getDestinationPincode());
        route.setDistanceInKm((int) calculateDistance(sLatitude, sLongitude, dLatitude, dLongitude));
        tollPlazaResponse.setRoute(route);
        List<TollPlaza> tollPlazaList = new ArrayList<>();
        minimalRouteTolls.forEach(t -> {
            TollPlaza plaza = new TollPlaza();
            plaza.setName(t.getTollName());
            plaza.setLatitude(t.getLatitude());
            plaza.setLongitude(t.getLongitude());
            plaza.setDistanceFromSource((int) calculateDistance(sLatitude, sLongitude, t.getLatitude(), t.getLongitude()));
            tollPlazaList.add(plaza);
        });

        tollPlazaResponse.setTollPlazaList(tollPlazaList.stream().sorted(Comparator.comparingInt(TollPlaza::getDistanceFromSource)).toList());


        return tollPlazaResponse;
    }
}
