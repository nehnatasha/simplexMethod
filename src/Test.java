import java.util.ArrayList;
//сайт алгоритма - http://galyautdinov.ru/post/proizvodstvennaya-zadacha-simpleks-metod

public class Test {

    public static void main(String[] args) {

        //Первоначальный массив
        String matrixStr = "4 1\n-1 1";
        ArrayList<ArrayList<Integer>> matrixMain = parseMatrixFromString(matrixStr);
        int yCount = matrixMain.size();

        //Значения равенств уравнений
        String resultStr = "8 3";
        ArrayList<Integer> result = parseArrayFromString(resultStr);
        ArrayList<ArrayList<Integer>> fullMatrix = new ArrayList<ArrayList<Integer>>();

        //Заполнение матрицы дополнительными переменными
        for (int i = 0; i < yCount; i++){
            ArrayList<Integer> fullRow = (ArrayList<Integer>) matrixMain.get(i).clone();
            for (int j = 0; j < yCount; j++){
                if (j == i){
                    fullRow.add(1);
                } else {
                    fullRow.add(0);
                }
            }
            fullMatrix.add(fullRow);//заполнение матрицы дополнительными переменными
        }

        //Значения целевой функции
        String targetFuncStr = "3 4";
        ArrayList<Integer> targetFunc = parseArrayFromString(targetFuncStr);

        //Значения целевой функции умножаем на -1
        for (int i = 0; i < targetFunc.size(); i++){
            targetFunc.set(i, targetFunc.get(i) * -1);
        }

        //Добавляем к целевой функции дополнительные переменные
        for (int i = 0; i < yCount; i++){
            targetFunc.add(0);
        }

        //Инициализация массива, где буду храниться базисы
        ArrayList<Integer> basis = new ArrayList<>();

        //Из targetFunc находим наименьшее число
        //В indexOfColumn записывается индекс разрешающего столбца
        int min = 100000000;
        int indexOfColumn = 10000000;
        for (int i =0; i < targetFunc.size(); i ++){
            if (targetFunc.get(i) < min){
                min = targetFunc.get(i);
                indexOfColumn = i;
            }
        }

        //Вычисление базиса
        for (int i = 0; i < result.size(); i++){
            basis.add(result.get(i) / fullMatrix.get(i).get(indexOfColumn));
        }

        //Находим минимальное число из базиса (если число =0 или < 0 его не учитываем)
        //В indexOfRow записывается индекс разрешающего столбца
        int minOfBasis = 100000000;
        int indexOfRow = 1000000000;
        for (int i = 0; i < basis.size(); i++){
            if (basis.get(i) > 0){
                if (basis.get(i) < minOfBasis){
                    minOfBasis = basis.get(i);
                    indexOfRow = i;
                }
            }
        }

        //Очищаем базис, так как он будет высчитываться снова
        basis.clear();

        //Перезаполняем матрицу с дополнительными переменными новыми значениями

        //для разрешающей строки делаем вычисления - каждый элемент разрешающей строки делим на разрешающий элемент -fullMatrix.get(indexOfRow).get(indexOfColumn)
        ArrayList<Integer> fullRow = fullMatrix.get(indexOfRow);
        for (int i = 0; i < fullRow.size(); i++){
            fullRow.set(i, fullRow.get(i) / fullMatrix.get(indexOfRow).get(indexOfColumn));
        }

        //Перезаписываем в главной матрице с доп переменными разрешающую строку новыми значениями
        fullMatrix.set(indexOfRow, fullRow);

        Integer newResult = 0;
        for (int i = 0; i < fullMatrix.size(); i++){
            ArrayList<Integer> fillRow = new ArrayList<>();
            if (i != indexOfRow){
                for (int j = 0; j < fullMatrix.get(i).size(); j++){
                    fillRow.add(fullRow.get(j) * fullMatrix.get(i).get(indexOfColumn) * -1 + fullMatrix.get(i).get(j));
//                    result.get(i) * -1 /
                }
                fullMatrix.set(i, fillRow);

                //Здесь высчитывается значения для результата неравенства, который относится к разрешающей строке и его новое значение записывается вместо старого
//                newResult = result.get(i) / fullMatrix.get(indexOfRow).get(indexOfColumn) * -1;
//                result.set(i, newResult);
            }
        }
        System.out.println(fullMatrix);
        System.out.println(fullRow);
        System.out.println(targetFunc);

    }

    /**
     *Метод для парсинга введенных строк в матрицу
     *
     * @param matrixStr
     * @return
     */
    private static ArrayList<ArrayList<Integer>> parseMatrixFromString(String matrixStr) {
        String[] matrixRowsStr = matrixStr.split("\n");
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

        for (String matrixRowStr : matrixRowsStr) {
            matrix.add(parseArrayFromString(matrixRowStr));
        }

        return matrix;
    }

    /**
     *Метод для парсинга введенных строк в массив
     *
     * @param arrayStr
     * @return
     */
    private static ArrayList<Integer> parseArrayFromString(String arrayStr) {
        ArrayList<Integer> array = new ArrayList<>();
        String[] arrayElementsStr = arrayStr.split(" ");

        for (String elementStr : arrayElementsStr) {
            array.add(Integer.parseInt(elementStr));
        }

        return array;
    }
}
