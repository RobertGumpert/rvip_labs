package concurrency;

public class CommonResource {

    public CommonResource(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] matrix;


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : this.matrix) {
            for (int element : row) {
                stringBuilder.append(element);
                stringBuilder.append(", ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String rowToString(int indexOfRow) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int element : this.matrix[indexOfRow]) {
            stringBuilder.append(element);
            stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }
}
