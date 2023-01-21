import java.util.Scanner;

class Main {

    public static int[] createArray(int size, Scanner sc) {

        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = sc.nextInt();
        }
        return array;
    }

    public static boolean arrayContainsNumber(int[] array, int number) {
        boolean inArray = false;
        for (int val: array) {
            if (val == number) {
                inArray = true;
                break;
            }
        }
        return inArray;
    }

    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(); // array size
        int[] array = createArray(n, scanner);
        int number = scanner.nextInt();

        System.out.println(arrayContainsNumber(array, number));

    }
}