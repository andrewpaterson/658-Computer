package net.logicim.data.editor;

import net.logicim.data.ReflectiveData;

public class BookmarkData
    extends ReflectiveData
{
  public int bookmarkIndex;
  public String subcircuitTypeName;

  public BookmarkData()
  {
  }

  public BookmarkData(int bookmarkIndex, String subcircuitTypeName)
  {
    this.bookmarkIndex = bookmarkIndex;
    this.subcircuitTypeName = subcircuitTypeName;
  }
}

