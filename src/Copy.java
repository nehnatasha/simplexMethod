public class Copy {
    //инициализация строк и столбцов
    private int rows, cols;
    //инициализация симплекс-таблицы
    private float[][] table;
    //флаг для определения оптимальный план или нет
    private boolean solutionIsUnbounded = false;

    public static enum ERROR{
        NOT_OPTIMAL,
        IS_OPTIMAL,
        UNBOUNDED
    };

    public Copy(int numOfConstraints, int numOfUnknowns){
        //Количество строк
        rows = numOfConstraints+1;
        //Количество столбцов
        cols = numOfUnknowns+1;

        //Инициализация двумерной матрицы
        table = new float[rows][];
        for(int i = 0; i < rows; i++){
            table[i] = new float[cols];
        }
    }

    //Вывод симплекс-таблицы
    public void print(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                String value = String.format("%.2f", table[i][j]);
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    //Заполнение симплекс таблицы коэффициентами
    public void fillTable(float[][] data){
        for(int i = 0; i < table.length; i++){
            System.arraycopy(data[i], 0, this.table[i], 0, data[i].length);
        }
    }

    //вычисляет значения симплексной таблицы, до тех пор, пока не найдено оптимальное решение
    public ERROR compute(){
        //Проверка оптимальности плана
        //Если план уже оптимальный, возвращает об этом ошибку
        if(checkOptimality()){
            return ERROR.IS_OPTIMAL;
        }

        //Поиск столбца ввода
        int pivotColumn = findEnteringColumn();
        System.out.println("Pivot Column: "+pivotColumn);


        //Поиск исходящего значения
        float[] ratios = calculateRatios(pivotColumn);
        if(solutionIsUnbounded) {
            return ERROR.UNBOUNDED;
        }

        //Поиск наименьшего значения
        int pivotRow = findSmallestValue(ratios);
        //System.out.println("Pivot row: "+ pivotRow);

        //Формируем следующую таблицу для вычислений
        formNextTableau(pivotRow, pivotColumn);
        //Возвращаем сообщение, что план еще не оптимальный
        return ERROR.NOT_OPTIMAL;
    }

    //Формируем новую таблицу из предварительно вычисленных значений
    private void formNextTableau(int pivotRow, int pivotColumn){
        float pivotValue = table[pivotRow][pivotColumn];
        float[] pivotRowVals = new float[cols];
        float[] pivotColumnVals = new float[cols];
        float[] rowNew = new float[cols];

        // разделите все записи в сводной строке на записи в сводном столбце
        // получить запись в сводной строке
        System.arraycopy(table[pivotRow], 0, pivotRowVals, 0, cols);

        // получить запись в сводном столбце
        for(int i = 0; i < rows; i++)
            pivotColumnVals[i] = table[i][pivotColumn];

        // разделите значения в сводной строке на сводное значение
        for(int  i = 0; i < cols; i++)
            rowNew[i] =  pivotRowVals[i] / pivotValue;

        // вычтите из каждой другой строки
        for(int i = 0; i < rows; i++){
            if(i != pivotRow){
                for(int j = 0; j < cols; j++){
                    float c = pivotColumnVals[i];
                    table[i][j] = table[i][j] - (c * rowNew[j]);
                }
            }
        }

        // замените строку
        System.arraycopy(rowNew, 0, table[pivotRow], 0, rowNew.length);
    }

    // вычисляет коэффициенты сводных строк
    private float[] calculateRatios(int column){
        float[] positiveEntries = new float[rows];
        float[] res = new float[rows];
        int allNegativeCount = 0;
        for(int i = 0; i < rows; i++){
            if(table[i][column] > 0){
                positiveEntries[i] = table[i][column];
            }
            else{
                positiveEntries[i] = 0;
                allNegativeCount++;
            }
            //System.out.println(positiveEntries[i]);
        }

        if(allNegativeCount == rows)
            this.solutionIsUnbounded = true;
        else{
            for(int i = 0;  i < rows; i++){
                float val = positiveEntries[i];
                if(val > 0){
                    res[i] = table[i][cols -1] / val;
                }
            }
        }

        return res;
    }

    // находит следующий столбец ввода
    private int findEnteringColumn(){
        float[] values = new float[cols];
        int location = 0;

        int pos, count = 0;
        for(pos = 0; pos < cols-1; pos++){
            if(table[rows-1][pos] < 0){
                //System.out.println("negative value found");
                count++;
            }
        }

        if(count > 1){
            for(int i = 0; i < cols-1; i++)
                values[i] = Math.abs(table[rows-1][i]);
            location = findLargestValue(values);
        } else location = count - 1;

        return location;
    }


    // находит наименьшее значение в массиве
    private int findSmallestValue(float[] data){
        float minimum ;
        int c, location = 0;
        minimum = data[0];

        for(c = 1; c < data.length; c++){
            if(data[c] > 0){
                if(Float.compare(data[c], minimum) < 0){
                    minimum = data[c];
                    location  = c;
                }
            }
        }

        return location;
    }

    // находит наибольшее значение в массиве
    private int findLargestValue(float[] data){
        float maximum = 0;
        int c, location = 0;
        maximum = data[0];

        for(c = 1; c < data.length; c++){
            if(Float.compare(data[c], maximum) > 0){
                maximum = data[c];
                location  = c;
            }
        }

        return location;
    }

    // проверяет, является ли таблица оптимальной
    public boolean checkOptimality(){
        boolean isOptimal = false;
        int vCount = 0;

        for(int i = 0; i < cols-1; i++){
            float val = table[rows-1][i];
            if(val >= 0){
                vCount++;
            }
        }

        if(vCount == cols-1){
            isOptimal = true;
        }

        return isOptimal;
    }

    // возвращает симплексную таблицу
    public float[][] getTable() {
        return table;
    }
}

