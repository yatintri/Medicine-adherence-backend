package com.example.user_service.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidCheck implements ConstraintValidator<ImageValidator, MultipartFile> {

    @Override
    public void  initialize(ImageValidator constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context){

        if(value.isEmpty()){
            return false;
        }
        String file = value.getContentType();
        if(!(file.equals("image/jpg")|| file.equals("image/png")|| file.equals("image/jpeg"))){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only png,jpg,or jpeg files are allowed").addConstraintViolation();
            return false;
        }
        return true;
    }
}
