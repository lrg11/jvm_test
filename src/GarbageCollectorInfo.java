import java.lang.management.ManagementFactory;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GarbageCollectorInfo {
    public static void main(String[] args) {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    Integer sum = 0;
                    for(int i=1000; i<5000; i++){   sum+=i; }
                    System.out.println(sum);
                }
            });
        }




        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("Name: " + gcBean.getName());
            System.out.println("Collection count: " + gcBean.getCollectionCount());
            System.out.println("Collection time: " + gcBean.getCollectionTime() + " ms");
            System.out.println();
        }
        executor.shutdown();
    }
}
