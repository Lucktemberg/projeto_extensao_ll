package com.doacoes.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class DoacaoStatusRequestDTO {

    @NotBlank(message = "Informe a etapa da doação")
    private String status;

    private LocalDate dataColeta;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }
}
