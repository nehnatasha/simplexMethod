public class Main {
    public static void main(String[] args) {
        Simplex simplex = new Simplex(2,2);
        float[][] data = {{1}, {2}};
        simplex.fillTable(data);
    }

}
