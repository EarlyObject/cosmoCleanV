package com.space.service.impl;


import com.space.controller.ShipOrder;
import com.space.exceptions.ShipNotFoundException;
import com.space.exceptions.ShipServiceException;
import com.space.model.Ship;
import com.space.model.response.ErrorMessages;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import com.space.shared.dto.ShipDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.space.service.Round.round;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    ShipRepository shipRepository;

    public ShipServiceImpl() {
    }

    @Override
    public ShipDto createShip(ShipDto shipDto)  {

        Ship ship = new Ship();
        BeanUtils.copyProperties(shipDto, ship);
        if (shipDto.getUsed() == null) { //изменения в код, проверить!
            ship.setUsed(false);
        }
        Double K;
        if (ship.getUsed() != null && !ship.getUsed()) {
            K = 1.0;
        } else {
            K = 0.5;
        }

        Long millis = ship.getProdDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int timeDifference = 3019 - calendar.get(Calendar.YEAR);


        Double rating = new Double(80*ship.getSpeed()*K)/(timeDifference + 1);

        ship.setRating(round(rating, 2));


        Ship storedShipDetails = shipRepository.save(ship);

        ShipDto returnValue = new ShipDto();
        BeanUtils.copyProperties(storedShipDetails, returnValue);

        return returnValue;
    }


    @Override
    public ShipDto getShipById(Long id) throws ShipNotFoundException, ShipServiceException {
        ShipDto returnValue = new ShipDto();

        if (id <= 0) {
            throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }
        if (!shipRepository.existsById(id) || !shipRepository.existsShipById(id)) {
            throw new ShipNotFoundException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }

        Ship ship = shipRepository.findShipById(id);

        if (ship == null) {
            throw new ShipNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(ship, returnValue);

        return returnValue;
    }

    @Override
    public List<ShipDto> getShips() {
        List<ShipDto> returnValue = new ArrayList<>();

        List<Ship> ships = (List<Ship>) shipRepository.findAll();

        for (Ship ship: ships) {
            ShipDto shipDto = new ShipDto();
            BeanUtils.copyProperties(ship, shipDto);
            returnValue.add(shipDto);
        }

        return returnValue;
    }

    @Override
    public List<ShipDto> getShips(Map<String, String> allParams) {

        List<ShipDto> returnValue = new ArrayList<>();

        List<Ship> ships;
        String sortFilter;
        if (allParams.containsKey("order")) {
            ShipOrder shipOrder = ShipOrder.valueOf(allParams.get("order").toString());
            sortFilter = shipOrder.getFieldName();
            ships = (List<Ship>) shipRepository.findAll(Sort.by(Sort.Direction.ASC, sortFilter));
        } else {
            ships = (List<Ship>) shipRepository.findAll();
        }

        if (allParams.containsKey("name")) {
            ships.removeIf(s -> !s.getName().contains((String)allParams.get("name")));
        }
        if (allParams.containsKey("planet")) {
            ships.removeIf(s -> !s.getPlanet().contains((String)allParams.get("planet")));
        }
        if (allParams.containsKey("shipType")) {
            ships.removeIf(s -> !s.getShipType().toString().equals(allParams.get("shipType").toString()));
        }
        if (allParams.containsKey("after")) {
            ships.removeIf(s -> s.getProdDate().getTime() < Long.parseLong(allParams.get("after").toString()));
        }
        if (allParams.containsKey("before")) {
            ships.removeIf(s -> s.getProdDate().getTime() > Long.parseLong(allParams.get("before").toString()));
        }
        if (allParams.containsKey("isUsed")) {
            ships.removeIf(s -> !s.getUsed().toString().equals(allParams.get("isUsed")));
        }
        if (allParams.containsKey("minSpeed")) {
            ships.removeIf(s -> s.getSpeed() < Double.parseDouble(allParams.get("minSpeed").toString()));
        }
        if (allParams.containsKey("maxSpeed")) {
            ships.removeIf(s -> s.getSpeed() > Double.parseDouble(allParams.get("maxSpeed").toString()));
        }
        if (allParams.containsKey("minCrewSize")) {
            ships.removeIf(s -> s.getCrewSize() < Integer.parseInt(allParams.get("minCrewSize").toString()));
        }
        if (allParams.containsKey("maxCrewSize")) {
            ships.removeIf(s -> s.getCrewSize() > Integer.parseInt(allParams.get("maxCrewSize").toString()));
        }
        if (allParams.containsKey("minRating")) {
            ships.removeIf(s -> s.getRating() < Double.parseDouble(allParams.get("minRating").toString()));
        }
        if (allParams.containsKey("maxRating")) {
            ships.removeIf(s -> s.getRating() > Double.parseDouble(allParams.get("maxRating").toString()));
        }

        int pageNumber = 0;
        if (allParams.containsKey("pageNumber")) {
            pageNumber = Integer.parseInt(allParams.get("pageNumber").toString());
        }
        int pageSize = 3;
        if (allParams.containsKey("pageSize")) {
            pageSize = Integer.parseInt(allParams.get("pageSize").toString());
        }


        int start = (int) new PageRequest(pageNumber, pageSize).getOffset();
        int end = (start + new PageRequest(pageNumber, pageSize).getPageSize()) > ships.size() ? ships.size() : (start + new PageRequest(pageNumber, pageSize).getPageSize());

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Ship> shipPage = new PageImpl<Ship>(ships.subList(start, end), pageRequest, ships.size());

        List<Ship> shipsToConvert = shipPage.getContent();

        for (Ship ship: shipsToConvert) {
            ShipDto shipDto = new ShipDto();
            BeanUtils.copyProperties(ship, shipDto);
            returnValue.add(shipDto);
        }

        return returnValue;
    }

    @Override
    public List<ShipDto> getShipsCount(Map<String, String> allParams) {

        List<ShipDto> returnValue = new ArrayList<>();

        List<Ship> ships;
        String sortFilter;
        if (allParams.containsKey("order")) {
            ShipOrder shipOrder = ShipOrder.valueOf(allParams.get("order").toString());
            sortFilter = shipOrder.getFieldName();
            ships = (List<Ship>) shipRepository.findAll(Sort.by(Sort.Direction.ASC, sortFilter));
        } else {
            ships = (List<Ship>) shipRepository.findAll();
        }

        if (allParams.containsKey("name")) {
            ships.removeIf(s -> !s.getName().contains((String)allParams.get("name")));
        }
        if (allParams.containsKey("planet")) {
            ships.removeIf(s -> !s.getPlanet().contains((String)allParams.get("planet")));
        }
        if (allParams.containsKey("shipType")) {
            ships.removeIf(s -> !s.getShipType().toString().equals(allParams.get("shipType").toString()));
        }
        if (allParams.containsKey("after")) {
            ships.removeIf(s -> s.getProdDate().getTime() < Long.parseLong(allParams.get("after").toString()));
        }
        if (allParams.containsKey("before")) {
            ships.removeIf(s -> s.getProdDate().getTime() > Long.parseLong(allParams.get("before").toString()));
        }
        if (allParams.containsKey("isUsed")) {
            ships.removeIf(s -> !s.getUsed().toString().equals(allParams.get("isUsed")));
        }
        if (allParams.containsKey("minSpeed")) {
            ships.removeIf(s -> s.getSpeed() < Double.parseDouble(allParams.get("minSpeed").toString()));
        }
        if (allParams.containsKey("maxSpeed")) {
            ships.removeIf(s -> s.getSpeed() > Double.parseDouble(allParams.get("maxSpeed").toString()));
        }
        if (allParams.containsKey("minCrewSize")) {
            ships.removeIf(s -> s.getCrewSize() < Integer.parseInt(allParams.get("minCrewSize").toString()));
        }
        if (allParams.containsKey("maxCrewSize")) {
            ships.removeIf(s -> s.getCrewSize() > Integer.parseInt(allParams.get("maxCrewSize").toString()));
        }
        if (allParams.containsKey("minRating")) {
            ships.removeIf(s -> s.getRating() < Double.parseDouble(allParams.get("minRating").toString()));
        }
        if (allParams.containsKey("maxRating")) {
            ships.removeIf(s -> s.getRating() > Double.parseDouble(allParams.get("maxRating").toString()));
        }

        for (Ship ship: ships) {
            ShipDto shipDto = new ShipDto();
            BeanUtils.copyProperties(ship, shipDto);
            returnValue.add(shipDto);
        }

        return returnValue;
    }

    @Override
    public ShipDto updateShip(Long id, ShipDto ship) throws ShipNotFoundException {
        ShipDto returnValue = new ShipDto();

        Ship shipToUpdate = shipRepository.findShipById(id);
        if (shipToUpdate == null) throw new ShipNotFoundException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());

        if (ship.getName() != null) {
            shipToUpdate.setName(ship.getName());
        }
        if (ship.getPlanet() != null) {
            shipToUpdate.setPlanet(ship.getPlanet());
        }
        if (ship.getShipType() != null) {
            shipToUpdate.setShipType(ship.getShipType());
        }
        if (ship.getProdDate() != null) {
            shipToUpdate.setProdDate(ship.getProdDate());
        }
        if (ship.getUsed() != null) {
            shipToUpdate.setUsed(ship.getUsed());
        }
        if (ship.getSpeed() != null) {
            shipToUpdate.setSpeed(ship.getSpeed());
        }
        if (ship.getCrewSize() != null) {
            shipToUpdate.setCrewSize(ship.getCrewSize());
        }

      /*  if (ship.getName() != null) {
            ship.setName(ship.getName());
        }
        if (ship.getPlanet() != null) {
            ship.setPlanet(ship.getPlanet());
        }
        if (ship.getShipType() != null) {
            ship.setShipType(ship.getShipType());
        }
        if (ship.getProdDate() != null) {
            ship.setProdDate(ship.getProdDate());
        }
        if (ship.getUsed() != null) {
            ship.setUsed(ship.getUsed());
        }
        if (ship.getSpeed() != null) {
            ship.setSpeed(ship.getSpeed());
        }
        if (ship.getCrewSize() != null) {
            ship.setCrewSize(ship.getCrewSize());
        }*/


        Double K;
        if (shipToUpdate.getUsed() != null && !shipToUpdate.getUsed()) {
            K = 1.0;
        } else {
            K = 0.5;
        }

        Long millis = shipToUpdate.getProdDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int timeDifference = 3019 - calendar.get(Calendar.YEAR);


        Double rating = new Double(80*shipToUpdate.getSpeed()*K)/(timeDifference + 1);

        shipToUpdate.setRating(round(rating, 2));
        Ship updatedShip = shipRepository.save(shipToUpdate);
        BeanUtils.copyProperties(updatedShip, returnValue);
        // BeanUtils.copyProperties(shipToUpdate, returnValue);
        BeanUtils.copyProperties(shipToUpdate, ship);


        return returnValue;
        //  return ship;

    }

    @Override
    public void deleteShip(Long id) throws ShipNotFoundException {
      /*  Ship ship = shipRepository.findShipById(id);

        if (ship == null) throw new ShipNotFoundException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());

        shipRepository.delete(ship);*/

        if (!shipRepository.existsById(id)) {
            throw new ShipNotFoundException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }
        shipRepository.deleteById(id);
    }
}
