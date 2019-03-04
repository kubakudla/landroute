package com.test.landroute.service;

import com.test.landroute.Application;
import com.test.landroute.model.Country;
import com.test.landroute.model.LandRouteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

/**
 * LandRouteService tests
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
class LandRouteServiceTest {

    private Country czechia;

    private Country italy;

    private Country austria;

    private Country germany;

    @Autowired
    private LandRouteService landRouteService;

    @MockBean
    private CountryService countryService;

    @BeforeEach
    void initData() {
        // removed some borders to simplify the init data
        czechia = Country.builder().cca3("CZE").borders(new HashSet<>(Arrays.asList("AUT", "DEU"))).build();
        italy = Country.builder().cca3("ITA").borders(new HashSet<>(Arrays.asList("AUT"))).build();
        austria = Country.builder().cca3("AUT").borders(new HashSet<>(Arrays.asList("CZE", "DEU", "ITA"))).build();
        germany = Country.builder().cca3("DEU").borders(new HashSet<>(Arrays.asList("CZE", "AUT"))).build();

        when(countryService.getCountriesByCca3(czechia.getBorders())).thenReturn(new HashSet<>(Arrays.asList(austria, germany)));
        when(countryService.getCountriesByCca3(germany.getBorders())).thenReturn(new HashSet<>(Arrays.asList(czechia, austria)));
        when(countryService.getCountriesByCca3(italy.getBorders())).thenReturn(new HashSet<>(Arrays.asList(austria)));
        when(countryService.getCountriesByCca3(austria.getBorders())).thenReturn(new HashSet<>(Arrays.asList(czechia, germany, italy)));
    }

    @Test
    void should_find_route_czechia_to_italy() {
        //when
        LandRouteResponse landRouteResponse = landRouteService.getDirections(czechia, italy);

        //then
        assertFalse(landRouteResponse.getRoute().isEmpty());
        assertThat(landRouteResponse.getRoute()).isEqualTo(Arrays.asList("CZE", "AUT", "ITA"));
    }

    @Test
    void should_find_route_italy_to_czechia() {
        //when
        LandRouteResponse landRouteResponse = landRouteService.getDirections(italy, czechia);

        //then
        assertFalse(landRouteResponse.getRoute().isEmpty());
        assertThat(landRouteResponse.getRoute()).isEqualTo(Arrays.asList("ITA", "AUT", "CZE"));
    }

    @Test
    void should_find_route_italy_to_germany() {
        //when
        LandRouteResponse landRouteResponse = landRouteService.getDirections(italy, germany);

        //then
        assertFalse(landRouteResponse.getRoute().isEmpty());
        assertThat(landRouteResponse.getRoute()).isEqualTo(Arrays.asList("ITA", "AUT", "DEU"));
    }

    @Test
    void should_find_route_germany_to_austria() {
        //when
        LandRouteResponse landRouteResponse = landRouteService.getDirections(germany, austria);

        //then
        assertFalse(landRouteResponse.getRoute().isEmpty());
        assertThat(landRouteResponse.getRoute()).isEqualTo(Arrays.asList("DEU", "AUT"));
    }


}
