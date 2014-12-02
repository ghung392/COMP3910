package com.corejsf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter class to help XML to return nicely formatted dates.
 */
public class DateAdapter extends XmlAdapter<String, Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * Used when parsing the XML, store date object.
     */
    public Date unmarshal(final String xml) throws Exception {
        return this.dateFormat.parse(xml);
    }

    /**
     * Used when producing XML, output date object.
     */
    public String marshal(final Date object) throws Exception {
        return this.dateFormat.format(object);
    }

}
