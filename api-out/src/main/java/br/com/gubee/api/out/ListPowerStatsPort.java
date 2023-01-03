package br.com.gubee.api.out;

import br.com.gubee.api.out.model.PowerStatsModelApiOut;

import java.util.List;

public interface ListPowerStatsPort {

    List<PowerStatsModelApiOut> findAll();
}
