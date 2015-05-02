
package networkmodeling.core;

import java.util.HashMap;
import junit.framework.TestCase;

public class NetworkModelTest extends TestCase {
    
    public NetworkModelTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testSendData() {
        System.out.println("SendData");
        IpAddress sourceIP = new IpAddress("1.0.0.0");
        
        IpAddress destIP = new IpAddress("1.1.0.0");
        
        NIC source = new NIC(MacAddress.getRandomAddress(), sourceIP, sourceIP);
        NIC dest = new NIC(MacAddress.getRandomAddress(), destIP, destIP);

        Switch switch1 = new Switch(MacAddress.getRandomAddress(), 2);
        
        NetworkModel instance = new NetworkModel();
        
        instance.AddDevice(source);
        instance.AddDevice(dest);
        instance.AddDevice(switch1);
        
        instance.ConnectDevices(switch1, dest);
        instance.ConnectDevices(switch1, source);
        
        
        String data = "test";
        boolean result = instance.SendData(sourceIP, data, destIP);
        
        while(!dest.getLastIncomingDataRoute().isEmpty())
        {
            NetworkDevice nextDev = dest.getLastIncomingDataRoute().pop();
            System.out.println(nextDev.getMacAddress().toString());
        }
        
        boolean expResult = true;
        
        assertEquals(expResult, result);

        if(! instance.TestNetwork())
            fail("testNetwork Failed!");
    }
/*
    public void testAddDevice() {
        System.out.println("AddDevice");
        NetworkDevice dev = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.AddDevice(dev);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testConnectDevices() {
        System.out.println("ConnectDevices");
        NetworkDevice dev1 = null;
        NetworkDevice dev2 = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.ConnectDevices(dev1, dev2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testDisconnectDevices() {
        System.out.println("DisconnectDevices");
        NetworkDevice dev1 = null;
        NetworkDevice dev2 = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.DisconnectDevices(dev1, dev2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testTestNetwork() {
        System.out.println("TestNetwork");
        NetworkModel instance = new NetworkModel();
        
        boolean expResult = false;
        boolean result = instance.TestNetwork();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testSendData() {
        System.out.println("SendData");
        IpAddress sourceIP = null;
        Object data = null;
        IpAddress target = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.SendData(sourceIP, data, target);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testDeleteDevice() {
        System.out.println("DeleteDevice");
        NetworkDevice dev = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.DeleteDevice(dev);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testAreConnected() {
        System.out.println("AreConnected");
        NetworkDevice dev1 = null;
        NetworkDevice dev2 = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.AreConnected(dev1, dev2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testHasDevice() {
        System.out.println("HasDevice");
        NetworkDevice dev = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.HasDevice(dev);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testGetDevicesMap() {
        System.out.println("getDevicesMap");
        NetworkModel instance = new NetworkModel();
        HashMap<MacAddress, NetworkDevice> expResult = null;
        HashMap<MacAddress, NetworkDevice> result = instance.getDevicesMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testChangeDeviceIP() {
        System.out.println("ChangeDeviceIP");
        IpBasedNetworkDevice dev = null;
        IpAddress newIP = null;
        NetworkModel instance = new NetworkModel();
        boolean expResult = false;
        boolean result = instance.ChangeDeviceIP(dev, newIP);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    public void testFindByMac() {
        System.out.println("FindByMac");
        MacAddress address = null;
        NetworkModel instance = new NetworkModel();
        NetworkDevice expResult = null;
        NetworkDevice result = instance.FindByMac(address);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */
    
}
