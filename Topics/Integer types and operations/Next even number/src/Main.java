import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int a;
        int b;

        int x = scanner.nextInt();
        if (x % 2 == 0) {
            a = x + 2;
            System.out.println(a);
        } else {
            b = x + 1;
            System.out.println(b);
        }
    }
}
