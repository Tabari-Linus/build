import java.util.Scanner;

public class MatrixMultiplication {

    private static int[][] getMatrix(Scanner scanner, int rows, int cols) {
        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.out.println("Enter row of elements separated by space: ");
            String[] rowElements = scanner.nextLine().trim().split("[,\\.\\s]");
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Integer.parseInt(rowElements[j]);
            }
        }
        return matrix;
    }

    private static void displayMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.print("|");
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println("|");
        }
    }

    public static int[] getDimentions(String dimension) {
        int matrix2D[] = new int[2];
        try {
            String matrixDim[] = dimension.trim().split("[,\\.\\s]");
            matrix2D[0] = Integer.parseInt(matrixDim[0]);
            matrix2D[1] = Integer.parseInt(matrixDim[1]);

        } catch (NumberFormatException e) {
            System.out.println("Value is not a number.");
        }
        return new int[]{matrix2D[0], matrix2D[1]};
    }

    private static int[][] multiplyMatrices(int[][] mat1, int[][] mat2) {
        int rowsA = mat1.length;
        int colsA = mat1[0].length;
        int colsB = mat2[0].length;

        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        System.out.println("#########-------Matrix Multiplier-------#########");
        System.out.println("Enter the dimensions of the first matrix separated by ,:");
        String dimensions = read.nextLine();
        int numberOfRows = getDimentions(dimensions)[0];
        int numberOfCols = getDimentions(dimensions)[1];
        int matrix1[][] = getMatrix(read, numberOfRows, numberOfCols);

        System.out.println("Enter the dimensions of the second matrix separated by ,:");
        dimensions = read.nextLine();
        numberOfRows = getDimentions(dimensions)[0];
        numberOfCols = getDimentions(dimensions)[1];
        int matrix2[][] = getMatrix(read, numberOfRows, numberOfCols);

        int[][] resultMatrix = multiplyMatrices(matrix1, matrix2);

        displayMatrix(resultMatrix);

    }
}
