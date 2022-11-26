package net.logicim.data;

public abstract class SaveData
{
  public SaveData()
  {
  }

  public static String getXMLTag(Class<?> aClass)
  {
    String simpleName = aClass.getSimpleName();
    if (simpleName.endsWith("Data"))
    {
      simpleName = simpleName.substring(0, simpleName.length() - 4);
    }
    return simpleName;
  }

  public abstract Object getObject();
}

