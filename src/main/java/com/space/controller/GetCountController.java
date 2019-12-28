package com.space.controller;

import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import com.space.shared.dto.ShipDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("rest/ships/count")
public class GetCountController  {

    @Autowired
    ShipService shipService;

    @Autowired
    ShipRepository shipRepository;



    @GetMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Integer getShipsCount(@RequestParam Map<String, String> allParams) {
        List<ShipDto> ships = shipService.getShipsCount(allParams);
        return ships.size();
    }






}




