package com.test.landroute.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Class representing response with a route from one country to another
 */
@Data
@AllArgsConstructor
public class LandRouteResponse {

    private List<String> route;
}
