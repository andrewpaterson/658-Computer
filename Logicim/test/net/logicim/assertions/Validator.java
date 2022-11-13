package net.logicim.assertions;

public abstract class Validator
{
  public static void validate(boolean expected, boolean actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Boolean.toString(expected), Boolean.toString(actual)));
    }
  }

  public static void validate(int expected, int actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Integer.toString(expected), Integer.toString(actual)));
    }
  }

  protected static String toFailureString(String expected, String actual)
  {
    return "\n" +
           "Expected: " + expected + "\n" +
           "Actual:   " + actual;
  }
}

