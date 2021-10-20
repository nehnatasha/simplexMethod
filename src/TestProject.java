public class TestProject {
    public static void main(String[] args) {
        int[][] matrixA = {{4, 1}, {-1, 1}, {3, 4}, {8,3}}; //система уравнений
        int[][] matrixB = new int[3][4]; // добавляем столбец из доп переменных
        int[][] matrixC = new int[3][5];; // ограничения неравенств

        int[][] matrixResult = new int[1][2]; //матрица для вычисления

        for (int i = 0; i < 3; i++) {  //идём по строкам
            for (int j = 0; j < 2; j++) {//идём по столбцам
                System.out.print(matrixA[i][j] + " "); //вывод элемента
            }
            System.out.println();//перенос строки ради визуального сохранения табличной формы
        }

        System.out.println();//перенос строки ради визуального сохранения табличной формы

        matrixB[0][2] = 1;
        matrixB[0][3] = 0;

        matrixB[1][2] = 0;
        matrixB[1][3] = 1;

        matrixB[2][2] = 0;
        matrixB[2][3] = 0;

        for (int i = 0; i < 3; i++) {  //идём по строкам
            for (int j = 0; j < 2; j++) {//идём по столбцам
                matrixB[i][j] = matrixA[i][j];
                System.out.print(matrixB[i][j] + " "); //вывод элемента
            }
            System.out.println();//перенос строки ради визуального сохранения табличной формы
        }

        System.out.println();//перенос строки ради визуального сохранения табличной формы

        for (int i = 0; i < 3; i++) {  //идём по строкам
            for (int j = 0; j < 4; j++) {//идём по столбцам
                System.out.print(matrixB[i][j] + " "); //вывод элемента
            }
            System.out.println();//перенос строки ради визуального сохранения табличной формы
        }

        for (int i = 0; i < 3; i++) {  //идём по строкам
            for (int j = 0; j < 4; j++) {//идём по столбцам
                matrixC[i][j] = matrixB[i][j];
                System.out.print(matrixB[i][j] + " "); //вывод элемента
            }
            System.out.println();//перенос строки ради визуального сохранения табличной формы
        }
        System.out.println();//перенос строки ради визуального сохранения табличной формы

        matrixC[0][4] = 8;
        matrixC[1][4] = 3;

        for (int i = 0; i < 3; i++) {  //идём по строкам
            for (int j = 0; j < 5; j++) {//идём по столбцам
                System.out.print(matrixC[i][j] + " "); //вывод элемента
            }
            System.out.println();//перенос строки ради визуального сохранения табличной формы
        }

    }
}
