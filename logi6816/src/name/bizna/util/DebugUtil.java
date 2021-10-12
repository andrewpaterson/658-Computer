package name.bizna.util;

public class DebugUtil
{
  public static final boolean debug = false;

  public static void debugLog(String s)
  {
    if (debug)
    {
      System.out.println(s);
    }
  }
}

