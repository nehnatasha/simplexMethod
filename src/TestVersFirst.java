import java.util.ArrayList;

public class TestVersFirst {

    public static void main(String[] args) {

        //Первоначальный массив
        String matrixStr = "3 4 2\n4 3 3";
        ArrayList<ArrayList<Double>> matrixMain = parseMatrixFromString(matrixStr);
        int yCount = matrixMain.size();

        //Значения равенств уравнений
        String resultStr = "1020 940";
        ArrayList<Double> result = parseArrayFromString(resultStr);
        ArrayList<ArrayList<Double>> fullMatrix = new ArrayList<ArrayList<Double>>();

        //Заполнение матрицы дополнительными переменными
        for (int i = 0; i < yCount; i++) {
            ArrayList<Double> fullRow = (ArrayList<Double>) matrixMain.get(i).clone();
            for (int j = 0; j < yCount; j++) {
                if (j == i) {
                    fullRow.add(1.0);
                } else {
                    fullRow.add(0.0);
                }
            }
            fullMatrix.add(fullRow);//заполнение матрицы дополнительными переменными
        }

        //Значения целевой функции
        String targetFuncStr = "10 12 8";
        ArrayList<Double> targetFunc = parseArrayFromString(targetFuncStr);

        //Значения целевой функции умножаем на -1
        for (int i = 0; i < targetFunc.size(); i++) {
            targetFunc.set(i, targetFunc.get(i) * -1);
        }

        //Добавляем к целевой функции дополнительные переменные
        for (int i = 0; i < yCount; i++) {
            targetFunc.add(0.0);
        }

        //Инициализация массива, где буду храниться базисы
        ArrayList<Double> basis = new ArrayList<>();

        //Из targetFunc находим наименьшее число
        //В indexOfColumn записывается индекс разрешающего столбца
        Double min = 10000.0;
        int indexOfColumn = 10000000;
        for (int i = 0; i < targetFunc.size(); i++) {
            if (targetFunc.get(i) < min) {
                min = targetFunc.get(i);
                indexOfColumn = i;
            }
        }

        //Вычисление базиса
        for (int i = 0; i < result.size(); i++) {
            basis.add(result.get(i) / fullMatrix.get(i).get(indexOfColumn));
        }

        //Находим минимальное число из базиса (если число =0 или < 0 его не учитываем)
        //В indexOfRow записывается индекс разрешающего столбца
        Double minOfBasis = 1000.0;
        Integer indexOfRow = 1000000000;
        for (int i = 0; i < basis.size(); i++) {
            if (basis.get(i) > 0) {
                if (basis.get(i) < minOfBasis) {
                    minOfBasis = basis.get(i);
                    indexOfRow = i;
                }
            }
        }


        double elemenOfRowColum = fullMatrix.get(indexOfRow).get(indexOfColumn);
        //Очищаем базис, так как он будет высчитываться снова
        basis.clear();

        //Перезаполняем матрицу с дополнительными переменными новыми значениями

        //для разрешающей строки делаем вычисления - каждый элемент разрешающей строки делим на разрешающий элемент -fullMatrix.get(indexOfRow).get(indexOfColumn)
        ArrayList<Double> fullRow = fullMatrix.get(indexOfRow);
        //Добавить в расчет данные из result
        fullRow.add(result.get(indexOfRow));

        for (int i = 0; i < fullRow.size(); i++) {
            fullRow.set(i, fullRow.get(i) / elemenOfRowColum);
        }

        //Вернем последний элемент в массив result и удалим его из fullRow
        result.set(indexOfRow, fullRow.get(fullRow.size()-1));
        fullRow.remove(fullRow.size() -1);

        //Перезаписываем в главной матрице с доп переменными разрешающую строку новыми значениями
        fullMatrix.set(indexOfRow, fullRow);
        System.out.println(fullRow);

        for (int i = 0; i < fullMatrix.size(); i++){
            ArrayList<Double> fillRow = new ArrayList<>();
            if (i != indexOfRow){
                for (int j = 0; j < fullMatrix.get(i).size(); j++){
                    fillRow.add((fullRow.get(j) * fullMatrix.get(i).get(indexOfColumn) * -1) +fullMatrix.get(i).get(j));
                }
                result.set(i,(result.get(indexOfRow) * fullMatrix.get(i).get(indexOfColumn) * -1) + result.get(i));
                fullMatrix.set(i,fillRow);
            }
        }
        
        System.out.println(fullMatrix);
        System.out.println(result);
    }


    public static ArrayList<ArrayList<Double>> addAdditionalElements(int yCount, ArrayList<ArrayList<Double>> matrixMain){
        ArrayList<ArrayList<Double>> fullMatrix = new ArrayList<ArrayList<Double>>();
        //Заполнение матрицы дополнительными переменными
        for (int i = 0; i < yCount; i++) {
            ArrayList<Double> fullRow = (ArrayList<Double>) matrixMain.get(i).clone();
            for (int j = 0; j < yCount; j++) {
                if (j == i) {
                    fullRow.add(1.0);
                } else {
                    fullRow.add(0.0);
                }
            }
            fullMatrix.add(fullRow);//заполнение матрицы дополнительными переменными
        }
        return fullMatrix;
    }


    /**
     * Метод для парсинга введенных строк в матрицу
     *
     * @param matrixStr
     * @return
     */
    private static ArrayList<ArrayList<Double>> parseMatrixFromString(String matrixStr) {
        String[] matrixRowsStr = matrixStr.split("\n");
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();

        for (String matrixRowStr : matrixRowsStr) {
            matrix.add(parseArrayFromString(matrixRowStr));
        }

        return matrix;
    }

    /**
     * Метод для парсинга введенных строк в массив
     *
     * @param arrayStr
     * @return
     */
    private static ArrayList<Double> parseArrayFromString(String arrayStr) {
        ArrayList<Double> array = new ArrayList<>();
        String[] arrayElementsStr = arrayStr.split(" ");

        for (String elementStr : arrayElementsStr) {
            array.add((double) Integer.parseInt(elementStr));
        }

        return array;
    }
}
