package name.bizna.cpu;

import name.bizna.bus.memory.Memory;

import java.io.File;

import static name.bizna.util.FileUtil.readBytes;

public class Main
{
  public static void main(String[] args)
  {
    Memory memory = new Memory(readBytes(new File("../Test816/Test816.bin")));

    BusPins65816 pins = new BusPins65816();
    Cpu65816 cpu = new Cpu65816(pins);

    int count = 1024;
    boolean clock = true;
    while (!cpu.isStopped() && count > 0)
    {
      pins.setPhi2(clock);

      cpu.tick();

      cpu.dump();

//      if (!clock)
//      {
//        if (pins.isValidDataAddress() || pins.isValidProgramAddress())
//        {
//          Address address = cpu.getAddress();  //This should probably use pins not cpu.
//          if (pins.isRead())
//          {
//            pins.setData(memory.readByte(address));
//          }
//          else
//          {
//            memory.writeByte(address, pins.getData());
//          }
//        }
//      }

      clock = !clock;
      count--;
    }

    byte[] progress = memory.get(0, 18);
    String s = new String(progress);
    System.out.println(s);
  }
}

