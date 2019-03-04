package com.test.landroute.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Class representing a simplified model of a class, with cca3 field and borders.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private String cca3;
    private Set<String> borders;
}
