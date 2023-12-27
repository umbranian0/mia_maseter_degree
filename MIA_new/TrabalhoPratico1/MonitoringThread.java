package TrabalhoPratico1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

class MonitoringThread extends Thread {
    private final ExecutorService producerExecutor;
    private final ExecutorService consumerExecutor;
    private final Map<Runnable, Long> lastExecutionTimes = new ConcurrentHashMap<>();
    public MonitoringThread(ExecutorService producerExecutor, ExecutorService consumerExecutor) {
        this.producerExecutor = producerExecutor;
        this.consumerExecutor = consumerExecutor;
    }

    
    public void run() {
        while (true) {
            // Verifica se algum produtor ou consumidor está inativo e reinicia
            checkAndRestartThreads(producerExecutor, 1);
            checkAndRestartThreads(consumerExecutor, 3);

            try {
                Thread.sleep(1000); // Verifica a cada 1 segundo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkAndRestartThreads(ExecutorService executorService, int secondsThreshold) {
        for (Runnable task : ((ThreadPoolExecutor) executorService).getQueue()) {
            long lastExecutionTime = lastExecutionTimes.getOrDefault(task, 0L);;
            long currentTime = System.currentTimeMillis();

            if (lastExecutionTime != 0 && currentTime - lastExecutionTime > secondsThreshold * 1000) {
                // A tarefa está inativa há mais de secondsThreshold segundos
                // Reinicia a tarefa (substitui por uma nova)
                executorService.execute(task);
                System.out.println("executin new task - " + task.toString());
            }
        }
    }

}
