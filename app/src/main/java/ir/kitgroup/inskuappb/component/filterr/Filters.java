package ir.kitgroup.inskuappb.component.filterr;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ir.kitgroup.inskuappb.component.AccountFilter;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.dataBase.City;
import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.State;

public class Filters {
    boolean filter;

    public boolean getResultFilter() {
        List<BusinessR> resBusinessR = Select.from(BusinessR.class).list();
        List<Guild> resGuild = Select.from(Guild.class).list();
        List<State> resState = Select.from(State.class).list();
        List<City> resCity = Select.from(City.class).list();


        if (resBusinessR.size() > 0 || resGuild.size() > 0 || resState.size() > 0 || resCity.size() > 0)
            filter = true;
        else
            filter = false;
        return filter;
    }
    public AccountFilter getAccountFilter() {
        AccountFilter accountFilter = new AccountFilter();
        List<City> chooseCities = Select.from(City.class).list();
        List<State> chooseStates = Select.from(State.class).list();
        List<Guild> choseGuilds = Select.from(Guild.class).list();
        List<BusinessR> businessRS = Select.from(BusinessR.class).list();


        ArrayList<String> cityIdList = new ArrayList<>();
        if (chooseCities.size() > 0)
            cityIdList.add(chooseCities.get(0).getI());


        ArrayList<String> stateIdList = new ArrayList<>();
        if (chooseStates.size() > 0)
            stateIdList.add(chooseStates.get(0).getI());

        ArrayList<String> guildIdList = new ArrayList<>();
        if (choseGuilds.size() > 0)
            guildIdList.add(choseGuilds.get(0).getI());

        ArrayList<String> businessRIdList = new ArrayList<>();
        for (int i=0;i<businessRS.size();i++){
                businessRIdList.add(businessRS.get(i).getI());
            }



        accountFilter.City = cityIdList;
        accountFilter.State = stateIdList;
        accountFilter.Guild = guildIdList;
        accountFilter.BuisnessRelation = businessRIdList;

        return accountFilter;
    }

}
