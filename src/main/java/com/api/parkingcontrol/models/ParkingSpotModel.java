/*
### Model ###
Explicação: Modelo, estrutura básica para receber e trabalhar os dados, um "template", um "esqueleto", um "schema"
 */
package com.api.parkingcontrol.models;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity // Anotação para o JPA gerar o modelo da tabela no banco de dados
@Table(name = "TB_PARKING_SPOT") // Anotação para definir o nome da tabela a ser criada
public class ParkingSpotModel implements Serializable { // "implements Serializable": Conversões feitas por baixo dos panos, de java, para bytes para BD

    private static final long serialVersionUID = 1L; // ID do Serializable
    
    /*
    # @Id // Anotação para definir a coluna que será o ID da tabela
    
    # @GeneratedValue(strategy = GenerationType.AUTO) // Anotação para definir responsável pela geração da chave
        - Se "GenerationType.AUTO"
            então: JPA escolhe a estratégia mais adequada de acordo com o banco de dados.
        - Se "GenerationType.IDENTITY"
            então: Gerados pela coluna de auto incremento do banco de dados.
        - Se "GenerationType.SEQUENCE"
            então: Gerados a partir de uma sequence. 
                   Caso não seja especificado um nome para a sequence, será utilizada uma sequence padrão, a qual será global, para todas as entidades. 
                   Caso uma sequence seja especificada, o provedor passará a adotar essa sequence para criação das chaves primárias.
                   Alguns bancos de dados podem não suportar essa opção.
        - Se "GenerationType.TABLE"
            então: Necessário criar uma tabela para gerenciar as chaves primárias. 
                   Por causa da sobrecarga de consultas necessárias para manter a tabela atualizada, essa opção é pouco recomendada.
    
    # @Column(nullable = false, unique = true, length = 10) // Anotação para definir coluna a ser criada e parâmetros (aceita nulo ou não, valor único ou não, tamanho)
    */
    
    @Id // ID da tabela
    @GeneratedValue(strategy = GenerationType.AUTO) // Geração da chave
    private UUID id; // Tipo de chave

    @Column(nullable = false, unique = true, length = 10) // Coluna a ser criada e seus parâmetros
    private String parkingSpotNumber;

    @Column(nullable = false, unique = true, length = 7)
    private String licensePlateCar;

    @Column(nullable = false, length = 70)
    private String brandCar;

    @Column(nullable = false, length = 70)
    private String modelCar;

    @Column(nullable = false, length = 70)
    private String colorCar;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false, length = 130)
    private String responsibleName;

    @Column(nullable = false, length = 30)
    private String apartment;

    @Column(nullable = false, length = 30)
    private String block;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParkingSpotNumber() {
        return parkingSpotNumber;
    }

    public void setParkingSpotNumber(String parkingSpotNumber) {
        this.parkingSpotNumber = parkingSpotNumber;
    }

    public String getLicensePlateCar() {
        return licensePlateCar;
    }

    public void setLicensePlateCar(String licensePlateCar) {
        this.licensePlateCar = licensePlateCar;
    }

    public String getBrandCar() {
        return brandCar;
    }

    public void setBrandCar(String brandCar) {
        this.brandCar = brandCar;
    }

    public String getModelCar() {
        return modelCar;
    }

    public void setModelCar(String modelCar) {
        this.modelCar = modelCar;
    }

    public String getColorCar() {
        return colorCar;
    }

    public void setColorCar(String colorCar) {
        this.colorCar = colorCar;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
