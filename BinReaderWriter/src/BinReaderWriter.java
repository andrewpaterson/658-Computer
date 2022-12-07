import java.io.File;
import java.io.FileInputStream;

public class BinReaderWriter
{
  public static void main(String[] args)
  {
    File file = new File("C:\\Work\\658-Computer\\Test816\\Test816.bin");

    byte[] bytes = new byte[(int) file.length()];

    try
    {
      FileInputStream fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytes);
      fileInputStream.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return;
    }

    int minusOneCount = 0;
    for (int i = 0; i < bytes.length; i++)
    {
      int aByte = bytes[i];
      if ((aByte != -1) || (minusOneCount != 0))
      {
        if ((aByte != -1))
        {
          minusOneCount = 2;
          if (aByte < 0)
          {
            aByte = 256 + aByte;
          }
        }
        else
        {
          aByte = 256 + aByte;
          minusOneCount--;
        }
        System.out.println("\t write(pcMaster, 0x" + StringUtil.to16BitHex(i).toUpperCase() + ", 0x" + StringUtil.to8BitHex(aByte).toUpperCase() + ");");
      }
    }
  }
}

