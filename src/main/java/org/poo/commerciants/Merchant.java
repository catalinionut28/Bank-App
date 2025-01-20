package org.poo.commerciants;

import lombok.Getter;
import org.poo.fileio.CommerciantInput;

public class Merchant {
    @Getter
    private final String name;
    @Getter
    private final int id;
    @Getter
    private final String iban;
    @Getter
    private final String type;
    @Getter
    private final String cashbackStrategy;

    public Merchant(final CommerciantInput commerciantInput) {
        this.id = commerciantInput.getId();
        this.name = commerciantInput.getCommerciant();
        this.type = commerciantInput.getType();
        this.cashbackStrategy = commerciantInput.getCashbackStrategy();
        this.iban =  commerciantInput.getAccount();
    }



}
