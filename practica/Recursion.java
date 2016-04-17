class Recursion {
	public static int Factorial(int n) {
		if (n<0) {
			return 0;
		} else if (n==0) {
			return 1;
		} else {
			return n*Factorial(n-1);
		}
	}

	public static int Fibonacci(int n) {
		if (n<=1) {
			return 1;
		} else {
			return Fibonacci(n-1)+Fibonacci(n-2);
		}
	}

	public static double Syracuse(int n) {
		if (n==1) {
			return 1;
		} else {
			if (n%2==0) return 1+Syracuse(n/2);
			else return (1+Syracuse(3*n+1)/2);
		}
	}

	public static void main(String[] args) {

	}
}