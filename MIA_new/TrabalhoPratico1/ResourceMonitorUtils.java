package TrabalhoPratico1;

import java.lang.management.ManagementFactory;
import java.util.Random;

import com.sun.management.OperatingSystemMXBean;

public class ResourceMonitorUtils
{
    private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    private static Random randomGenerator = new Random();

    public static double getCpuLoad()
    {
        // This method returns the real CPU load.
        double cpuLoad = operatingSystemMXBean.getCpuLoad();

        // If the CPU load is invalid, an exception is thrown.
        if (cpuLoad < 0.0)
            throw new RuntimeException("Invalid CPU load.");
        return cpuLoad;
    }

    private static double simulatePercentage(String errorMessage)
    {
        // There is a 5% probability that an exception may occur.
        if (randomGenerator.nextDouble() < 0.05)
            throw new RuntimeException(errorMessage);

        return randomGenerator.nextDouble();
    }

    public static double getFreeRAM()
    {
        // This method simulates the free RAM percentage.
        return simulatePercentage("Invalid free RAM percentage.");
    }

    public static double getFreeDiskSpace()
    {
        // This method simulates the free disk space percentage.
        return simulatePercentage("Invalid free disk space percentage.");
    }

}
