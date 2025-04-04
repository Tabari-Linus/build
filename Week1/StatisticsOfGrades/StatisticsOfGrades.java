import java.util.Scanner;

public class StatisticsOfGrades {

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

    public static void display(String[] values){
        for(int j=0; j<values.length; j++){
            System.out.print(values[j]);
        }
    }

    public static void graph(int[] stats) {
        String[] axisLabel = {"I    0-20   ","I    21-40   ","I    41-60   ","I    61-80   ","I    81-100   "};
        String[] axisX = {"   +------------","+------------","+------------","+------------","+------------"};
        int maxStat = 0;
        for (int a = 0; a < stats.length; a++) {
            if (maxStat < stats[a])
                maxStat = stats[a];
        }
        for (int row = maxStat; row >= 1; row--) {
            System.out.print(row + " > ");
            for (int value : stats) {
                // Calculate how many # symbols to print for this bar
                if (value >= row) {
                    System.out.print(" ##########\t");
                } else {
                    System.out.print("           \t");
                }
            }
            System.out.println();
        }

        display(axisX);
        System.err.println();
        display(axisLabel);
        }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        System.out.print("Enter marks seperated by space :");
        String values[] = read.nextLine().trim().split("\\s");
        int N = values.length;
        double sumOfGrades = 0, averageGrade, minGrade;
        double maxGrade;
        double scores[] = new double[N];
        int gradeStats[] = new int[5];

        try {
            for (int i = 0; i < N; i++) {
                scores[i] = Double.parseDouble(values[i].trim());
                if (scores[i] < 0 || scores[i] > 100) {
                    throw new IllegalArgumentException("Grade score greater than 100 or less than 0.");
                }
                sumOfGrades += scores[i];

                if (scores[i] > 80)
                    gradeStats[0] += 1;
                else if (scores[i] > 60)
                    gradeStats[1] += 1;
                else if (scores[i] > 40)
                    gradeStats[2] += 1;
                else if (scores[i] > 20)
                    gradeStats[3] += 1;
                else
                    gradeStats[4] += 1;
            }

            averageGrade = sumOfGrades / N;
            maxGrade = getMaxMinGrade(scores)[0];
            minGrade = getMaxMinGrade(scores)[1];

            System.out.println("The maximum grade in the class is :" + maxGrade);
            System.out.println("The minimum grade in the class is :" + minGrade);
            System.out.println("The average grade in the class is :" + averageGrade);

            graph(gradeStats);
        } catch (NumberFormatException e) {
            System.out.println("Value is not a number.");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("An exception occured");
        }

    }
}