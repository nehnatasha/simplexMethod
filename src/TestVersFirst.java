import java.util.ArrayList;

public class TestVersFirst {

    public static void main(String[] args) {

        //Первоначальный массив
        String matrixStr = "3 4 2\n4 3 3\n5 3 5";
        ArrayList<ArrayList<Double>> matrixMain = parseMatrixFromString(matrixStr);
        int yCount = matrixMain.size();

        //Значения равенств уравнений
        String resultStr = "1020 940 1010";
        ArrayList<Double> result = parseArrayFromString(resultStr);
        ArrayList<ArrayList<Double>> fullMatrix = addAdditionalElements(yCount, matrixMain);

        //Значения целевой функции
        String targetFuncStr = "10 12 8";
        ArrayList<Double> targetFunc = parseArrayFromString(targetFuncStr);
        //Маскимальная прибыль
        Double profit = 0.0;

        //Проверка оптимальности плана
        for (Double elemet : targetFunc){
            if (elemet < 0){
                System.out.println("План не оптимальный - рассчитываем");

            }
            else{
                System.out.println("План оптимальный");
                //нужно вывести значения базиса и сами базисы
            }
        }





        multiplTargetFunc(targetFunc);
        addForTargetFuncAdditionalElement(yCount, targetFunc);

        //Из targetFunc находим наименьшее число
        //В indexOfColumn записывается индекс разрешающего столбца
        int indexOfColumn = findMinElementFromBasis(targetFunc);
        //Вычисление базиса
        ArrayList<Double> basis = calculateBasis(result, fullMatrix, indexOfColumn);

        //Находим минимальное число из базиса (если число =0 или < 0 его не учитываем)
        //В indexOfRow записывается индекс разрешающего столбца
        int indexOfRow = findMinElemenInBasis(basis);

        double elementOfRowColumn = fullMatrix.get(indexOfRow).get(indexOfColumn);

        //Очищаем базис, так как он будет высчитываться снова
        basis.clear();

        //Перезаполняем матрицу с дополнительными переменными новыми значениями
        //для разрешающей строки делаем вычисления - каждый элемент разрешающей строки делим на разрешающий элемент -fullMatrix.get(indexOfRow).get(indexOfColumn)
        ArrayList<Double> fullRow = calculateAllowRow(fullMatrix, indexOfRow, elementOfRowColumn, result);

        //Вернем последний элемент в массив result и удалим его из fullRow
        result.set(indexOfRow, fullRow.get(fullRow.size()-1));
        fullRow.remove(fullRow.size() -1);

        //Перезаписываем в главной матрице с доп переменными разрешающую строку новыми значениями
        fullMatrix = rewriteMainMatrix(fullMatrix, indexOfRow, fullRow, indexOfColumn, result);

        //Вычисляем максимальную прибыль
        profit += result.get(indexOfRow) * targetFunc.get(indexOfColumn) * -1;

        //Вычисляем новые значения для целевой функции
        targetFunc = rewriteTargetFunc(targetFunc, fullRow, indexOfColumn);

        

        System.out.println(profit);
        System.out.println(targetFunc);
        System.out.println(fullMatrix);
        System.out.println(result);
    }


