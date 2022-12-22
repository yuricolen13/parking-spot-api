package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository // Anotação para determinar que essa classe 
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> { 
    /*
    # Por que extendeu o JpaRepository?
        Porque o JpaRepository já possui vários métodos prontos para utilizar para transações com banco de dados, não necessitando a criação de 
        scripts ou querys para consumir os dados
    
    # extends JpaRepository<ParkingSpotModel, UUID>
        * Informa qual é o model a ser utilizado para a criação desse repository
        * Informa a chave desse model/repository
    */
    
    boolean existsByLicensePlateCar(String licensePlateCar);
    boolean existsByParkingSpotNumber(String parkingSpotNumber);
    boolean existsByApartmentAndBlock(String apartment, String block);
}
