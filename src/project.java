import java.util.ArrayList;
import java.util.List;

public class project {
    public static void main(String[] args) {

        ArrayList<Integer> bi = new ArrayList<>();
        ArrayList<Integer> basis = new ArrayList<>(); // bi/разрещающий столбец
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>(); //главный массив.

        String matrixStr = "4 1 1 0 8\n-1 1 0 1 3\n-3 -4 0 0 0";
        String[] matrixRowsStr = matrixStr.split("\n");

        for (String matrixRowStr : matrixRowsStr) {
            String[] matrixRowArr = matrixRowStr.split(" ");
            ArrayList<Integer> matrixRow = new ArrayList<>();
            for (String elStr : matrixRowArr) {
                matrixRow.add(Integer.parseInt(elStr));
            }
            matrix.add(matrixRow);
        }

        while (!isOptimal(matrix)) {
            //заполнение столбца bi

            bi.clear();
            for (int i = 0; i < matrix.size(); i++){
                var lastIndex = matrix.get(i).size()-1;
                bi.add(matrix.get(i).get(lastIndex));
            }

            //находим индекс разрешающего столбца
            var row = matrix.get(matrix.size()-1);
            var minColumn = minInStr(row);

            //записываем в разрешающий столбец значения
            for (int i = 0; i < matrix.size(); i++) {
                basis.add(matrix.get(i).get(minColumn));
            }

            //вычисляем bi/разрещающий столбец
            var biDelAllowCol = allowColumn(bi, basis);

            //находим минимальный элемент из bi/разрещающий столбец
            var minRow = minInStr(biDelAllowCol);

            //заполнЯем разрешающюю строку значениями
            ArrayList<Integer> allowStr = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                allowStr.add(matrix.get(minRow).get(i));
            }

            var factor = computFactor(allowStr, minColumn);

            for (int i = 0; i < matrix.size(); i++) {
                if (i != minRow) {
                    matrix.set(i, newRow(matrix.get(i), allowStr, factor));
                }
            }

            System.out.println(matrix);
            System.out.println(bi);
            System.out.println(biDelAllowCol);
            System.out.println(allowStr);
            System.out.println(basis);
            System.out.println(matrix.get(matrix.size()-1));

        }
    }


    //поиск минимального элемента в строке
    public static int minInStr(List<Integer> row) {
        var min = 10_000;
        var numRow = 0;
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i) < min && row.get(i) != 0) {
                min = row.get(i);
                numRow = i;
            }
        }
        return numRow;
    }

    //рассчет столбца = bi/разрещающий столбец
    public static List<Integer> allowColumn(ArrayList<Integer> bi, ArrayList<Integer> column) {
        List<Integer> allowCol = new ArrayList<>();
        for (int i = 0; i < bi.size(); i++) {
            try {
                allowCol.add(bi.get(i) / column.get(i));
            } catch (ArithmeticException e) {
                allowCol.add(0);
            }
        }
        return allowCol;
    }

    //рассчет множителя для преобразования матрицы
    public static int computFactor(ArrayList<Integer> bi, int column) {
        var factor = bi.get(column);
        return factor;
    }

    //рассчет новой строки матрицы после преобразования
    public static ArrayList<Integer> newRow(ArrayList<Integer> row, ArrayList<Integer> allowStr, int factor) {
        ArrayList<Integer> newRow = new ArrayList<>();
        for (int i = 0; i < row.size(); i++) {
            newRow.add(row.get(i) / factor - allowStr.get(i));
        }
        return newRow;
    }

    public static boolean isOptimal(ArrayList<ArrayList<Integer>> matrix) {
        // проверка оптимального плана
        var row = matrix.get(matrix.size() - 1);
        boolean optimal = true;
        for (Integer integer : row) {
            if (integer < 0) {
                optimal = false;
                break;
            }
        }
        return optimal;
    }
}

