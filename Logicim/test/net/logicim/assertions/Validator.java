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

  public static void validateTrue(boolean actual)
  {
    validate(true, actual);
  }

  public static void validateFalse(boolean actual)
  {
    validate(false, actual);
  }

  public static void validate(int expected, int actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Integer.toString(expected), Integer.toString(actual)));
    }
  }

  public static void validate(float expected, float actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Float.toString(expected), Float.toString(actual)));
    }
  }

  public static void validate(long expected, long actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Long.toString(expected), Long.toString(actual)));
    }
  }

  public static void validate(String expected, String actual)
  {
    if (!expected.equals(actual))
    {
      throw new ValidationException(toFailureString(expected, actual));
    }
  }

  public static void validate(Object expected, Object actual)
  {
    if (expected != actual)
    {
      int expectedHash = System.identityHashCode(expected);
      int actualHash = System.identityHashCode(actual);
      throw new ValidationException(toFailureString("0x" + Integer.toHexString(expectedHash), "0x" + Integer.toHexString(actualHash)));
    }
  }

  public static void validateNotNull(Object o)
  {
    if (o == null)
    {
      throw new ValidationException(toFailureString("Not null", "null"));
    }
  }

  public static void validateClass(Class<?> aClass, Object o)
  {
    if (o != null)
    {
      Class<?> oClass = o.getClass();
      if (oClass != aClass)
      {
        throw new ValidationException(toFailureString(aClass.getSimpleName(), oClass.getSimpleName()));
      }
    }
    else
    {
      throw new ValidationException(toFailureString(aClass.getName(), "null"));
    }
  }

  protected static String toFailureString(String expected, String actual)
  {
    return "\n" +
           "Expected: " + expected + "\n" +
           "Actual:   " + actual;
  }
}

