package test;

public class B extends A{
	
	public B(){
		test();
	}
	
	public void test(){
		System.out.println("test B");
	}
	
	public static void main(String[] args) {
		A a = new B();
	}

}
