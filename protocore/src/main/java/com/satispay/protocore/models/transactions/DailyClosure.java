package com.satispay.protocore.models.transactions;

public class DailyClosure {

/*
{
  "id": "20161003"
	"amount_unit": 90,
  "currency" : "EUR"
}
*/

    public DailyClosure() {
    }

    private String id;
    private Long amountUnit;
    private String currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(Long amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DailyClosure that = (DailyClosure) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (amountUnit != null ? !amountUnit.equals(that.amountUnit) : that.amountUnit != null)
            return false;
        return currency != null ? currency.equals(that.currency) : that.currency == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amountUnit != null ? amountUnit.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DailyClosure{" +
                "id='" + id + '\'' +
                ", amountUnit=" + amountUnit +
                ", currency='" + currency + '\'' +
                '}';
    }
}
