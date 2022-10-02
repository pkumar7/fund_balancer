package application;
import java.util.List;
import java.util.Arrays;
import entities.assets.AssetInterface;
import entities.assets.Debt;
import entities.assets.Equity;
import entities.assets.Gold;



public class AssetService {
    private List<AssetInterface> allAssets = Arrays.asList(new Equity(), new Debt(), new Gold());
    public List<AssetInterface> getAllAssets(){
        return allAssets;
    }
}
