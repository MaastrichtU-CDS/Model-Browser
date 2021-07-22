package nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeHelper {
    public static DateTimeFormatter xsdDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
}
