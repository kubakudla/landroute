package com.test.landroute.controller;

import com.test.landroute.model.Country;
import com.test.landroute.model.LandRouteResponse;
import com.test.landroute.service.CountryService;
import com.test.landroute.service.LandRouteService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Main controller for Land route application. Contains REST API methods.
 */
@RestController
@RequestMapping("routing")
@AllArgsConstructor
public class LandRouteController {

    static final String ERROR_DUPLICATE_PARAMETERS = "Origin shouldn't be the same as destination!";
    static final String ERROR_ORIGIN_COUNTRY_NOT_FOUND = "Origin country not found!";
    static final String ERROR_DESTINATION_COUNTRY_NOT_FOUND = "Destination country not found!";
    static final String WARNING_NO_LAND_CROSSING_IS_POSSIBLE = "No land crossing is possible!";

    private final LandRouteService landRouteService;

    private final CountryService countryService;

    /**
     * Method that accepts origin and destination countries and returns the route for them.
     *
     * @param origin      the origin country (cca3 field)
     * @param destination the destination country (cca3 field)
     * @return
     */
    @ApiOperation("Method that accepts origin and destination countries and returns the route for them.")
    @ResponseBody
    @GetMapping("/{origin}/{destination}")
    public ResponseEntity<Object> getRouting(@PathVariable String origin, @PathVariable String destination) {

        if (origin.equalsIgnoreCase(destination)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_DUPLICATE_PARAMETERS);
        }

        Optional<Country> originCountry = countryService.getCountryByCca3(origin);
        if (!originCountry.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_ORIGIN_COUNTRY_NOT_FOUND);
        }

        Optional<Country> destinationCountry = countryService.getCountryByCca3(destination);
        if (!destinationCountry.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_DESTINATION_COUNTRY_NOT_FOUND);
        }

        LandRouteResponse landRouteResponse = landRouteService.getDirections(originCountry.get(), destinationCountry.get());
        if (landRouteResponse.getRoute().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WARNING_NO_LAND_CROSSING_IS_POSSIBLE);
        }
        return ResponseEntity.ok(landRouteResponse);
    }
}
