package net.logicim.file;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LogicimFileReader
{
  public static final String xml = "<baeldung>\n" +
                                   "    <articles>\n" +
                                   "        <article>\n" +
                                   "            <title>Parsing an XML File Using SAX Parser</title>\n" +
                                   "            <content>SAX Parser's Lorem ipsum...</content>\n" +
                                   "        </article>\n" +
                                   "        <article>\n" +
                                   "            <title>Parsing an XML File Using DOM Parser</title>\n" +
                                   "            <content>DOM Parser's Lorem ipsum...</content>\n" +
                                   "        </article>\n" +
                                   "        <article>\n" +
                                   "            <title>Parsing an XML File Using StAX Parser</title>\n" +
                                   "            <content>StAX's Lorem ipsum...</content>\n" +
                                   "        </article>\n" +
                                   "    </articles>\n" +
                                   "</baeldung>\n";

  public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
  {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    SAXParser parser = saxParserFactory.newSAXParser();
    LogicimXMLReader logicimXmlReader = new LogicimXMLReader();
    InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    parser.parse(inputStream, logicimXmlReader);
  }
}

