package networkmodeling.core;

import java.util.LinkedList;

public class NetworkModelTestResult {
    
    public NetworkModelTestResult(boolean isTestPassed,
            LinkedList<LinkedList<NetworkDevice>> testRoutes)
    {
        this.isTestPassed = isTestPassed;
        this.testRoutes = testRoutes;
        this.testLog = generateTestLog();
    }
    
    private String[] generateTestLog()
    {
        String log[] = new String[testRoutes.size()];
        
        int logStringsNumber = log.length;
        
        for(int i=0; i< logStringsNumber; i++)
        {
            LinkedList<NetworkDevice> currentRoute = testRoutes.pop();
        
            if(currentRoute.peekFirst() instanceof NIC && 
                    currentRoute.peek() instanceof NIC)
            {
                log[i] = "\nSender: NIC with Ip: " + 
                        ((NIC)currentRoute.peekFirst()).getIpAddress().toString();
                log[i] += "\nReceiver " + ((NIC)currentRoute.peekLast()).getIpAddress().toString() + "\n";
                currentRoute.removeFirst();
                currentRoute.removeLast();
            }
            log[i] += "Route:";
            while(!currentRoute.isEmpty())
            {
                NetworkDevice dev = currentRoute.pollFirst();
                if(dev instanceof Switch)
                    log[i] +=" switch with mac" + dev.getMacAddress().toString();
                else if(dev instanceof Hub)
                    log[i] +=" hub with mac" + dev.getMacAddress().toString();
            }
            log[i]+="\n";
        }
        
        return log;
    }
    
    public String[] getTestLog()
    {
        return testLog;
    }
    
    public boolean getTestResult()
    {
        return isTestPassed;
    }
    
    private String[] testLog;
    private final boolean isTestPassed;
    private final LinkedList<LinkedList<NetworkDevice>> testRoutes;
}
