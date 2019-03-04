package com.test.landroute.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.landroute.model.Country;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class having methods for getting informations about countries
 */
@Service
public class CountryService {

    private static final String COUNTRIES_PATH = "src/main/resources/countries.json";

    private final Set<Country> countries;

    public CountryService() throws FileNotFoundException {
        countries = initCountries();
    }

    private Set<Country> initCountries() throws FileNotFoundException {
        return new Gson().fromJson(new FileReader(COUNTRIES_PATH), new TypeToken<Set<Country>>() {
        }.getType());
    }

    /**
     * Get a country by cca3 field
     *
     * @param cca3 field
     * @return country
     */
    public Optional<Country> getCountryByCca3(String cca3) {
        if (StringUtils.isEmpty(cca3)) {
            return Optional.empty();
        }
        return countries.stream().filter(c -> c.getCca3().equalsIgnoreCase(cca3)).findAny();
    }

    /**
     * Get countries by a set of cca3 fields
     *
     * @param cca3Set set of cca3 fields
     * @return set of countries
     */
    Set<Country> getCountriesByCca3(Set<String> cca3Set) {
        return countries.stream().filter(c -> cca3Set.contains(c.getCca3())).collect(Collectors.toSet());
    }
}
