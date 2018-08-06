/*
 쓰레드란? 
  - 하나의 프로세스내에 독립적으로 실행되는
    세부 실행단위
    
  실행원리-
    개발자는 원하는 로직을 쓰레드의 run에 명시하
    면 jvm에 의해 관리되면서 수행해준다..
 */
package thread;

public class ThreadTest {

	Thread t1;
	Thread t2;
	public ThreadTest() {
		//내부익명 클래스란? 
		//재사용가능성이 떨어지는 코드를 굳이
		//.java로 만들필요가 있나?....
		//이럴땐, 현재 개발중인 자바클래스내에
		//이름없는 클래스를 정의할 수 있다..
		//언제 많이 쓰나? 이벤트 구현시 이벤트리스너
		//연결할때 많이 씀..
		t1=new Thread() {
			public void run() {
				while(true) {
					System.out.println("★");	
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		t2=new Thread() {
			public void run() {
				while(true) {
					System.out.println("☆");	
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		//jvm에게 맡기자!!
		t1.start();//개발자 손아귀 벗어남...
		t2.start();
	}
	
	public static void main(String[] args) {	
		new ThreadTest();
	}
}


