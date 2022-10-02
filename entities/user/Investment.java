package entities.user;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import entities.Month;
import entities.assets.AssetInterface;
import entities.exceptions.InvalidMonthException;


public class Investment {
    public AssetInterface assetClass;
    private Integer allocatedAmount;
    private Integer currentBalance;
    private Integer sipAmount;
    public ArrayList<AssetChange> assetChanges;
    private Map<Month, Integer> monthWiseCurrentBalance;
    public Month runningMonth;

    private class AssetChange {
        private Month month;
        private double percentage;
    
        private AssetChange(Month month, double percentage) {
            this.month = month;
            this.percentage = percentage;
        }
    }
    
    public Investment(AssetInterface assetClass, Integer allocatedAmount) {
        this.assetClass = assetClass;
        this.setAllocatedAmount(allocatedAmount);
        this.currentBalance = this.allocatedAmount;
        this.assetChanges = new ArrayList<AssetChange>();
        this.sipAmount = 0;
        this.monthWiseCurrentBalance = new HashMap<Month, Integer>();
        this.runningMonth = null;
    }

    public Integer getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(Integer allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public void setSipAmount(Integer sipAmount) {
        this.sipAmount = sipAmount;
    }

    public void setAssetChange(Double percentage, Month month) throws InvalidMonthException {
        AssetChange assetChange = new AssetChange(month, percentage);
    
        if (this.runningMonth != null && 
                assetChange.month.getMonthOrder() - this.runningMonth.getMonthOrder() != 1) {
            throw new InvalidMonthException("Asset Change months should be in sequential order");
        }
        this.assetChanges.add(assetChange);
        this.runningMonth = assetChange.month;
        this.updateCurrentBalanceForAssetChange(assetChange);
    }

    private void updateCurrentBalanceForAssetChange(AssetChange assetChange){
        Double currentBalance = Double.valueOf(this.currentBalance);
        if (assetChange.month.getMonthOrder() > 1) {
            currentBalance = currentBalance + this.sipAmount;
        }
        currentBalance = currentBalance + ((assetChange.percentage / 100) * currentBalance);
        this.currentBalance = currentBalance.intValue();
        
        this.monthWiseCurrentBalance.put(assetChange.month, this.currentBalance);
    }

    public void setCurrentBalance(Integer amount) {
        this.currentBalance = amount;
        monthWiseCurrentBalance.put(this.runningMonth, amount);
    }

    public Integer getCurrentBalanceForMonth(Month month) throws InvalidMonthException {
        if (month == null) {
            month = this.runningMonth;
        }
        if (this.monthWiseCurrentBalance.get(month) == null) {
            throw new InvalidMonthException("Querying balance for a future month");
        }
        return this.monthWiseCurrentBalance.get(month);
    }

    public Integer getCurrentBalanceForMonthV2(Month month) {
        Double currentBalance = Double.valueOf(this.allocatedAmount);
        Integer monthOrder;
        for (int i = 0; i < this.assetChanges.size(); i++) {
            monthOrder = this.assetChanges.get(i).month.getMonthOrder();
            if (monthOrder >  month.getMonthOrder()) {
                break;
            }
            if (monthOrder > 1) {
                currentBalance = currentBalance + this.sipAmount;
            }
            currentBalance = currentBalance + ((
                this.assetChanges.get(i).percentage/100) * currentBalance);
        }
        return currentBalance.intValue();
    }

}
