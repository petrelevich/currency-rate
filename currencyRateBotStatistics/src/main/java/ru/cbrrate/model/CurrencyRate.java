package ru.cbrrate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyRate {
    String charCode;
    String nominal;
    String value;

    @JsonCreator
    public CurrencyRate(@JsonProperty("charCode") String charCode,
                        @JsonProperty("nominal") String nominal,
                        @JsonProperty("value") String value) {
        this.charCode = charCode;
        this.nominal = nominal;
        this.value = value;
    }
}
