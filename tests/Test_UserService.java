package tests;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import entities.user.UserFolio;
import entities.assets.AssetInterface;
import application.UserService;
import entities.Month;



public class Test_UserService {

    private static UserFolio createTestUserFolio() {
        Map<String, Integer> fundWiseAmount = Map.of(
            "equity", 6000,
            "debt", 3000,
            "gold", 1000
        );
        UserFolio userFolio = UserService.allocateFund(fundWiseAmount);
        return userFolio;
    } 

    private static Map<String, Double> createTestChangeRates(Double equitychange, 
        Double debtChange, Double goldChange) {
        Map<String, Double> fundWiseAmount = Map.of(
            "equity", equitychange,
            "debt", debtChange,
            "gold", goldChange
        );
        return fundWiseAmount;
    }

    private static Map<String, Integer> createTestAssetWiseSIP(Integer equitySip, 
        Integer debtSip, Integer goldSip) {
        Map<String, Integer> fundWiseSip = Map.of(
            "equity", equitySip,
            "debt", debtSip,
            "gold", goldSip
        );
        return fundWiseSip;
    }


    private static ArrayList<Month> createTestMonths(Integer count) {
        ArrayList<Month> months = new ArrayList<Month>();
        Month[] allMonths = Month.values();
        for (int i=0; i < allMonths.length; i++) {
            if (allMonths[i].getMonthOrder() > count) {
                break;
            }
            months.add(allMonths[i]);
        }
        return months;
    }

    private static UserFolio updateFolioWithRateChange() {
        UserFolio userFolio = createTestUserFolio();
        Map<String, Integer> assetWiseSip = createTestAssetWiseSIP(2000, 1000, 500);
        UserService.setSip(userFolio, assetWiseSip);
        ArrayList<Month> monthsToJune = createTestMonths(6);

        List<List<Double>> testChangeRateValues = List.of(
                List.of(Double.valueOf(4), Double.valueOf(10), Double.valueOf(2)),
                List.of(Double.valueOf(-10), Double.valueOf(40), Double.valueOf(0)),
                List.of(Double.valueOf(12.50), Double.valueOf(12.50), Double.valueOf(12.50)),
                List.of(Double.valueOf(8), Double.valueOf(-3), Double.valueOf(7)),
                List.of(Double.valueOf(13), Double.valueOf(21), Double.valueOf(10.50)),
                List.of(Double.valueOf(10), Double.valueOf(8), Double.valueOf(-5))
        );

        List<Double> testChangeRateValueSet;
        Map<String, Double> testChangeRates;
        for (int i=0; i<monthsToJune.size(); i++) {
            testChangeRateValueSet = testChangeRateValues.get(i);

            testChangeRates = createTestChangeRates(testChangeRateValueSet.get(0), 
                                                    testChangeRateValueSet.get(1), 
                                                    testChangeRateValueSet.get(2));
            UserService.ChangeRate(userFolio, monthsToJune.get(i), testChangeRates);    
        }
        return userFolio;
    }

    @Test
    public void testfundAllocation() {
        UserFolio userFolio = createTestUserFolio();
        Integer totalAmount = userFolio.getTotalAmount();
        assertEquals(10000, totalAmount.intValue());
    }

    @Test
    public void testRateOfChange() {
        UserFolio userFolio =  updateFolioWithRateChange();
        Map<AssetInterface, Integer> assetWiseBalance = UserService.getFolioBalance(
            userFolio, "MARCH");
        for (var entry : assetWiseBalance.entrySet()) {
            if (entry.getKey().name() == "equity") {
                assertEquals(10593, entry.getValue().intValue());
            }
            if (entry.getKey().name() == "debt") {
                assertEquals(7897, entry.getValue().intValue());
            }
            if (entry.getKey().name() == "gold") {
                assertEquals(2272, entry.getValue().intValue());
            }
        }     
    }

    @Test
    public void testRebalance() {
        UserFolio userFolio = updateFolioWithRateChange();
        Map<AssetInterface, Integer> assetWiseBalance = UserService.rebalance(userFolio);
        for (var entry : assetWiseBalance.entrySet()) {
            if (entry.getKey().name() == "equity") {
                assertEquals(23619, entry.getValue().intValue());
            }
            if (entry.getKey().name() == "debt") {
                assertEquals(11809, entry.getValue().intValue());
            }
            if (entry.getKey().name() == "gold") {
                assertEquals(3936, entry.getValue().intValue());
            }
        }     
    }

}