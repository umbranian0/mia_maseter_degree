package MIA_01;
 

public class example_01 {

	public static void main(String[] args) throws InterruptedException{
		// TODO Auto-generated method stub
		
        class ThreadExample extends Thread{
            public void run(){
                System.out.println("hello from new thread");
            }
        }
        ThreadExample t1 = new ThreadExample();
        //deamon = tarefas/threads a auxiliar threads (usadas no fundo por um utilizador)
        t1.setDaemon(true);
        t1.start();
        Thread.yield();
        //join espera todas as threads acabarem para fazer um processo sequencial
        t1.join();
        System.out.println("hello from main thread");
//		    
	}

}
