package com.example.bloodbank.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"status", "message", "data"})
public class MainResponse {
    private boolean status;
    private String message;
    private Object data;
    public MainResponse(boolean status,String message){
        this.status = status;
        this.message = message;
    }
}