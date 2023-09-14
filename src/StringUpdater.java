import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class StringUpdater {
    private AtomicReference<String> sharedString = new AtomicReference<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public StringUpdater(String initialString) {
        sharedString.set(initialString);
    }

    public void startUpdater() {
        // 每分钟检查一次是否需要更新
        scheduler.scheduleAtFixedRate(this::checkForUpdate, 0, 1, TimeUnit.MINUTES);
    }

    public String getString() {
        return sharedString.get();
    }

    private void checkForUpdate() {
        String currentString = sharedString.get();
        Date expirationTime = extractExpirationTime(currentString);

        if (expirationTime != null && isUpdateNeeded(expirationTime)) {
            // 在过期前5分钟内触发更新
            updateString();
        }
    }

    private boolean isUpdateNeeded(Date expirationTime) {
        Date now = new Date();
        long timeDiffMillis = expirationTime.getTime() - now.getTime();
        return timeDiffMillis <= TimeUnit.MINUTES.toMillis(5);
    }

    private void updateString() {
        // 在这里实现更新逻辑，例如从远程服务获取新的字符串
        String updatedString = fetchUpdatedStringFromService();
        sharedString.set(updatedString);
        System.out.println("String updated: " + updatedString);
    }

    private Date extractExpirationTime(String str) {
        // 从字符串中提取时间部分并转换为Date对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            int startIndex = str.indexOf("ExpirationTime:") + 15;
            String timeStr = str.substring(startIndex, startIndex + 19); // 假设时间格式为"yyyy-MM-dd HH:mm:ss"
            return dateFormat.parse(timeStr);
        } catch (ParseException e) {
            return null;
        }
    }

    // 模拟从远程服务获取新的字符串
    private String fetchUpdatedStringFromService() {
        // 这里可以实现与远程服务的通信逻辑
        // 在示例中，我们简单地返回一个随机的字符串
        return "UpdatedString-ExpirationTime:" + getFutureTime(10); // 假设10分钟后过期
    }

    private static String getFutureTime(int minutes) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date futureTime = new Date(now.getTime() + minutes * 60 * 1000); // 当前时间加上指定分钟数
        return dateFormat.format(futureTime);
    }

    public static void main(String[] args) {
        StringUpdater updater = new StringUpdater("InitialString-ExpirationTime:" + getFutureTime(15)); // 初始字符串设置为15分钟后过期
        updater.startUpdater();

        // 创建一个读取线程来定期读取字符串
        ScheduledExecutorService readerExecutor = Executors.newScheduledThreadPool(1);
        readerExecutor.scheduleAtFixedRate(() -> {
            String currentValue = updater.getString();
            System.out.println("Current String: " + currentValue);
        }, 0, 1, TimeUnit.SECONDS); // 每秒读取一次

        // 让应用程序运行一段时间
        try {
            Thread.sleep(60000); // 运行60秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 停止更新和读取线程
        updater.scheduler.shutdown();
        readerExecutor.shutdown();
    }
}
