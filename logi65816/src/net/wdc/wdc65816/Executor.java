package net.wdc.wdc65816;

import net.util.EmulatorException;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;

public interface Executor<T>
    extends Consumer<T>,
            Serializable

{
  static String getMethodName(Executor<W65C816> getter)
  {
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(getter);
      oos.flush();
      String s = bos.toString();
      String packageName = W65C816.class.getName().replace('.', '/');
      int start = s.indexOf(packageName);
      int end = s.indexOf(packageName, start + 1);
      s = s.substring(start + packageName.length(), end);
      String delimiter = "t\u0000\u0003";
      start = getIndexOfDelimiter(s);
      s = s.substring(start + delimiter.length());
      end = getIndexOfDelimiter(s);
      return s.substring(0, end);
    }
    catch (Exception e)
    {
      throw new EmulatorException(e.getMessage());
    }
  }

  static int getIndexOfDelimiter(String s)
  {
    int index3 = s.indexOf("t\u0000\u0003");
    int index5 = s.indexOf("t\u0000\u0005");
    if (index3 == -1 && index5 != -1)
    {
      return index5;
    }
    else if (index3 != -1 && index5 == -1)
    {
      return index3;
    }
    else if (index3 == -1)
    {
      return 0;
    }
    else if (index3 < index5)
    {
      return index3;
    }
    else
    {
      return index5;
    }
  }
}

