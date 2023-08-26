import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClassLoadingMetricsExample {
    public static void main(String[] args) {
        // 获取类加载管理器
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

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

        // 获取已加载的类数量
        int loadedClassCount = classLoadingMXBean.getLoadedClassCount();
        System.out.println("Loaded Class Count: " + loadedClassCount);

        // 获取总共加载的类数量
        long totalLoadedClassCount = classLoadingMXBean.getTotalLoadedClassCount();
        System.out.println("Total Loaded Class Count: " + totalLoadedClassCount);

        // 获取卸载的类数量
        long unloadedClassCount = classLoadingMXBean.getUnloadedClassCount();
        System.out.println("Unloaded Class Count: " + unloadedClassCount);

        // 打印类加载和卸载的次数
        System.out.println("Class Loading Count: " + classLoadingMXBean.getLoadedClassCount());
        System.out.println("Class Unloading Count: " + classLoadingMXBean.getUnloadedClassCount());
    }
}
