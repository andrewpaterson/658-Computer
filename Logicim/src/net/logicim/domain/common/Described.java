package net.logicim.domain.common;

public interface Described
{
  String getDescription();

  static String getDescription(Described described)
  {
    if (described != null)
    {
      return described.getDescription();
    }
    else
    {
      return "null";
    }
  }
}

