package com.corejsf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter class to help XML to return nicely formatted dates.
 */
public class DateAdapter extends XmlAdapter<String, Date> {

    /** Format of date to output and input. */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * Used when parsing the XML, store date object.
     * @param xml input String expecting to be date.
     * @return Date converted from String input.
     * @exception Exception any error that could go wrong when parsing 
     * string.
     */
    public Date unmarshal(final String xml) throws Exception {
        return this.dateFormat.parse(xml);
    }

    /**
     * Used when producing XML, output date object.
     * @param object Date that should be convert to formated output
     * @return Date in String output format
     * @exception Exception any error that could occur when converting
     * Date to string.
     */
    public String marshal(final Date object) throws Exception {
        return this.dateFormat.format(object);
    }

}
