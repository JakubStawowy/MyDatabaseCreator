package controllers.validators;

import database.repositories.DataTypesRepository;
import exceptions.*;
import controllers.templates.DataValidatorApi;

public final class DataValidator implements DataValidatorApi {

    @Override
    public String checkSize(final String size) throws BadTypeSizeException {
        if(!(size.equals("Size") || size.equals(""))) {
            try {
                if(Integer.parseInt(size)<=0)
                    throw  new BadTypeSizeException("Bad length. Length cannot be less or equal 0");
            } catch (NumberFormatException ignored) {
                throw new BadTypeSizeException("Bad length. Length must be a number");
            }
            return "("+size+")";
        }
        else return "";
    }

    @Override
    public void checkType(final String type) throws BadColumnTypeException {
        boolean flag = true;
        if(type.equals("null"))
            throw new BadColumnTypeException("Choose column type");

        for(String numericType: DataTypesRepository.getAllTypes()){
            if (numericType.equals(type.toLowerCase())) {
                flag = false;
                break;
            }
        }

        if(flag) {
            throw new BadColumnTypeException("Bad Column Type");
        }
    }
}
