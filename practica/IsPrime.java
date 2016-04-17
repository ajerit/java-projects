class IsPrime {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Debe pasar el numero por linea de comando.");
			System.exit(1);
		}

		long k=0,n=Long.parseLong(args[0]);
		boolean isPrime = true;

		if (n==1 || (n>2 && n%2 == 0) || (n>3 && n%3==0)) {
			isPrime = false;
		} else {
			k=(long)(Math.sqrt(n)+1);
			for (long i=5; i<k;i=i+6) {
				if ((n%i == 0) || n%(i+2)==0) {
					isPrime = false;
					break;
				}
			}
		}

		if (isPrime) {
			System.out.println("Number "+n+" is prime.");
		} else {
			System.out.println("Number "+n+" is not prime.");			
		}
	}
}