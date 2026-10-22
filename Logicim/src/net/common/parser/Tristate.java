package net.common.parser;

public enum Tristate
{
  TRUE,
  FALSE,
  ERROR;

  public static String toString(Tristate tristate)
  {
    switch (tristate)
    {
      case TRUE:
        return "TRUE";
      case FALSE:
        return "FALSE";
      case ERROR:
        return "ERROR";
      default:
        return "<Unknown>";
    }
  }
}