    /**
     * Метод для Заполнение матрицы дополнительными переменными
     *
     * @param yCount,matrixMain
     * @return
     */
    public static ArrayList<ArrayList<Double>> addAdditionalElements(int yCount, ArrayList<ArrayList<Double>> matrixMain){
        ArrayList<ArrayList<Double>> fullMatrix = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < yCount; i++) {
            ArrayList<Double> fullRow = (ArrayList<Double>) matrixMain.get(i).clone();
            for (int j = 0; j < yCount; j++) {
                if (j == i) {
                    fullRow.add(1.0);
                } else {
                    fullRow.add(0.0);
                }
            }
            fullMatrix.add(fullRow);
        }
        return fullMatrix;
    }

    /**
     * Значения целевой функции умножаем на -1
     * @param targetFunc
     * @return
     */
    public static ArrayList<Double> multiplTargetFunc(ArrayList<Double> targetFunc){
        for (int i = 0; i < targetFunc.size(); i++) {
            targetFunc.set(i, targetFunc.get(i) * -1);
        }
        return targetFunc;
    }

    /**
     * Добавляем к целевой функции дополнительные переменные
     * @param yCount,targetFunc
     * @return
     */
    public static ArrayList<Double> addForTargetFuncAdditionalElement(int yCount, ArrayList<Double> targetFunc){
        for (int i = 0; i < yCount; i++) {
            targetFunc.add(0.0);
        }
        return targetFunc;
    }

    /**
     * Из targetFunc находим наименьшее число
     * В indexOfColumn записывается индекс разрешающего столбца
     * @param targetFunc
     * @return
     */
    public static int findMinElementFromBasis(ArrayList<Double> targetFunc) {
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
        return indexOfColumn;
    }

    /**
     * Вычисление базиса
     * @param result
     * @param fullMatrix
     * @param indexOfColumn
     * @return
     */
    public static ArrayList<Double> calculateBasis(ArrayList<Double> result, ArrayList<ArrayList<Double>> fullMatrix, int indexOfColumn){
        ArrayList<Double> basis = new ArrayList<>();
        //Вычисление базиса
        for (int i = 0; i < result.size(); i++) {
            basis.add(result.get(i) / fullMatrix.get(i).get(indexOfColumn));
        }
        return basis;
    }

    /**
     * Находим минимальное число из базиса (если число =0 или < 0 его не учитываем)
     * В indexOfRow записывается индекс разрешающего столбца
     * @param basis
     * @return
     */
    public static Integer findMinElemenInBasis(ArrayList<Double> basis){
        Double minOfBasis = basis.get(0);
        Integer indexOfRow = 0;
        for (int i = 0; i < basis.size(); i++) {
            if (basis.get(i) > 0) {
                if (basis.get(i) < minOfBasis) {
                    minOfBasis = basis.get(i);
                    indexOfRow = i;
                }
            }
        }
        return indexOfRow;
    }

    /**
     * для разрешающей строки делаем вычисления - каждый элемент разрешающей строки делим на разрешающий элемент -fullMatrix.get(indexOfRow).get(indexOfColumn)
     * @param fullMatrix
     * @param indexOfRow
     * @param elementOfRowColumn
     * @param result
     * @return
     */
    public static ArrayList<Double> calculateAllowRow(ArrayList<ArrayList<Double>> fullMatrix, int indexOfRow, double elementOfRowColumn, ArrayList<Double> result){
        ArrayList<Double> fullRow = fullMatrix.get(indexOfRow);
        //Добавить в расчет данные из result
        fullRow.add(result.get(indexOfRow));
        for (int i = 0; i < fullRow.size(); i++) {
            fullRow.set(i, fullRow.get(i) / elementOfRowColumn);
        }
        return fullRow;
    }

    /**
     * Перезаписываем в главной матрице с доп переменными разрешающую строку новыми значениями
     * @param fullMatrix
     * @param indexOfRow
     * @param fullRow
     * @param indexOfColumn
     * @param result
     * @return
     */
    public static ArrayList<ArrayList<Double>> rewriteMainMatrix(ArrayList<ArrayList<Double>> fullMatrix, int indexOfRow, ArrayList<Double> fullRow, int indexOfColumn, ArrayList<Double> result){
        fullMatrix.set(indexOfRow, fullRow);
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
        return fullMatrix;
    }

    /**
     * //Вычисляем новые значения для целевой функции
     * @param targetFunc
     * @param fullRow
     * @param indexOfColumn
     * @return
     */
    public static ArrayList<Double> rewriteTargetFunc(ArrayList<Double> targetFunc, ArrayList<Double> fullRow, int indexOfColumn){
        ArrayList<Double> test = new ArrayList<>();
        for (int i = 0; i < targetFunc.size(); i++){
            test.add((fullRow.get(i) * targetFunc.get(indexOfColumn) * -1) +targetFunc.get(i));
        }
        targetFunc.clear();
        targetFunc = (ArrayList<Double>) test.clone();
        return targetFunc;
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
