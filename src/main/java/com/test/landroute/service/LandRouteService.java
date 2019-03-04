package com.test.landroute.service;

import com.test.landroute.model.Country;
import com.test.landroute.model.LandRouteResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Land route finding service. Has a method for finding a path from origin country to destination country.
 */
@Service
@AllArgsConstructor
public class LandRouteService {

    private final CountryService countryService;

    /**
     * Method that finds the path from origin to destination, using BFS for unweighted graph.
     *
     * @param origin
     * @param destination
     * @return the route from origin to destination
     */
    public LandRouteResponse getDirections(Country origin, Country destination) {
        List<Country> visitted = new ArrayList<>();
        Map<Country, Country> nextCountryMap = new HashMap<>();
        List<Country> directions = new LinkedList<>();
        Queue<Country> queue = new LinkedList<>();

        Country current = origin;
        queue.add(current);
        visitted.add(current);
        while (!queue.isEmpty()) {
            if (queue.contains(destination)) {
                current = destination;
                break;
            }
            current = queue.remove();
            for (Country country : countryService.getCountriesByCca3(current.getBorders())) {
                if (!visitted.contains(country)) {
                    queue.add(country);
                    visitted.add(country);
                    nextCountryMap.put(country, current);
                }
            }
        }
        if (!current.equals(destination)) {
            return new LandRouteResponse(Collections.emptyList());
        }
        for (Country country = destination; country != null; country = nextCountryMap.get(country)) {
            directions.add(country);
        }

        Collections.reverse(directions);
        List<String> routes = directions.stream().map(d -> d.getCca3()).collect(Collectors.toList());
        return new LandRouteResponse(routes);
    }
}