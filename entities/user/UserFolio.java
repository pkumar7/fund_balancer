package entities.user;
import java.util.Map;
import java.util.HashMap;
import entities.assets.AssetInterface;
import java.util.ArrayList;
import entities.Month;
import entities.exceptions.InvalidMonthException;
import entities.exceptions.InvalidRebalanceMonthCount;

public class UserFolio {

    private ArrayList <FolioAllotment> folioAllotments;
    private Integer totalAmount;
    
    public Map<AssetInterface, FolioAllotment> assetToFolioAllotment;

    public UserFolio(ArrayList<FolioAllotment> folioAllotments, Integer totalAmount) {
        this.setFolioAllotments(folioAllotments);
        this.setTotalAmount(totalAmount);
        this.assetToFolioAllotment = new HashMap<AssetInterface, FolioAllotment>();
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList <FolioAllotment> getFolioAllotments() {
        return folioAllotments;
    }

    private void setFolioAllotments(ArrayList<FolioAllotment> folioAllotments) {
        this.folioAllotments = folioAllotments;
    }

    public Map<AssetInterface, Integer> getBalance(Month month) throws InvalidMonthException{
        Map<AssetInterface, Integer> assetWiseBalance = new HashMap<AssetInterface, Integer>();
        Integer balance;
        FolioAllotment folioAllotment;

        for (int i = 0; i < this.folioAllotments.size(); i++) {
            folioAllotment = this.folioAllotments.get(i);
            try {
                balance = folioAllotment.investment.getCurrentBalanceForMonth(month);
            } catch (InvalidMonthException e) {
                throw e;
            }
            assetWiseBalance.put(folioAllotment.investment.assetClass, balance);
        }
        return assetWiseBalance;
    }

    private void prepareForRebalance() throws InvalidRebalanceMonthCount{
        FolioAllotment folioAllotment;
        for (int i = 0; i < this.folioAllotments.size(); i++) {
            folioAllotment = this.folioAllotments.get(i); 
            if (folioAllotment.investment.runningMonth.getMonthOrder() != 6 &&
                    folioAllotment.investment.runningMonth.getMonthOrder() != 12){
                throw new InvalidRebalanceMonthCount("Invalid number of months for rebalancing");
            }
        }
    }

    public void rebalance() throws InvalidMonthException, InvalidRebalanceMonthCount {
        try {
            this.prepareForRebalance();
        } catch (InvalidRebalanceMonthCount e1) {
            throw e1;
        }
        try{
            FolioAllotment folioAllotment;
            Integer rebalancedAmount;
            Map<AssetInterface, Integer> assetWiseBalance = this.getBalance(null);
            Integer totalBalance = assetWiseBalance.values().stream().mapToInt(
                Integer::intValue).sum();
            for (int i = 0; i < this.folioAllotments.size(); i++) {
                folioAllotment = this.folioAllotments.get(i);
                rebalancedAmount = (int) ((folioAllotment.percentage / 100) * totalBalance);
                folioAllotment.investment.setCurrentBalance(rebalancedAmount);
            }
        } catch (InvalidMonthException e) {
            throw e;
        }
    }
}
