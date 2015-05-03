package networkmodeling.core;

import java.util.LinkedList;

public class NetworkModelTestResult {
    
    public NetworkModelTestResult(boolean isTestPassed,
            LinkedList<LinkedList<NetworkDevice>> testRoutes,
            LinkedList<NetworkDevice> failedRoute)
    {
        this.isTestPassed = isTestPassed;
        this.testRoutes = testRoutes;
        this.failedRoute = failedRoute;
        this.testLog = generateTestLog();
    }
    
    private String[] generateTestLog()
    {
        String log[] = new String[testRoutes.size() + 1];
        
        int logStringsNumber = testRoutes.size();
        
        for(int i=0; i< logStringsNumber; i++)
        {
            LinkedList<NetworkDevice> currentRoute = testRoutes.pop();
            
            if(!currentRoute.isEmpty())
            {
                if(currentRoute.peekFirst() != null &&
                        currentRoute.peekLast()!= null)
                {
                    log[i] = "\nSender: " + 
                            currentRoute.peekFirst().getDescription();
                    log[i] += "\nReceiver: " + 
                            currentRoute.peekLast().getDescription() + "\n";
                    currentRoute.removeFirst();
                    currentRoute.removeLast();
                }
                log[i] += "Route:";
                if(currentRoute.isEmpty())
                    log[i] += " direct";
                while(!currentRoute.isEmpty())
                {
                    NetworkDevice dev = currentRoute.pollFirst();
                    log[i] += "\n - " + dev.getDescription();
                }
                log[i] += "\n";
            }
            else
                log[i] = "";
        }
        
        if(isTestPassed)
            log[logStringsNumber] = "\nResult: test passed.";
        else
        {
            log[logStringsNumber] = "\nResult: test failed.\n";
            log[logStringsNumber]+= "\nUnable to transfer data \nfrom " +
                    failedRoute.peekFirst().getDescription() + "\nto " +
                    failedRoute.peekLast().getDescription();
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
    
    private LinkedList<NetworkDevice> failedRoute;
    private String[] testLog;
    private final boolean isTestPassed;
    private final LinkedList<LinkedList<NetworkDevice>> testRoutes;
}
