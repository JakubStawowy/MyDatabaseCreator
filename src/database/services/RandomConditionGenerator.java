package database.services;

import exceptions.EmptyTableException;
import database.models.Table;
import database.repositories.DataTypesRepository;

import java.util.Random;

public final class RandomConditionGenerator {
    public static String generateRandomCondition(final Table table) throws EmptyTableException {
        if(table.getNumberOfRows()==0)
            throw new EmptyTableException("Cannot generate condition. Table "+table.getTableName()+" is empty");
        Random random = new Random();
        int randomColumnIndex = random.nextInt(table.getNumberOfColumns());
        int randomRowIndex = random.nextInt(table.getNumberOfRows());
        int randomOperatorIndex;
        String[] operators = new String[]{"=", "!=", ">", "<", "<=",">="};
        String cellData;
        String columnType = table.getColumnTypes().get(randomColumnIndex);
        if(!DataTypesRepository.isNumeric(columnType)) {

            randomOperatorIndex = random.nextInt(2);
            cellData = "\""+table.getData().get(randomRowIndex).get(randomColumnIndex)+"\"";
        }
        else {

            randomOperatorIndex = random.nextInt(operators.length);
            cellData = String.valueOf(table.getData().get(randomRowIndex).get(randomColumnIndex));
        }

        return table.getColumnNames().get(randomColumnIndex)+operators[randomOperatorIndex]+cellData;
    }
}
