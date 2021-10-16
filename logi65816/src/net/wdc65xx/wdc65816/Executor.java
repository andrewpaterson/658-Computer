package net.wdc65xx.wdc65816;

import net.util.EmulatorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;

public interface Executor<T>
    extends Consumer<T>,
            Serializable

{
  static String getMethodName(Executor<WDC65C816> getter)
  {
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(getter);
      oos.flush();
      String s = bos.toString();
      String packageName = WDC65C816.class.getName().replace('.', '/');
      int start = s.indexOf(packageName);
      int end = s.indexOf(packageName, start + 1);
      s = s.substring(start + packageName.length(), end);
      String delimiter = "t\u0000\u0003";
      start = s.indexOf(delimiter);
      end = s.indexOf(delimiter, start + 1);
      return s.substring(start + delimiter.length(), end);
    }
    catch (IOException e)
    {
      throw new EmulatorException(e.getMessage());
    }
  }
}

