import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.io.InputStreamReader;

import entities.Month;
import entities.user.UserFolio;

import application.UserService;

class RebalancerThread extends Thread {
    private Thread rebalancerThread;
    private String threadName;
 
    RebalancerThread(String name) {
       threadName = name;
    }
    public void run() {       
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        try  {
            System.out.println("Please enter the file path:- ");
            String filePath = consoleReader.readLine();
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            UserFolio userfolio = null;
            while (line != null) {
                if (line.startsWith("ALLOCATE")) {
                    String[] allocatedValues = line.split(" ");
                    Map<String, Integer> fundWiseAmount = Map.of(
                        "equity", Integer.parseInt(allocatedValues[1]),
                        "debt", Integer.parseInt(allocatedValues[2]),
                        "gold", Integer.parseInt((allocatedValues[3])));
                    userfolio = UserService.allocateFund(fundWiseAmount);
                }
                if (line.startsWith("SIP")) {
                    String[] sipAmounts = line.split(" ");
                    Map<String, Integer> fundWiseSip = Map.of(
                        "equity", Integer.parseInt(sipAmounts[1]),
                        "debt", Integer.parseInt(sipAmounts[2]),
                        "gold", Integer.parseInt(sipAmounts[3])
                    );
                    UserService.setSip(userfolio, fundWiseSip);
                }
                if (line.startsWith("CHANGE")) {
                    String[] changes = line.split(" ");
                    String equityChange = changes[1].replaceFirst("%", "");
                    String debtChange = changes[2].replaceFirst("%", "");
                    String goldChange = changes[3].replaceFirst("%", "");
                    Map<String, Double> fundWiseChange = Map.of(
                        "equity", Double.valueOf(equityChange),
                        "debt", Double.valueOf(debtChange),
                        "gold", Double.valueOf(goldChange)
                    );
                    UserService.ChangeRate(userfolio, Month.valueOf(changes[4]), fundWiseChange);
                }
                if (line.startsWith("BALANCE")) {
                    String[] balanceArgs = line.split(" ");
                    UserService.getFolioBalance(userfolio, balanceArgs[1]);
                }
                if (line.startsWith("REBALANCE")) {
                    UserService.rebalance(userfolio);
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            }  
        catch(IOException e){  
            e.printStackTrace();
        }  
    }
    public void start () {       
       if (rebalancerThread == null) {
        rebalancerThread = new Thread (this, threadName);
        rebalancerThread.start();
       }
    }
 }

public class Rebalancings {
    public static void main(String args[]) {
        RebalancerThread T1 = new RebalancerThread("Thread1");
        T1.start();
        RebalancerThread T2 = new RebalancerThread("Thread2");
        T2.start();
    }
}
