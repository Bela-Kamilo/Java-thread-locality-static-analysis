package util;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class EmptyFormatter extends Formatter {


        @Override
        public String format(LogRecord record) {
            //StringBuffer sb = new StringBuffer();
            //sb.append("Prefixn");
            return record.getMessage()+"\n";
            //sb.append("Suffixn");
            //sb.append("n");
            //return sb.toString();
        }


}
