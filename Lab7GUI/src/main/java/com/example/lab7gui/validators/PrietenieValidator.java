package com.example.lab7gui.validators;


import com.example.lab7gui.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getUser1()==null){
            throw new ValidationException("NU EXISTA USER 1");
        }
        if(entity.getUser2()==null){
            throw new ValidationException("NU EXISTA USER 2");
        }
        if(entity.getUser1().getId()==entity.getUser2().getId())
            throw new ValidationException("NU POTI SA TE ADAUGI PE TINE LA PRIETENI");
    }
}

