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
        String log[] = new String[testRoutes.size() + 1];
        
        int logStringsNumber = testRoutes.size();
        
        for(int i=0; i< logStringsNumber; i++)
        {
            LinkedList<NetworkDevice> currentRoute = testRoutes.pop();
        
            log[i] = "\nSender: " + 
                    ((NIC)currentRoute.peekFirst()).getDescription();
            log[i] += "\nReceiver: " + 
                    ((NIC)currentRoute.peekLast()).getDescription() + "\n";
            currentRoute.removeFirst();
            currentRoute.removeLast();
            
            log[i] += "Route:";
            if(currentRoute.isEmpty())
                log[i] += " direct";
            while(!currentRoute.isEmpty())
            {
                NetworkDevice dev = currentRoute.pollFirst();
                log[i] += " " + dev.getDescription();
            }
            log[i]+="\n";
        }
        
        if(isTestPassed)
            log[logStringsNumber] = "\nResult: test passed.";
        else
            log[logStringsNumber] = "\nResult: test failed.";
        
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
