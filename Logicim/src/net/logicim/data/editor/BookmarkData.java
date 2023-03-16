package net.logicim.data.editor;

import net.logicim.data.ReflectiveData;

public class BookmarkData
    extends ReflectiveData
{
  public int bookmarkKey;
  public String subcircuitTypeName;

  public BookmarkData()
  {
  }

  public BookmarkData(int bookmarkKey, String subcircuitTypeName)
  {
    this.bookmarkKey = bookmarkKey;
    this.subcircuitTypeName = subcircuitTypeName;
  }
}

