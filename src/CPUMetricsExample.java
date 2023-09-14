import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.management.OperatingSystemMXBean;

public class CPUMetricsExample {
    public static void main(String[] args) {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    Integer sum = 0;
                    for(int i=1000; i<5000; i++){   sum+=i; }
                    System.out.println(sum);
                    sum = 0;
                    for(int i = 0; i < 100; i++) {
                        if(i >= 10) sum +=2;
                        else sum +=1;
                    }
                    System.out.println(sum);
                }
            });
        }

        // 获取系统 CPU 时间
        double systemCpuTime = osBean.getSystemCpuLoad() * 100;
        System.out.println("System CPU Time: " + systemCpuTime + " %");

        // 获取进程 CPU 时间
        double processCpuTime = osBean.getProcessCpuLoad() * 100;
        System.out.println("Process CPU Time: " + processCpuTime + " %");
    }
}
