要通过HTTP方式获取JMX metrics，你可以使用Java中的Servlet来实现。下面是一个简单的示例，演示如何创建一个Servlet来获取JMX metrics，并将其通过HTTP方式返回给客户端。

1. 创建一个Java类，命名为 `JmxMetricsServlet`，并继承自 `HttpServlet`：

```java
import javax.management.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JmxMetricsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        // JMX metrics query
        String metricsData = getJmxMetrics();

        // Write metrics data to response
        resp.getWriter().write(metricsData);
    }

    private String getJmxMetrics() {
        // Create a JMX connector to the local JVM
        // You may need to customize this part based on your use case
        MBeanServerConnection mBeanServer = ManagementFactory.getPlatformMBeanServer();

        // Replace with your MBean's ObjectName
        ObjectName objectName = null; // Replace with your MBean's ObjectName

        try {
            // Query the MBean to get metrics data
            // Customize this part based on the MBean and attributes you want to query
            String metricValue = String.valueOf(mBeanServer.getAttribute(objectName, "AttributeName"));

            return "JMX Metric: " + metricValue;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fetch JMX metric.";
        }
    }
}
```

2. 配置 `web.xml` 文件，将 `JmxMetricsServlet` 映射到一个URL路径，例如 `/jmxmetrics`：

```xml
<servlet>
    <servlet-name>JmxMetricsServlet</servlet-name>
    <servlet-class>com.example.JmxMetricsServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>JmxMetricsServlet</servlet-name>
    <url-pattern>/jmxmetrics</url-pattern>
</servlet-mapping>
```

3. 构建并部署你的Web应用程序，确保Servlet容器（例如Tomcat）能够访问JMX metrics。当访问 `http://your-server:port/your-app-context/jmxmetrics` 时，将会返回获取的JMX metrics数据。

注意：这只是一个简单的示例，实际情况可能更复杂，特别是在配置和获取JMX数据方面。你需要根据实际情况进行适当的调整和扩展，确保安全性和正确性。