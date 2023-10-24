package net.logicim.assertions;

import net.common.parser.Tristate;

import static net.common.parser.Tristate.*;

public abstract class Validator
{
  private static final String NULL = "null";
  private static final String NOT_NULL = "not null";

  public static void validate(boolean expected, boolean actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Boolean.toString(expected), Boolean.toString(actual)));
    }
  }
  public static void validate(Class<?> expected, Class<?> actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(toClassString(expected), toClassString(actual)));
    }
  }

  private static String toClassString(Class<?> aClass)
  {
    if (aClass != null)
    {
      return aClass.getSimpleName();
    }
    return NULL;
  }

  public static void validateTrue(boolean actual)
  {
    validate(true, actual);
  }

  public static void validateFalse(boolean actual)
  {
    validate(false, actual);
  }

  public static void validate(Tristate expected, Tristate actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Tristate.toString(expected), Tristate.toString(actual)));
    }
  }

  public static void validateTrue(Tristate actual)
  {
    validate(TRUE, actual);
  }

  public static void validateFalse(Tristate actual)
  {
    validate(FALSE, actual);
  }

  public static void validateError(Tristate actual)
  {
    validate(ERROR, actual);
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

  public static void validate(double expected, double actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(Double.toString(expected), Double.toString(actual)));
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

  public static void validateIdentity(Object expected, Object actual)
  {
    if (expected != actual)
    {
      throw new ValidationException(toFailureString(toIdentityHashCode(expected), toIdentityHashCode(actual)));
    }
  }

  protected static String toIdentityHashCode(Object expected)
  {
    return "0x" + Integer.toHexString(System.identityHashCode(expected));
  }

  public static void validateEquals(Object expected, Object actual)
  {
    if ((expected == null) && (actual == null))
    {
      return;
    }

    if (expected == null || actual == null)
    {
      throw new ValidationException(toFailureString(toNullString(expected), toNullString(actual)));
    }

    if (!expected.equals(actual))
    {
      throw new ValidationException(toFailureString(expected.toString(), actual.toString()));
    }
  }

  private static String toNullString(Object o)
  {
    if (o == null)
    {
      return NULL;
    }
    else
    {
      return NOT_NULL;
    }
  }

  public static void validateNotNull(Object o)
  {
    if (o == null)
    {
      throw new ValidationException(toFailureString(NOT_NULL, NULL));
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
      throw new ValidationException(toFailureString(aClass.getName(), NULL));
    }
  }

  protected static String toFailureString(String expected, String actual)
  {
    return "\n" +
           "Expected: " + expected + "\n" +
           "Actual:   " + actual;
  }
}

