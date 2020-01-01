package com.space.controller;

import com.space.exceptions.ShipNotFoundException;
import com.space.exceptions.ShipServiceException;
import com.space.model.request.ShipDetailsRequestModel;
import com.space.model.response.ErrorMessages;
import com.space.model.response.ShipRest;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import com.space.shared.dto.ShipDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;


@RestController
@RequestMapping("rest/ships")
public class ShipController  {

    @Autowired
    ShipService shipService;

    @Autowired
    ShipRepository shipRepository;


    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    ) // раньше работало с @Valid перед  @RequestBody
    public ShipRest createShip(@RequestBody ShipDetailsRequestModel shipDetails) throws ShipServiceException, ParseException {

        ShipRest returnValue = new ShipRest();

        if (shipDetails.getName() == null || shipDetails.getPlanet() == null
                || shipDetails.getShipType() == null || shipDetails.getProdDate() == null
                || shipDetails.getSpeed() == null || shipDetails.getCrewSize() == null) {
            throw new ShipServiceException((ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage()));
        }

        if (shipDetails.getName().length() > 50 || shipDetails.getPlanet().length() > 50
                || shipDetails.getName().isEmpty() || shipDetails.getPlanet().isEmpty()
                || shipDetails.getCrewSize() > 9999 || shipDetails.getCrewSize() < 1
                ||shipDetails.getSpeed() > 0.99 || shipDetails.getSpeed() < 0.01) {
            throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }

        Date dateInit = new GregorianCalendar(2800, Calendar.JANUARY, 01).getTime();
        Date dateFin = new GregorianCalendar(3019, Calendar.DECEMBER, 31).getTime();

        if (shipDetails.getProdDate().before(dateInit)
                || shipDetails.getProdDate().after(dateFin)) {
            throw new ShipServiceException(ErrorMessages.WRONG_DATE.getErrorMessage());
        }

        ShipDto shipDto = new ShipDto();
        BeanUtils.copyProperties(shipDetails, shipDto);

        ShipDto createdShip = shipService.createShip(shipDto);
        BeanUtils.copyProperties(createdShip, returnValue);

        return returnValue;
    }

    /*@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<ShipRest> getShips(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String planet,
                                   @RequestParam(required = false) String shipType,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean isUsed,
                                   @RequestParam(required = false) Double minSpeed,
                                   @RequestParam(required = false) Double maxSpeed,
                                   @RequestParam(required = false) Integer minCrewSize,
                                   @RequestParam(required = false) Integer maxCrewSize,
                                   @RequestParam(required = false) Double minRating,
                                   @RequestParam(required = false) Double maxRating,
                                   @RequestParam(required = false) ShipOrder order) {
*/


    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<ShipRest> getShips(@RequestParam Map<String, String> allParams) {

        List<ShipRest> returnValue = new ArrayList<>();

        List<ShipDto> ships = shipService.getShips(allParams);

        for (ShipDto shipDto: ships) {
            ShipRest shipModel = new ShipRest();
            BeanUtils.copyProperties(shipDto, shipModel);
            returnValue.add(shipModel);
        }

        return returnValue;
    }



   /* @GetMapping(path = "/{count}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Integer getShipsCount(@RequestParam Map<String, String> allParams) {
        List<ShipDto> ships = shipService.getShipsCount(allParams);
        return ships.size();
    }
*/

    @GetMapping(value = "/{id}")
    //produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    //  @ResponseStatus(HttpStatus.OK)
    public ShipRest getShip(@PathVariable Long id) throws ShipNotFoundException, ShipServiceException {

        ShipRest returnValue = new ShipRest();

        Long checkedId = null;
        try {
            checkedId = id;
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (checkedId <= 0) {
            throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }
        if (!shipRepository.existsById(checkedId) || !shipRepository.existsShipById(checkedId)) {
            throw new ShipNotFoundException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }

        ShipDto shipDto = shipService.getShipById(checkedId);
        BeanUtils.copyProperties(shipDto, returnValue);

        return returnValue;
    }

    @PostMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ShipRest updateShip(@PathVariable Long id, @RequestBody ShipDetailsRequestModel shipDetails) throws ShipNotFoundException {

        if (shipDetails.getName() == null && shipDetails.getPlanet() == null && shipDetails.getShipType() == null
                && shipDetails.getProdDate() == null && shipDetails.getUsed() == null
                && shipDetails.getSpeed() == null && shipDetails.getCrewSize() == null) {

            ShipRest returnValue = new ShipRest();
            BeanUtils.copyProperties(shipDetails, returnValue);
        }

        ShipRest returnValue = new ShipRest();

        if (id <= 0) {
            throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }
        if (!shipRepository.existsById(id) || !shipRepository.existsShipById(id)) {
            throw new ShipNotFoundException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }
        if (shipDetails.getName() != null ) {
            if (shipDetails.getName().length() > 50 || shipDetails.getName().isEmpty()) {
                throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
            }
        }
        if (shipDetails.getPlanet() != null) {
            if (shipDetails.getPlanet().length() > 50 || shipDetails.getPlanet().isEmpty()) {
                throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
            }
        }
        if (shipDetails.getCrewSize() != null) {
            if ( shipDetails.getCrewSize() > 9999 || shipDetails.getCrewSize() < 1) {
                throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
            }
        }
        if (shipDetails.getSpeed() != null) {
            if (shipDetails.getSpeed() > 0.99 || shipDetails.getSpeed() < 0.01) {
                throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
            }
        }
        Date dateInit = new GregorianCalendar(2800, Calendar.JANUARY, 01).getTime();
        Date dateFin = new GregorianCalendar(3019, Calendar.DECEMBER, 31).getTime();
        if (shipDetails.getProdDate() != null) {
            if (shipDetails.getProdDate().before(dateInit)
                    || shipDetails.getProdDate().after(dateFin)) {
                throw new ShipServiceException(ErrorMessages.WRONG_DATE.getErrorMessage());
            }
        }


        ShipDto shipDto = new ShipDto();
        BeanUtils.copyProperties(shipDetails, shipDto);

        ShipDto updatedShip = shipService.updateShip(id, shipDto);
        BeanUtils.copyProperties(updatedShip, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}")
    public void deleteShip(@PathVariable Long id) throws ShipNotFoundException, ShipServiceException {


        if (id <= 0) {
            throw new ShipServiceException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }
        if (!shipRepository.existsById(id) || !shipRepository.existsShipById(id)) {
            throw new ShipNotFoundException(ErrorMessages.WRONG_ARGUMENT.getErrorMessage());
        }

        shipService.deleteShip(id);

    }
}




