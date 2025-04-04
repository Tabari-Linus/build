import java.util.Scanner;

public class StatisticsOfGrades {

    //A method to return the maximum grade and the minimum grade from the list of grades
    public static double[] getMaxMinGrade(double[] grades) {
        double minGrade = grades[0];
        double maxGrade = grades[0];

        for (int j = 1; j < grades.length; j++) {
            if (maxGrade < grades[j])
                maxGrade = grades[j];

            if (minGrade > grades[j])
                minGrade = grades[j];

        }
        return new double[] { maxGrade, minGrade };
    }

    // A generic method that loops over a string array and display the content of the array
    public static void display(String[] values){
        for (String value : values) {
            System.out.print(value);
        }
    }

    // A method to print the statistical graph of the grade frequency
    public static void graph(int[] stats) {
        String[] axisLabel = {"   I    0-20   ","I    21-40   ","I    41-60   ","I    61-80   ","I    81-100   "};
        String[] axisX = {"   +------------","+------------","+------------","+------------","+------------"};
        int maxStat = 0;
        for (int a = 0; a < stats.length; a++) {
            if (maxStat < stats[a])
                maxStat = stats[a];
        }
        for (int row = maxStat; row >= 1; row--) {
            System.out.print(row + " > ");
            for (int value : stats) {
                
                if (value >= row) {
                    System.out.print("  ########## ");
                } else {
                    System.out.print("             ");
                }
            }
            System.out.println();
        }

        display(axisX);
        System.out.println();
        display(axisLabel);
        }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        System.out.print("Enter marks seperated by space :");
        String values[] = read.nextLine().trim().split("\\s+");
        int N = values.length;
        double sumOfGrades = 0, averageGrade, minGrade;
        double maxGrade;
        double scores[] = new double[N];
        int gradeStats[] = new int[5];


        try {
            for (int i = 0; i < N; i++) {
                scores[i] = Double.parseDouble(values[i]);
                if (scores[i] < 0 || scores[i] > 100) {
                    // Instruction to throw exception if values is greater 100 or less than 0
                    throw new IllegalArgumentException("Grade score greater than 100 or less than 0.");
                }

                sumOfGrades += scores[i];

                if (scores[i] > 80)
                    gradeStats[4] += 1;
                else if (scores[i] > 60)
                    gradeStats[3] += 1;
                else if (scores[i] > 40)
                    gradeStats[2] += 1;
                else if (scores[i] > 20)
                    gradeStats[1] += 1;
                else
                    gradeStats[0] += 1;
            }
            // calculating the average
            averageGrade = sumOfGrades / N;

            maxGrade = getMaxMinGrade(scores)[0];
            minGrade = getMaxMinGrade(scores)[1];

            System.out.println("Values:\n");
            System.out.println("The maximum grade in the class is :" + maxGrade);
            System.out.println("The minimum grade in the class is :" + minGrade);
            System.out.println("The average grade in the class is :" + averageGrade);

            // Displaying graph
            System.out.println("Graph: \n");
            graph(gradeStats);
        } catch (NumberFormatException e) {
            System.out.println("Value is not a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Grade should be between from 0 to 100");
        }
        catch(Exception e){
            System.out.println("An exception occured");
        }

    }
}