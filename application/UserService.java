package application;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Month;
import entities.assets.AssetInterface;
import entities.exceptions.InvalidMonthException;
import entities.exceptions.InvalidRebalanceMonthCount;
import entities.user.Investment;
import entities.user.FolioAllotment;
import entities.user.UserFolio;


public class UserService {
    
    public static UserFolio allocateFund(Map<String, Integer> fundWiseAmount) {
        AssetService assetService = new AssetService();
        List<AssetInterface> allAssets = assetService.getAllAssets();
        AssetInterface asset;
        Integer amount;
        Double percentage;
        ArrayList <FolioAllotment> folioAllotments = new ArrayList<FolioAllotment>();

        Integer totalAmount = 0;
        for (var entry : fundWiseAmount.entrySet()) {
            totalAmount += entry.getValue();
        }

        for (int i = 0; i < allAssets.size(); i++) { 
            asset = allAssets.get(i);
            amount = fundWiseAmount.get(asset.name());
            Investment investment = new Investment(asset, amount);
            percentage = Double.valueOf(amount) / Double.valueOf(totalAmount) * 100;
            FolioAllotment folioAllotment = new FolioAllotment(investment, percentage);
            folioAllotments.add(folioAllotment);
        }
        UserFolio userFolio = new UserFolio(folioAllotments, totalAmount);
        return userFolio;
    }

    public static void setSip(UserFolio userFolio, Map<String, Integer> fundWiseSip) {
        ArrayList<FolioAllotment> folioAllotments = userFolio.getFolioAllotments();
        FolioAllotment folioAllotment;
        Investment investment;
        for (int i = 0; i < folioAllotments.size(); i++) {
            folioAllotment = folioAllotments.get(i);
            investment = folioAllotment.investment;
                investment.setSipAmount(fundWiseSip.get(investment.assetClass.name()));
        }

    }

    public static void ChangeRate(UserFolio userFolio, Month month, 
                    Map<String, Double> fundWiseChange) {
        ArrayList<FolioAllotment> folioAllotments = userFolio.getFolioAllotments();
        FolioAllotment folioAllotment;
        Investment investment;
        for (int i = 0; i < folioAllotments.size(); i++) {
            folioAllotment = folioAllotments.get(i);
            investment = folioAllotment.investment;
            try {
                investment.setAssetChange(fundWiseChange.get(investment.assetClass.name()), month);
            } catch (InvalidMonthException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Map<AssetInterface, Integer> getFolioBalance(UserFolio userFolio, String month) {
        Month monthEnum = null;
        if (month != null) {
            monthEnum = Month.valueOf(month);
        }
        Map<AssetInterface, Integer> assetWiseBalance = new HashMap<AssetInterface, Integer>();
        try {
            assetWiseBalance = userFolio.getBalance(monthEnum);
        } catch (InvalidMonthException e) {
            System.out.println("Wrong Month as input");
            return null;
        }
        displayMsg(assetWiseBalance);
        return assetWiseBalance;
    }

    private static void displayMsg(Map<AssetInterface, Integer> assetWiseBalance){
        Integer assetBalances[] = new Integer[3];
        for (var entry : assetWiseBalance.entrySet()) {
            if (entry.getKey().name() == "equity"){
                assetBalances[0] = entry.getValue();
            }
            if (entry.getKey().name() == "debt"){
                assetBalances[1] = entry.getValue();
            }
            if (entry.getKey().name() == "gold"){
                assetBalances[2] = entry.getValue();
            }
        }
        for (int i=0; i<assetBalances.length; i++) {
            System.out.print(assetBalances[i]);
            System.out.print(" ");
        }
        System.out.println();


    }
    public static Map<AssetInterface, Integer> rebalance(UserFolio userFolio) {
        try {
            userFolio.rebalance();
        } catch (InvalidMonthException e) {
            System.out.println("Wrong months configured for the folio");
            return null;
            
        } catch (InvalidRebalanceMonthCount e) {
            System.out.println("CANNOT_REBALANCE");
            return null;
        }
        Map<AssetInterface, Integer> assetWiseBalance = new HashMap<AssetInterface, Integer>();
        assetWiseBalance = getFolioBalance(userFolio, null);
        return assetWiseBalance;
    }

}
