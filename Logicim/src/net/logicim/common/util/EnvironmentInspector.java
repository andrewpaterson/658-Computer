package net.logicim.common.util;

public class EnvironmentInspector
{
  public static String getUserHomeDir()
  {
    return getPropertyOrNull("user.home");
  }

  public static String getProgramDir()
  {
    return getPropertyOrNull("user.dir");
  }

  public static String getPropertyOrNull(String key)
  {
    return System.getProperty(key);
  }
}
