package cn.teaey.apns4j.protocol;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Teaey
 * Date: 13-9-1
 *
 * @author xiaofei.wxf
 * @version $Id: $Id
 */
public class JsonParser
{
    private static final char DOUBLE_QUOTE = '"';
    private static final char COLON        = ':';
    private static final char BRACE_L      = '{';
    private static final char BRACE_R      = '}';
    private static final char BRACKETS_L   = '[';
    private static final char BRACKETS_R   = ']';
    private static final char COMMA        = ',';
    /**
     * <p>toJsonBytes.</p>
     *
     * @param root a {@link java.util.Map} object.
     * @return an array of byte.
     */
    public static byte[] toJsonBytes(Map<String, Object> root)
    {
        return toBytes(toJsonString(root));
    }
    /**
     * <p>toBytes.</p>
     *
     * @param jsonString a {@link String} object.
     * @return an array of byte.
     */
    public static byte[] toBytes(String jsonString)
    {
        return jsonString.getBytes(Protocal.DEF_CHARSET);
    }
    /**
     * <p>toJsonString.</p>
     *
     * @param root a {@link java.util.Map} object.
     * @return a {@link String} object.
     */
    public static String toJsonString(Map<String, Object> root)
    {
        StringBuilder sb = new StringBuilder(32);
        append(sb, root);
        int deleteIndex = -1;
        while ((deleteIndex = sb.indexOf(",}")) != -1)
        {
            sb.deleteCharAt(deleteIndex);
        }
        String ret = sb.toString();
        return ret;
    }
    static void append(StringBuilder sb, Map<String, Object> jsonMeta)
    {
        sb.append(BRACE_L);
        Iterator<String> iter = jsonMeta.keySet().iterator();
        while (iter.hasNext())
        {
            String eachKey = iter.next();
            Object eachValue = jsonMeta.get(eachKey);
            sb.append(DOUBLE_QUOTE).append(eachKey).append(DOUBLE_QUOTE).append(COLON);
            if (eachValue instanceof Map)
            {
                append(sb, (Map<String, Object>) eachValue);
            }
            else
                if (eachValue instanceof List)
                {
                    List list = (List) eachValue;
                    int size = list.size();
                    for (int i = 0; i < size; i++)
                    {
                        Object value = list.get(i);
                        if (i == 0)
                        {
                            sb.append(BRACKETS_L);
                        }
                        sb.append(value.toString());
                        if (i == (size - 1))
                        {
                            sb.append(BRACKETS_R);
                        }
                        else
                        {
                            sb.append(COMMA);
                        }
                    }
                }
                else
                    if (eachValue instanceof String)
                    {
                        sb.append(DOUBLE_QUOTE).append(eachValue).append(DOUBLE_QUOTE);
                    }
                    else
                    {
                        sb.append(eachValue);
                    }
            if (iter.hasNext())
                sb.append(COMMA);
        }
        sb.append(BRACE_R);
    }
}
