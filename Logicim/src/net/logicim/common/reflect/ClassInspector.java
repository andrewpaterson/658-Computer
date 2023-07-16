package net.logicim.common.reflect;

import net.logicim.common.SimulatorException;
import sun.reflect.ReflectionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClassInspector
{
  private static ConcurrentMap<Class<?>, ClassInspector> classInspectorCache = new ConcurrentHashMap<>();
  private static ConcurrentMap<String, Class<?>> classNameCache = new ConcurrentHashMap<>();

  private Class<?> aClass;
  private Map<String, Field> localFields;
  private Map<String, Field> superFields;
  private Map<String, Field> allFields;
  private Map<MethodKey, Method> _allMethodsMap;
  private Constructor<?> specialNoArgConstructor;

  private ClassInspector(Class<?> aClass)
  {
    this.aClass = aClass;
  }

  public static ClassInspector forClass(Class<?> aClass)
  {
    if (aClass == null)
    {
      throw new SimulatorException("Cannot get ClassInspector for [null] Class.");
    }
    ClassInspector classInspector = classInspectorCache.get(aClass);
    if (classInspector == null)
    {
      classInspector = new ClassInspector(aClass);
      ClassInspector current = classInspectorCache.putIfAbsent(aClass, classInspector);
      if (current != null)
      {
        classInspector = current;
      }
    }
    return classInspector;
  }

  public static ClassInspector forName(String className)
  {
    return forClass(getClass(className));
  }

  private static Class<?> getClass(String className)
  {
    Class<?> result = classNameCache.get(className);
    if (result == null)
    {
      result = findClass(className);
      Class<?> current = classNameCache.putIfAbsent(className, result);
      if (current != null)
      {
        result = current;
      }
    }
    return result;
  }

  private static Class<?> findClass(String className)
  {
    Class<?> aClass = classOrNullForPrimitives(className);

    if (aClass == null)
    {
      try
      {
        aClass = Class.forName(className);
      }
      catch (ClassNotFoundException e)
      {
        aClass = getClassFromContextClassLoader(className);
      }
    }
    return aClass;
  }

  private static Class<?> classOrNullForPrimitives(String className)
  {
    if (className.equals("int"))
    {
      return int.class;
    }
    if (className.equals("boolean"))
    {
      return boolean.class;
    }
    if (className.equals("char"))
    {
      return char.class;
    }
    if (className.equals("byte"))
    {
      return byte.class;
    }
    if (className.equals("short"))
    {
      return short.class;
    }
    if (className.equals("int"))
    {
      return int.class;
    }
    if (className.equals("float"))
    {
      return float.class;
    }
    if (className.equals("long"))
    {
      return long.class;
    }
    if (className.equals("double"))
    {
      return double.class;
    }
    return null;
  }

  private static Class<?> getClassFromContextClassLoader(String className)
  {
    try
    {
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      return contextClassLoader.loadClass(className);
    }
    catch (ClassNotFoundException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  private boolean areFieldsInitialized()
  {
    return localFields != null;
  }

  private synchronized Map<String, Field> getAllFieldsAsMap()
  {
    if (!areFieldsInitialized())
    {
      initializeAllFields();
    }

    return allFields;
  }

  private synchronized Map<String, Field> getLocalFieldsAsMap()
  {
    if (!areFieldsInitialized())
    {
      initializeAllFields();
    }
    return localFields;
  }

  private Map<String, Field> getSuperFieldsAsMap()
  {
    if (!areFieldsInitialized())
    {
      initializeAllFields();
    }
    return superFields;
  }

  private void initializeAllFields()
  {
    localFields = new LinkedHashMap<>();
    superFields = new LinkedHashMap<>();
    allFields = new LinkedHashMap<>();

    initialiseFields(aClass, localFields);

    Class<?> clazz = aClass.getSuperclass();
    while (clazz != null)
    {
      initialiseFields(clazz, superFields);
      clazz = clazz.getSuperclass();
    }

    allFields.putAll(superFields);
    allFields.putAll(localFields);
  }

  private void initialiseFields(Class<?> clazz, Map<String, Field> fields)
  {
    Field[] declaredFields = clazz.getDeclaredFields();

    for (Field field : declaredFields)
    {
      if (!fields.containsKey(field.getName()))
      {
        field.setAccessible(true);
        fields.put(field.getName(), field);
      }
    }
  }

  public String getPathForPackage()
  {
    String packageName = aClass.getPackage().getName();
    return String.format("%s/", packageName.replace('.', '/'));
  }

  public Field getField(String fieldName)
  {
    Field field = getAllFieldsAsMap().get(fieldName);
    if (field == null)
    {
      throw new SimulatorException(String.format("Could not find field with name [%s] on class [%s]", fieldName, aClass.getSimpleName()));
    }
    return field;
  }

  public Collection<String> getAllInstanceFieldNames()
  {
    List<String> result = new ArrayList<String>();

    for (Field field : getAllInstanceFields())
    {
      result.add(field.getName());
    }

    return result;
  }

  public String getPackageName()
  {
    return aClass.getPackage().getName();
  }

  public String getSimpleName()
  {
    return aClass.getSimpleName();
  }

  public Collection<String> getDeclaredFieldNames()
  {
    Collection<String> declaredFieldNames = new ArrayList<>();
    Field[] declaredFields = aClass.getDeclaredFields();

    for (Field declaredField : declaredFields)
    {
      declaredFieldNames.add(declaredField.getName());
    }

    return declaredFieldNames;
  }

  public List<Class<?>> getAllGenericTypesForField(String fieldName)
  {
    Field field = getField(fieldName);
    try
    {
      return toList(((ParameterizedType) field.getGenericType()).getActualTypeArguments());
    }
    catch (ClassCastException e)
    {
      throw new SimulatorException("Could not get generic type for %s.%s", aClass.getSimpleName(), fieldName);
    }
  }

  private List toList(Object[] actualTypeArguments)
  {
    List<Object> result = new ArrayList<Object>(actualTypeArguments.length);
    Collections.addAll(result, actualTypeArguments);
    return result;
  }

  public boolean isSubclassOf(Class<?> superClass)
  {
    return superClass.isAssignableFrom(aClass) && (aClass != superClass);
  }

  public boolean isDirectSubclassOf(Class<?> superclass)
  {
    return aClass.getSuperclass() == superclass;
  }

  public boolean isAnonymousOrInner()
  {
    return aClass.getName().contains("$");
  }

  public boolean isNotAnonymousOrInner()
  {
    return !isAnonymousOrInner();
  }

  public boolean isAbstract()
  {
    return Modifier.isAbstract(aClass.getModifiers());
  }

  public boolean isPrivate()
  {
    return Modifier.isPrivate(aClass.getModifiers());
  }

  public Object newInstance()
  {
    return newInstance(new Class<?>[0], new Object[0]);
  }

  public Object newInstance(Class<?>[] parameterTypes, Object[] arguments)
  {
    try
    {
      Constructor<?> constructor = aClass.getDeclaredConstructor(parameterTypes);
      constructor.setAccessible(true);
      return constructor.newInstance(arguments);
    }
    catch (Exception e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public Object bestGuessNewInstance(Object... arguments)
  {
    List<Constructor<?>> matchingConstructors = findAllMatchingConstructors(arguments);

    if (matchingConstructors.size() == 1)
    {
      Constructor<?> constructor = matchingConstructors.get(0);
      try
      {
        return newInstance(constructor.getParameterTypes(), arguments);
      }
      catch (Exception e)
      {
        throw new SimulatorException(e.getMessage());
      }
    }
    else if (matchingConstructors.isEmpty())
    {
      throw new SimulatorException("Could not find matching constructor");
    }
    else
    {
      throw new SimulatorException("Found more than one matching constructor");
    }
  }

  private List<Constructor<?>> findAllMatchingConstructors(Object... arguments)
  {
    List<Constructor<?>> matchingConstructors = new ArrayList<>();

    for (Constructor<?> constructor : aClass.getDeclaredConstructors())
    {
      if (doesConstructorMatch(constructor, arguments))
      {
        matchingConstructors.add(constructor);
      }
    }
    return matchingConstructors;
  }

  private boolean doesConstructorMatch(Constructor<?> constructor, Object... arguments)
  {
    Class<?>[] parameterTypes = constructor.getParameterTypes();

    if (parameterTypes.length == arguments.length)
    {
      for (int i = 0; i < parameterTypes.length; i++)
      {
        Object argument = arguments[i];
        if (argument != null)
        {
          if (!parameterTypes[i].isInstance(argument))
          {
            return false;
          }
        }
      }

      return true;
    }
    else
    {
      return false;
    }
  }

  public Object newUninitialisedInstance()
  {
    try
    {
      return getNoArgsConstructor().newInstance();
    }
    catch (Exception e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  private synchronized Constructor<?> getNoArgsConstructor()
  {
    if (specialNoArgConstructor == null)
    {
      specialNoArgConstructor = createNoArgsConstructor(aClass);
    }

    return specialNoArgConstructor;
  }

  Constructor<?> createNoArgsConstructor(Class<?> mappedClass)
  {
    try
    {
      Constructor<?> randomNoArgConstructor = Object.class.getDeclaredConstructor();
      ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
      Constructor<?> constructor = reflectionFactory.newConstructorForSerialization(mappedClass, randomNoArgConstructor);
      constructor.setAccessible(true);
      return constructor;
    }
    catch (NoSuchMethodException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public Collection<Method> getDeclaredMethods()
  {
    return toList(aClass.getDeclaredMethods());
  }

  private Constructor<?> _findConstructorOrNull(Class<?>[] parameterTypes)
  {
    try
    {
      return aClass.getDeclaredConstructor(parameterTypes);
    }
    catch (NoSuchMethodException e)
    {
      return null;
    }
  }

  public String getName()
  {
    return aClass.getName();
  }

  public <A extends Annotation> boolean hasAnnotation(Class<A> annotationClass)
  {
    return getAnnotationOrNull(annotationClass) != null;
  }

  public <A extends Annotation> A getAnnotationOrNull(Class<A> annotationClass)
  {
    return aClass.getAnnotation(annotationClass);
  }

  private void assertIsStatic(Method method, String methodName)
  {
    if (!Modifier.isStatic(method.getModifiers()))
    {
      throw new SimulatorException("Method %s.%s is not static", getSimpleName(), methodName);
    }
  }

  public Collection<Field> getAllFields()
  {
    return getAllFieldsAsMap().values();
  }

  public static boolean isStaticOrTransient(Field field)
  {
    return isStatic(field) || isTransient(field);
  }

  public static boolean isTransient(Field field)
  {
    return Modifier.isTransient(field.getModifiers());
  }

  public static boolean isStatic(Field field)
  {
    return Modifier.isStatic(field.getModifiers());
  }

  public Collection<Field> getAllInstanceFields()
  {
    List<Field> result = new ArrayList<Field>();

    for (Field field : getAllFieldsAsMap().values())
    {
      if (!isStatic(field))
      {
        result.add(field);
      }
    }

    return result;
  }

  public List<String> getFieldNames(Class<?> fieldClass)
  {
    List<String> result = new ArrayList<>();
    for (Field field : getAllFields())
    {
      if (fieldClass.isAssignableFrom(field.getType()))
      {
        result.add(field.getName());
      }
    }
    return result;
  }

  public boolean isPrimitiveOrWrapper()
  {
    return aClass.isPrimitive() ||
           aClass == Boolean.class ||
           aClass == Character.class ||
           aClass == Byte.class ||
           isSubclassOf(Number.class);
  }

  public List<Constructor<?>> getConstructors()
  {
    return Arrays.asList(aClass.getConstructors());
  }

  public Collection<Field> getPersistentLocalFields()
  {
    return findPersistentFields(getLocalFields());
  }

  private Collection<Field> getLocalFields()
  {
    return getLocalFieldsAsMap().values();
  }

  private Collection<Field> getSuperFields()
  {
    return getSuperFieldsAsMap().values();
  }

  public Collection<Field> getPersistentSuperFields()
  {
    return findPersistentFields(getSuperFields());
  }

  private Collection<Field> findPersistentFields(Collection<Field> fields)
  {
    List<Field> result = new ArrayList<Field>();
    for (Field field : fields)
    {
      if (!isStaticOrTransient(field))
      {
        result.add(field);
      }
    }
    sortFieldsByName(result);
    return result;
  }

  private void sortFieldsByName(List<Field> result)
  {
    result.sort(new Comparator<Field>()
    {
      public int compare(Field o1, Field o2)
      {
        return o1.getName().compareTo(o2.getName());
      }
    });
  }

  public Object invokeByTypes(String methodName)
  {
    return invokeByTypes(methodName, new Class[]{}, new Object[]{});
  }

  public Object invokeByTypes(String methodName, Class[] argTypes, Object[] args)
  {
    Method method = getMethod(methodName, argTypes);
    assertIsStatic(method, methodName);

    try
    {
      return method.invoke(aClass, args);
    }
    catch (Exception e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  private Map<MethodKey, Method> getAllMethodsMap()
  {
    if (_allMethodsMap == null)
    {
      _allMethodsMap = new HashMap<>();

      Class<?> clazz = aClass;
      while (clazz != null)
      {
        addToAllMethodsMap(clazz);

        clazz = clazz.getSuperclass();
      }
    }

    return _allMethodsMap;
  }

  private void addToAllMethodsMap(Class clazz)
  {
    Method[] declaredMethods = clazz.getDeclaredMethods();

    for (Method method : declaredMethods)
    {
      MethodKey methodKey = new MethodKey(method);
      if (!_allMethodsMap.containsKey(methodKey))
      {
        method.setAccessible(true);
        _allMethodsMap.put(methodKey, method);
      }
    }
  }

  protected Method getMethodOrNull(String methodName, Class<?>[] parameterTypes)
  {
    return getAllMethodsMap().get(new MethodKey(methodName, parameterTypes));
  }

  public Method getMethod(String methodName, Class<?>[] parameterTypes)
  {
    Method result = getMethodOrNull(methodName, parameterTypes);

    if (result != null)
    {
      return result;
    }
    else
    {
      throw new SimulatorException(String.format("ClassInspector could not find method with name [%s] and parameter types [%s] from class [%s]", methodName, Arrays.toString(parameterTypes), aClass.getSimpleName()));
    }
  }
}

