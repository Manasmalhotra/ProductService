package com.example.productservice.exceptions;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(){
        super("Please enter correct product data");
    }
}
