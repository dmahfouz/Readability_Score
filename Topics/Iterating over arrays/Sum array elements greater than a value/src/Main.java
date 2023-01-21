import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] values = new int[n];

        // collect integer values
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
        }

        int threshold = scanner.nextInt();
        int sum = 0;

        for (int num: values) {
            sum += num > threshold ? num : 0;
        }

        System.out.println(sum);







    }
}