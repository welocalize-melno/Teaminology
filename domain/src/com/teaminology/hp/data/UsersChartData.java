package com.teaminology.hp.data;

/**
 * POJO class containing the instance variables of Users chart per month.
 *
 * @author sarvanic
 */
public class UsersChartData
{

    private String month;
    private Integer count;

    /**
     * To get month
     *
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     * To set month
     *
     * @param month month name
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * To get no.of terms count
     *
     * @return no.of terms count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * To set no.of terms count
     *
     * @param count no.of terms count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

}
