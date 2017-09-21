package com.satispay.instore.client.data;

import com.satispay.protocore.dh.UptimeMillisProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It might be important to implement the {@link UptimeMillisProvider} interface using the singleton pattern, depending on the client
 * implementor architecture.
 */
public class UptimeMillisProviderClientImpl implements UptimeMillisProvider {

    private static volatile UptimeMillisProviderClientImpl instance = new UptimeMillisProviderClientImpl();

    public static UptimeMillisProviderClientImpl getInstance() {
        return instance;
    }

    private UptimeMillisProviderClientImpl() {
    }

    @Override
    public long uptimeMillis() {
        // ==> Here is a simple example of retrieving the uptime millis of the terminal (Unix or Windows).
        long uptime = 10000L;
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Process uptimeProc = Runtime.getRuntime().exec("net stats srv");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("Statistics since")) {
                        SimpleDateFormat format = new SimpleDateFormat("'Statistics since' MM/dd/yyyy hh:mm:ss a");
                        Date boottime = format.parse(line);
                        uptime = System.currentTimeMillis() - boottime.getTime();
                        break;
                    }
                }
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                Process uptimeProc = Runtime.getRuntime().exec("uptime");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                String line = in.readLine();
                if (line != null) {
                    Pattern parse = Pattern.compile("((\\d+) days,)? (\\d+):(\\d+)");
                    Matcher matcher = parse.matcher(line);
                    if (matcher.find()) {
                        String _days = matcher.group(2);
                        String _hours = matcher.group(3);
                        String _minutes = matcher.group(4);
                        int days = _days != null ? Integer.parseInt(_days) : 0;
                        int hours = _hours != null ? Integer.parseInt(_hours) : 0;
                        int minutes = _minutes != null ? Integer.parseInt(_minutes) : 0;
                        uptime = (minutes * 60000) + (hours * 60000 * 60) + (days * 6000 * 60 * 24);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return uptime;
    }
}