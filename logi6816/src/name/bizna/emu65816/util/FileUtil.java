package name.bizna.emu65816.util;

import java.io.*;

public class FileUtil
{
  private static final int EOF = -1;

  public static BufferedInputStream openInputStream(File file)
  {
    try
    {
      return new BufferedInputStream(new FileInputStream(file));
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
      throws IOException
  {
    long count = 0;
    int n;
    while (EOF != (n = input.read(buffer)))
    {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  public static int copy(final InputStream input, final OutputStream output) throws IOException
  {
    final long count = copyLarge(input, output, new byte[4 * 1024]);
    if (count > Integer.MAX_VALUE)
    {
      return -1;
    }
    return (int) count;
  }

  public static byte[] toByteArray(final InputStream input) throws IOException
  {
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    int copy = copy(input, output);
    return output.toByteArray();
  }

  public static byte[] readBytes(File file)
  {
    try
    {
      BufferedInputStream inputStream = openInputStream(file);
      return toByteArray(inputStream);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
