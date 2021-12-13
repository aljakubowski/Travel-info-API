package com.alja.travelinfo.util;

public class CodeArchive {

    // == CITY SERVICE ==


//    public String getCityDetails(String cityName){
//
//        // build request
//        this.request = HttpRequest.newBuilder().uri(URI.create(apiAddress + cityName + format)).build();
//        // send request using client, async to not to block thread?
//        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()) // want receive response body as a string
//                .thenApply(HttpResponse :: body)    // then apply to previous result (above) - want to use body method from http response
//                .join();
//
//        response = response.substring(0, response.length()-1).substring(1);
//
//
//        String outputResult;
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//
//            City city = mapper.readValue(response, City.class);
//            outputResult = city.getDisplay_name() + "\nlatitude: " + city.getLat() + "\nlongitude: " + city.getLon();
//            log.info(outputResult);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            outputResult = null;
//        }
//
//        return outputResult;
//    }

    //    public String getTwoCitiesDetails(String cityName1, String cityName2){
//
//        String outputResult;
//
//        request = HttpRequest.newBuilder().uri(URI.create(apiAddress + cityName1 + format)).build();
//        String response1 = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse :: body)
//                .join();
//        response1 = response1.substring(0, response1.length()-1).substring(1);
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            City city1 = mapper.readValue(response1, City.class);
//            outputResult = city1.getDisplay_name() + "\nlatitude: " + city1.getLat() + "\nlongitude: " + city1.getLon();
//            log.info(response1);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            outputResult = null;
//        }
//
//
//        request = HttpRequest.newBuilder().uri(URI.create(apiAddress + cityName2 + format)).build();
//        String response2 = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse :: body)
//                .join();
//        response2 = response2.substring(0, response2.length()-1).substring(1);
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            City city2 = mapper.readValue(response2, City.class);
//            outputResult += "\n" + city2.getDisplay_name() + "\nlatitude: " + city2.getLat() + "\nlongitude: " + city2.getLon();
//            log.info(response2);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            outputResult = null;
//        }
//
//        return  outputResult;
//    }




//    public String getCityDetails2(String cityName) {
//        String urlOpenstreetmap = "https://nominatim.openstreetmap.org/?city=" + cityName + "&format=json&limit=1";
////        RestTemplate restTemplate = new RestTemplate();
////        Object city = restTemplate.getForEntity(urlOpenstreetmap, City.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String outputResult;
//        try{
//            City city = objectMapper.readValue(urlOpenstreetmap, City.class);
//            outputResult = city.getDisplay_name() + "\nlatitude: " + city.getLat() + "\nlongitude: " + city.getLon();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            outputResult = null;
//        }
//
//    return outputResult;
//    }

//            if (lat > 0) {
//        result = String.valueOf(lat) + "N, ";
//    } else {
//        result = String.valueOf(lat) + "S, ";
//    }
//        if (lon > 0) {
//        result += String.valueOf(lon) + "E";
//    } else {
//        result += String.valueOf(lon) + "W";
//    }



    // == CITY CONTROLLER ==

//    @GetMapping(path = "/v2/cities/{cityName}")
//    public String getCityDetails2(@PathVariable("cityName") String cityName){
//        return cityService.getCityDetails2(cityName);
//    }


    // ===


