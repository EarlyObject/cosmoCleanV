package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShipRepository extends PagingAndSortingRepository<Ship, Long> {

    Ship findShipById(Long id);

    Boolean existsShipById(Long id);
    boolean existsById(Long id);

}
