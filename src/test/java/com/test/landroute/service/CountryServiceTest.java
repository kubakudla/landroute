package com.test.landroute.service;

import com.test.landroute.Application;
import com.test.landroute.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CountryService tests
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Test
    void should_find_country_by_cca3() {
        //when
        Optional<Country> country = countryService.getCountryByCca3("CZE");
        //then
        assertTrue(country.isPresent());
        Country czechRepublik = country.get();
        assertThat(czechRepublik.getCca3()).isEqualTo("CZE");
        assertThat(czechRepublik.getBorders()).isEqualTo(new HashSet<>(Arrays.asList("AUT", "DEU", "POL", "SVK")));
    }

    @Test
    void should_not_find_country_by_cca3() {
        //when
        Optional<Country> country = countryService.getCountryByCca3("XYZ");
        //then
        assertFalse(country.isPresent());
    }

    @Test
    void should_find_countries_if_exist_by_cca3() {
        //when
        Set<Country> countries = countryService.getCountriesByCca3(new HashSet<>(Arrays.asList("POL", "CZE", "XYZ", "DEU")));
        //then
        assertThat(countries.size()).isEqualTo(3);
    }
}
