package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.AppProperties;
import com.api.parkingcontrol.MyBean;
import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController // Camada que receberá as solicitações (GET, POST, PUT, DELETE)
@CrossOrigin(origins = "*", maxAge = 3600) // Permitir/bloquear acesso de qualquer fonte ("*" para qualquer lugar ou pode especificar) 
@RequestMapping("/parking-spot") // Define a url que quando for requisitada chamará o metodo ou a classe (ex.: http://localhost:8080/parking-spot)
@Scope("prototype")
@PropertySource("classpath:custom.properties")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private MyBean myBean;

    @Autowired
    private AppProperties appProperties;

    @Value("${app.name}")
    private String appName;

    @Value("${app.port}")
    private String appPort;

    @Value("${app.host}")
    private String appHost;

    @Value("${message}")
    private String message;

    public ParkingSpotController() {
        System.out.println("ParkingSpotController created!!!");
    }

    /*
    * @PostMapping
        - "ResponseEntity<Object> para diferentes tipos de retorno
        - URL definida a nível de classe (@RequestMapping("/parking-spot"))
        - @RequestBody: exige que seja recebido um body JSON
        - @Valid: Valida os dados recebidos no JSON, conforme o DTO (ParkingSpotDto parkingSpotDto). Se estiver faltando algum dos campos, já vai retornar bad request
    * Após as verificações, os dados serão salvos no BD através do Model.
    * BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel); // Convertendo o DTO para Model
     */
    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {

        // Consulta no BD se a placa já está cadastrada
        if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!"); // Status 409
        }

        // Consulta no BD se a vaga já está preenchida
        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!"); // Status 409
        }

        // Consulta no BD se o apartamento e o block já estão usando alguma vaga
        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!"); // Status 409
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel); // Convertendo o DTO para Model
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC"))); // Formatação da data

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel)); // Retorno com status 201
    }

    /*
    * @GetMapping (sem paginação, sem ordenação)
        @GetMapping
        public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpotsWithoutPagination(){
            return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll())
        }
    */
    
    @GetMapping // Listar tudo
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        System.out.println("App Name: " + appProperties.getName());
        System.out.println("App Port: " + appProperties.getPort());
        System.out.println("App Host: " + appProperties.getHost());

        System.out.println(message);
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}") // Listar por ID
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found."); // Status 404
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get()); // Status 200
    }

    @DeleteMapping("/{id}") // Deletar por ID
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        
        if (!parkingSpotModelOptional.isPresent()) { // Verifica se o ID existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully."); // Status 200
    }

    @PutMapping("/{id}") // Atualizar por ID
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
            @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found."); // Status 404
        }
        
        var parkingSpotModel = new ParkingSpotModel();
        //var parkingSpotModel = parkingSpotModelOptional.get();
        
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel); // Convertendo o DTO para Model
        
        // Mantém ID e data de registro, pq eles não podem ser atualizados
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        
        /*
            Setando manualmente alterações (sem o BeanUtils)

            parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
            parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
            parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
            parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
            parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
            parkingSpotModel.setResponsibleName(parkingSpotDto.getResponsibleName());
            parkingSpotModel.setApartment(parkingSpotDto.getApartment());
            parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
        */
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel)); // Status 200
    }

    @GetMapping("/testParam")
    public ResponseEntity<Object> getOneParkingSpotTestParam(@RequestParam(value = "id", required = true) UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found."); // Status 404
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get()); // Status 200
    }

}
