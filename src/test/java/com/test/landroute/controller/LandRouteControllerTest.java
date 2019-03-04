package com.test.landroute.controller;

import com.test.landroute.Application;
import com.test.landroute.model.Country;
import com.test.landroute.model.LandRouteResponse;
import com.test.landroute.service.CountryService;
import com.test.landroute.service.LandRouteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static com.test.landroute.controller.LandRouteController.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests for LandRouteController
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
class LandRouteControllerTest {

    private static final String ORIGIN = "CZE";
    private static final String DESTINATION = "ITA";

    @Autowired
    private LandRouteController landRouteController;

    @MockBean
    private LandRouteService landRouteService;

    @MockBean
    private CountryService countryService;


    @Test
    void should_run_successfully() {
        //given
        Country cze = new Country("CZE", Collections.emptySet());
        Country ita = new Country("ITA", Collections.emptySet());
        LandRouteResponse landRouteResponse = new LandRouteResponse(Arrays.asList("CZE", "AUT", "ITA"));
        when(countryService.getCountryByCca3(ORIGIN)).thenReturn(Optional.of(cze));
        when(countryService.getCountryByCca3(DESTINATION)).thenReturn(Optional.of(ita));
        when(landRouteService.getDirections(cze, ita)).thenReturn(landRouteResponse);

        //when
        ResponseEntity<Object> routing = landRouteController.getRouting(ORIGIN, DESTINATION);

        //then
        assertThat(routing.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(routing.getBody()).isEqualTo(landRouteResponse);
    }

    @Test
    void should_show_error_when_destination_equals_origin() {
        //when
        ResponseEntity<Object> responseEntity = landRouteController.getRouting(ORIGIN, ORIGIN);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(ERROR_DUPLICATE_PARAMETERS);
    }

    @Test
    void should_show_error_when_origin_country_is_not_found() {
        //given
        when(countryService.getCountryByCca3(ORIGIN)).thenReturn(Optional.empty());

        //when
        ResponseEntity<Object> responseEntity = landRouteController.getRouting(ORIGIN, DESTINATION);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(ERROR_ORIGIN_COUNTRY_NOT_FOUND);
    }

    @Test
    void should_show_error_when_destination_country_is_not_found() {
        //given
        Country cze = new Country("CZE", Collections.emptySet());
        when(countryService.getCountryByCca3(ORIGIN)).thenReturn(Optional.of(cze));
        when(countryService.getCountryByCca3(DESTINATION)).thenReturn(Optional.empty());

        //when
        ResponseEntity<Object> responseEntity = landRouteController.getRouting(ORIGIN, DESTINATION);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(ERROR_DESTINATION_COUNTRY_NOT_FOUND);
    }

    @Test
    void should_show_error_when_no_landmark_is_found() {
        //given
        Country cze = new Country("CZE", new HashSet<>());
        Country ita = new Country("ITA", new HashSet<>());
        when(countryService.getCountryByCca3(ORIGIN)).thenReturn(Optional.of(cze));
        when(countryService.getCountryByCca3(DESTINATION)).thenReturn(Optional.of(ita));
        when(landRouteService.getDirections(cze, ita)).thenReturn(new LandRouteResponse(Collections.emptyList()));


        //when
        ResponseEntity<Object> responseEntity = landRouteController.getRouting(ORIGIN, DESTINATION);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo(WARNING_NO_LAND_CROSSING_IS_POSSIBLE);
    }

}
