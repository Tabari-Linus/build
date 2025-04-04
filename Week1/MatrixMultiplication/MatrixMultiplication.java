
import java.util.Scanner;

public class MatrixMultiplication {

    // Method to obtain matrix from user input
    private static int[][] getMatrix(Scanner scanner, int rows, int cols) {
        int[][] matrix = new int[rows][cols];

        try {
            for (int i = 0; i < rows; i++) {
                System.out.println("Enter row of elements separated by space: ");
                String[] rowElements = scanner.nextLine().trim().split("[\\,\\.\\s]");
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = Integer.parseInt(rowElements[j]);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Value is not a number.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Number of elements entered for column not equal ");
        }

        return matrix;
    }

    // Method to display values in a 2D array or matrix
    private static void displayMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.print("|");
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println("|");
        }
    }

    // Method to get the dimension provied by the used separated by a comma 
    public static int[] getDimentions(String dimension) {
        int matrix2D[] = new int[2];
        try {
            String matrixDim[] = dimension.trim().split("[\\,\\s]");
            matrix2D[0] = Integer.parseInt(matrixDim[0].trim());
            matrix2D[1] = Integer.parseInt(matrixDim[1].trim());

        } catch (NumberFormatException e) {
            System.out.println("Value is not a number.");
        }
        return matrix2D;
    }

    // Method that perform matrix multiplication for two valid matrix 
    private static int[][] multiplyMatrices(int[][] mat1, int[][] mat2) {
        int rows1 = mat1.length;
        int cols1 = mat1[0].length;
        int cols2 = mat2[0].length;

        int[][] result = new int[rows1][cols2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        System.out.println("#########-------Matrix Multiplier-------#########\n");
        System.out.println("Enter the dimensions of the first matrix separated by ,:");
        String dimensions = read.nextLine();
        int numberOfRows = getDimentions(dimensions)[0];
        int numberOfCols = getDimentions(dimensions)[1];
        int matrix1[][] = getMatrix(read, numberOfRows, numberOfCols);

        System.out.println("\nEnter the dimensions of the second matrix separated by ,:");
        dimensions = read.nextLine();
        numberOfRows = getDimentions(dimensions)[0];
        numberOfCols = getDimentions(dimensions)[1];
        int matrix2[][] = getMatrix(read, numberOfRows, numberOfCols);

        try {
            if (matrix1[0].length != matrix2.length) {
                throw new IllegalArgumentException("Matrix multiplication is not possible. bacause the number of rows in the first matrics does not match the number of columns in the second matrics ");
            }

            int[][] resultMatrix = multiplyMatrices(matrix1, matrix2);

            displayMatrix(resultMatrix);

        } catch (IllegalArgumentException e) {
            System.out.println("Rows of matrix one doesn't match columns of matrix2");
        } catch (Exception e) {
            System.out.println("An exception occurred");
        }

    }
}
