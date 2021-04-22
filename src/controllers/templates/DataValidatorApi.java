package controllers.templates;

import exceptions.BadColumnTypeException;
import exceptions.BadTypeSizeException;

public interface DataValidatorApi {

    /*
     * checkSize
     *
     * this method throws new BadTypeSizeException if type size is less or equal 0 or when size cannot be casted to int.
     *
     * @param String size
     *
     * @return String
     *
     * @throws BadTypeSizeException
     * */
    String checkSize(final String size) throws BadTypeSizeException;


    /*
     * checkType
     *
     * this method throws new BadColumnTypeException if column type wasn't selected
     *
     * @param String type
     *
     * @throws BadColumnTypeException
     */
    void checkType(final String type) throws BadColumnTypeException;
}
