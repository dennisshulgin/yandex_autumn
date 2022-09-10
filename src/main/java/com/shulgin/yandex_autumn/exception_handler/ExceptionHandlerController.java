package com.shulgin.yandex_autumn.exception_handler;

import com.shulgin.yandex_autumn.exception.IncorrectInputData;
import com.shulgin.yandex_autumn.exception.ItemNotFoundException;
import com.shulgin.yandex_autumn.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<IncorrectInputData> validationExceptionHandler(ValidationException e) {
        String message = "Validation Failed";
        IncorrectInputData incorrectInputData = new IncorrectInputData(message, 400);
        return new ResponseEntity<>(incorrectInputData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectInputData> itemNotFoundExceptionHandler(ItemNotFoundException e) {
        String message = "Item not found";
        IncorrectInputData incorrectInputData = new IncorrectInputData(message, 404);
        return new ResponseEntity<>(incorrectInputData, HttpStatus.NOT_FOUND);
    }
}