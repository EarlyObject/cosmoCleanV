package com.space.service;

import com.space.exceptions.ShipNotFoundException;
import com.space.exceptions.ShipServiceException;
import com.space.shared.dto.ShipDto;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ShipService {

    ShipDto createShip (ShipDto ship) throws ParseException;

    ShipDto getShipById(Long id) throws ShipNotFoundException, ShipServiceException;
    List<ShipDto> getShips ();
    List<ShipDto> getShips (Map<String, String> allParams);
    List<ShipDto> getShipsCount (Map<String, String> allParams);

    ShipDto updateShip(Long id, ShipDto ship) throws ShipNotFoundException;
    void deleteShip(Long id) throws ShipNotFoundException, ShipServiceException;
}