    // class that handles nominatim.openstreetmap Service
//    @Slf4j
//    @Service
//    public class CityService {
//
//        // create client
//        private final HttpClient client = HttpClient.newHttpClient();
//
//        private final String apiAddress = "https://nominatim.openstreetmap.org/?city=";
//        private final String format = "&format=json&limit=1";
//
//        private final TwoCitiesInfo twoCitiesInfo;
//
//        @Autowired
//        public CityService(TwoCitiesInfo twoCitiesInfo) {
//            this.twoCitiesInfo = twoCitiesInfo;
//        }
//
//
//
//        public String getCityDetails(String cityName) {
//            String outputResult;
//            City city = createRequest(cityName);
//
//            outputResult = city.getDisplay_name() + "\nlatitude: " + city.getLat() + "\nlongitude: " + city.getLon();
//            return outputResult;
//        }
//
//
//        public TwoCitiesInfo getTravelInfo(String cityName1, String cityName2){
//
//            City city1 = createRequest(cityName1);
//            City city2 = createRequest(cityName2);
//
//            String [] coordCity1 = {city1.getLat(), city1.getLon()};
//            String [] coordCity2 = {city2.getLat(), city2.getLon()};
//            double distance = distanceBetween(coordCity1, coordCity2);
//
//            twoCitiesInfo.setStartCity(city1.getDisplay_name());
//            twoCitiesInfo.setDestinationCity(city2.getDisplay_name());
//
//            twoCitiesInfo.setStartCityCoord(convertCoord(coordCity1));
//            twoCitiesInfo.setDestinationCityCoord(convertCoord(coordCity2));
//
//            twoCitiesInfo.setDistanceInKm(distance);
//            twoCitiesInfo.setAverageFlightTime(averageFlightTime(distance));
//
//            return twoCitiesInfo;
//
//        }
//
//
//
//        public String getTwoCitiesDetails(String cityName1, String cityName2) {
//
//            String outputResult ="";
//
//            City city1 = createRequest(cityName1);
//            City city2 = createRequest(cityName2);
//
////        outputResult = city1.getDisplay_name() + "\nlatitude: " + city1.getLat() + "\nlongitude: " + city1.getLon();
////        outputResult += "\n" + city2.getDisplay_name() + "\nlatitude: " + city2.getLat() + "\nlongitude: " + city2.getLon();
//
//            String [] coordCity1 = {city1.getLat(), city1.getLon()};
//            String [] coordCity2 = {city2.getLat(), city2.getLon()};
//            double distance = distanceBetween(coordCity1, coordCity2);
//
//
//            return outputResult +"Distance between:\n" + city1.getDisplay_name() + "\nand\n" + city2.getDisplay_name() +
//                    "\nis: " + distance +" km";
//        }
//
//
//
//
//        private double distanceBetween(String [] coord1, String [] coord2 ){
//
//            double lat1 = Double.parseDouble(coord1[0]);
//            double lon1 = Double.parseDouble(coord1[1]);
//
//            double lat2 = Double.parseDouble(coord2[0]);
//            double lon2 = Double.parseDouble(coord2[1]);
//
//            double distance;
//
//            distance = (Math.sqrt(Math.pow((lat2 - lat1), 2)
//                    + Math.pow((Math.cos(lat1*Math.PI/180) * (lon2-lon1)), 2) ))
//                    * (40075.704/360);
//            distance = Math.round(distance*100.0)/100.0;
//            return distance;
//        }
//
//        private String averageFlightTime(double distance){
//
//            double averagePlaneSpeed = 900.0d;
//            int starAndLandTime = 1;
//            double time = distance/averagePlaneSpeed;
//
//            int hours = (int) time;
//            int minutes = (int)Math.round(time% 1 * 60);
//
//            return hours + starAndLandTime + "h and " + minutes + "min";
//        }
//
//
//        private City createRequest(String cityName) {
//
//            City city = new City();
//
//            HttpRequest request = HttpRequest.newBuilder().uri(URI.create( (apiAddress + cityName + format).replace(" ", "%20") )).build();
//
//            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                    .thenApply(HttpResponse::body)
//                    .join();
//
//            log.info("==== >>>> request is: " + apiAddress + cityName + format);
//
//
//            response = response.substring(0, response.length() - 1).substring(1);
//
//            // TODO exception message: "City was not found"
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                city = mapper.readValue(response, City.class);
//                log.info(response);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return city;
//        }
//
//        private String convertCoord(String[] coords) {
//
//            double lat = Double.parseDouble(coords[0]);
//            double lon = Double.parseDouble(coords[1]);
//
//            lat = Math.round(lat * 100.0) / 100.0;
//            lon = Math.round(lon * 100.0) / 100.0;
//
//            return "lat: " + String.valueOf(lat) + ", lon: " + String.valueOf(lon);
//        }
//
//    }

}
