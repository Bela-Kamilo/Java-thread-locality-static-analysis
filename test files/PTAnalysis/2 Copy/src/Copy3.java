public class Copy3 {

	public void a(A d, int c){
    A x = new A();
    A y = new A();
    A z = c==0 ? x : y;
    z.use();
  }
}
