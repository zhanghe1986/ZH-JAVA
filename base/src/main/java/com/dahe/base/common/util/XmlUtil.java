package com.dahe.base.common.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {
	
	private final static String XML_DOC_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	private final static String XML_TABLE_START = "<table>";
	private final static String XML_TABLE_END = "</table>";
	private final static String XML_HEAD_START = "<head>";
	private final static String XML_HEAD_END = "</head>";
	private final static String XML_RESULT_START = "<result";
	private final static String XML_ROWS_START = "<rows>";
	private final static String XML_ROWS_END = "</rows>";
	private final static String XML_ROW_START = "<row ";
	private final static String XML_END = " />";

	
	public static List<Map<String, Object>> xml2ListMap(String xml) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			Document doc = string2Xml(xml);
			List<Element> rows = getElementsByXPath(doc, "/table/rows/row");
			for (Element row : rows) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (Object o : row.attributes()) {
					Attribute attribute = (Attribute) o;
					map.put(attribute.getName(), attribute.getValue());
				}
				result.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @describe String result = XmlUtil.toXml("", rc, null, list, null);
	 * */
	/*public static String toXml(String xmlType, ResultCode resultCode, Long resultId, List list,
			Map<String, Object> args) {
		int size = 1;
		if (list != null) {
			size = list.size() + 1;
		}
		StringBuilder sb = new StringBuilder(size*100);
		sb.append(XML_DOC_HEADER);
		sb.append(XML_TABLE_START);
		sb.append("\n");
		
		//ResultCode analyze
		if (resultCode != null) {
			sb.append(XML_HEAD_START).append("\n");
			sb.append(XML_RESULT_START);
			if (xmlType != null && xmlType.length() > 0) {
				sb.append(" type=\""+xmlType+"\"");
			}
			if (args != null) {
				for (Entry<String, Object> entry : args.entrySet()) {
					sb.append(" "+entry.getKey()+"=\""+entry.getValue()+"\"");
				}
			}
			if (list != null) {
				sb.append(" result_code=\""+resultCode.getCode()
						+"\" message=\""+string2Xml(resultCode.getMessage())
						+"\" size=\""+list.size()+"\"");
			} else {
				if (resultId != null) {
					sb.append(" result_code=\""+resultCode.getCode()
							+"\" message=\""+string2Xml(resultCode.getMessage())
							+"\""+" id=\""+resultId+"\"");
				} else {
					sb.append(" result_code=\""+resultCode.getCode()
							+"\" message=\""+string2Xml(resultCode.getMessage())
							+"\"");
				}
			}
			sb.append(XML_END).append("\n");
			sb.append(XML_HEAD_END).append("\n");
		}
		
		sb.append(XML_ROWS_START).append("\n");
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				StringBuilder rowSb = new StringBuilder("");
				if (obj instanceof Map) {
					rowSb.append(map2Row(xmlType, (Map)obj));
				} else {
					rowSb.append(obj2Row(obj));
				}
				sb.append(rowSb);
			}
		}
		sb.append(XML_ROWS_END).append("\n");
		sb.append(XML_TABLE_END);
		return sb.toString();
	}*/
	
	private static Document string2Xml(String xml) {
		if (xml != null && xml.length() > 0) {
			return null;
		}
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	private static List<Element> getElementsByXPath(Document doc, String xPath) {
		List properties = null;
		try {
			properties = doc.selectNodes(xPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/*private static StringBuilder map2Row(String xmlType, Map map) {
		Set<String> keySet = map.keySet();
		StringBuilder rowSb = new StringBuilder();
		rowSb.append(XML_ROW_START);
		for (String key : keySet) {
			Object obj = map.get(key);
			rowSb.append(key.toLowerCase())
				.append("=\"")
				.append(obj != null ? string2Xml(object2String(obj)) : "null")
				.append("\" ");
		}
		if (xmlType != null && xmlType.length() > 0) {
			rowSb.append("tag").append("=\"").append(xmlType).append("\" ");
		}
		rowSb.append(XML_END);
		rowSb.append("\n");
		return rowSb;
	}*/
	
	/*private static String obj2Row(Object obj) {
		StringBuilder sb = new StringBuilder();
		sb.append(XML_ROW_START);
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(obj.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				String name = props[i].getName();
				String value = "null";
				try {
					Method m = props[i].getReadMethod();
					if (m == null) {
						continue;
					}
					Object object = m.invoke(obj);
					if (object != null 
							&& (object instanceof String || object instanceof Integer
									|| object instanceof Short
									|| object instanceof Long
									|| object instanceof Boolean
									|| object instanceof Double
									|| object instanceof Byte
									|| object instanceof Character
									|| object instanceof Float || object instanceof Date)) {
						value = toXml(object2String(object));
					} else {
						continue;
					}
					if (name != null && name.length() > 0) {
						sb.append(name);
						sb.append("=\"");
						sb.append(value);
						sb.append("\" ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		sb.append(XML_END);
		sb.append("\n");
		return sb.toString();
	}*/
	
	/*private static String object2String(Object obj) {
		if (obj instanceof Date) {
			return DateFormatUtils.format((Date)obj, DateFormatUtils.FORMAT_COMMON_DATE_TIME);
		} else {
			if (obj instanceof String || obj instanceof Integer
					|| obj instanceof Short
					|| obj instanceof Long
					|| obj instanceof Boolean
					|| obj instanceof Double
					|| obj instanceof Byte
					|| obj instanceof Character
					|| obj instanceof Float || obj instanceof BigDecimal) {
				return obj.toString();
			} else {
				return "";
			}
		}
	}*/
	
	private static String toXml(String content) {
		char[] chars = content.toCharArray();
		StringBuilder sb = new StringBuilder(content.length()*4);
		
		for (char c : chars) {
			switch (c) {
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '&':
					sb.append("&amp;");
					break;
				case '\'':
					sb.append("&apos;");
					break;
				default:
					sb.append(c);
			}
		}
		
		if (sb.length() == content.length()) {
			return content;
		} else {
			return sb.toString();
		}
	}
	
    public static String toXml(Object object) {
        String result = toXml(object, "GBK");
        result = result.replace("encoding=\"GBK\"", "encoding=\"UTF-8\"");
        return result;
    }

    public static String toXml(Object object, String code) {
        String result = "";
        OutputStream out = new ByteArrayOutputStream();
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, code);
            marshaller.marshal(object, out);
            result = out.toString().replace(" standalone=\"yes\"", "");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new UnsupportedOperationException(e);
            }
        }
        return result;
    }

    
    public static <T> T unMarshaller(Class<T> cls, String protocol) {
        return unMarshaller(cls, protocol, "UTF-8");
    }

    
    @SuppressWarnings("unchecked")
    public static <T> T unMarshaller(Class<T> cls, String protocol, String code) {
        T t = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(protocol.getBytes(code));
            JAXBContext context = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(is);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new UnsupportedOperationException(e);
                }
            }
        }
        return t;
    }
/**
    public static String format(String unformattedXml) {
        try {
            final Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    **/

    @SuppressWarnings("unused")
	private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return (Document) db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /*public static void main(String args[]) {
    	Socket socket = new Socket();
    	Client client = new Client();
    	
    	client.setListenedPort(80);
    	client.setServerIp("127.0.0.1");
    	client.setSocket(socket);
    	System.out.println(toXml(client));
    }*/

}
