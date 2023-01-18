package net.logicim.common.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodKey
{
  protected String methodName;
  protected Class<?>[] parameterTypes;

  public MethodKey(String methodName, Class<?>[] parameterTypes)
  {
    this.methodName = methodName;
    this.parameterTypes = parameterTypes;
  }

  public MethodKey(Method method)
  {
    this.methodName = method.getName();
    this.parameterTypes = method.getParameterTypes();
  }

  public boolean equals(Object rhsObject)
  {
    if ((rhsObject != null) && (rhsObject.getClass() == getClass()))
    {
      MethodKey rhs = (MethodKey) rhsObject;

      return methodName.equals(rhs.methodName) && Arrays.equals(parameterTypes, rhs.parameterTypes);
    }
    else
    {
      return false;
    }
  }

  public int hashCode()
  {
    int result = methodName.hashCode();
    for (Class<?> type : parameterTypes)
    {
      result = result * type.hashCode();
    }
    return result;
  }
}
