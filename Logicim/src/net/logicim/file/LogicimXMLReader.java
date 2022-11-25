package net.logicim.file;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class LogicimXMLReader
    extends DefaultHandler
{

  private Baeldung website;
  private StringBuilder elementValue;

  @Override
  public void characters(char[] ch, int start, int length)
  {
    if (elementValue == null)
    {
      elementValue = new StringBuilder();
    }
    else
    {
      elementValue.append(ch, start, length);
    }
  }

  @Override
  public void startDocument()
  {
    website = new Baeldung();
  }

  @Override
  public void startElement(String uri, String lName, String qName, Attributes attr)
  {
    switch (qName)
    {
      case "articles":
        website.articleList = new ArrayList<>();
        break;
      case "article":
        website.articleList.add(new BaeldungArticle());
        break;
      case "title":
        elementValue = new StringBuilder();
        break;
      case "content":
        elementValue = new StringBuilder();
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    switch (qName)
    {
      case "title":
        latestArticle().title = elementValue.toString();
        break;
      case "content":
        latestArticle().content = elementValue.toString();
        break;
    }
  }

  private BaeldungArticle latestArticle()
  {
    List<BaeldungArticle> articleList = website.articleList;
    int latestArticleIndex = articleList.size() - 1;
    return articleList.get(latestArticleIndex);
  }

  public Baeldung getWebsite()
  {
    return website;
  }
}

