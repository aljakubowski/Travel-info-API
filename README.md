# Travel-info-API
API provides some data about requested cities

Application is using some external APIs like:

- nominatim.openstreetmap.org
- metaweather.com
- restcountries.com

Based on above and internal service layer Application exposes an endpotins:

  - api/v1/info/{cityName}
  
      endpoint that returns basic city data as name, coordinates, country, borders and currency
      about requested city (works with major world cities)
 
  - api/v1/weather/{cityName}
   
      endpoint that returns basic geographic info and current weather data
      about requested city (works with major world cities)
    
  - api/v1/weather/{cityName}/{days}

      endpoint that returns basic geographic info, current weather data
      and weather-cast (for requested number of days: 1-5)
      about requested city (works with major world cities)
      
  - api/v1/cities/{city1Name}/{city2Name}

      endpoint that returns full names and coordinates of requested cities
      as well as distance and average flight time between these cities
      (works with most cities)
