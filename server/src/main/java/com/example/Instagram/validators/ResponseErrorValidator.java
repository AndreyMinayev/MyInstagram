package com.example.Instagram.validators;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidator {

    // BindingResult is the  class that  contains result of authentication validations
    public ResponseEntity<Object> mapValidationService(BindingResult result){
        if(result.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(result.getAllErrors())){   //  getting all errors
                for(ObjectError error: result.getAllErrors()){
                    errorMap.put(error.getCode(), error.getDefaultMessage());  // putting it in the map
                }
            }
            for(FieldError error: result.getFieldErrors()){   // getting field errors
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            //  putting the map in ResponseEntity  and  giving status BAD_REQUEST
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        // return null if no errors
        return null;



    }



}
