package com.example.lab7gui.validators;

import com.example.lab7gui.domain.Message;

public class MessagesValidator implements Validator<Message>{

    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getId()==null){
            throw  new ValidationException("NU POATE SA AIBA ID-UL NULL");
        }
    }
}
