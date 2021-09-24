package name.bizna.emu65816;

import java.io.File;

import static name.bizna.emu65816.util.FileUtil.readBytes;

public class Main
{
  public static void main(String[] args)
  {
    SystemBus systemBus = new SystemBus();

    MemoryDevice memory = new MemoryDevice(readBytes(new File("../Test816/Test816.bin")));

    systemBus.registerDevice(memory);

    Pins pins = new Pins();
    Cpu65816 cpu = new Cpu65816(systemBus, pins);

    int count = 1024;
    boolean clock = true;
    while (!cpu.isStopped() && count > 0)
    {
      cpu.tick(clock);
      clock = !clock;
//      cpu.executeNextInstruction();
      count--;
    }

    byte[] progress = memory.get(0, 18);
    String s = new String(progress);
    System.out.println(s);
  }
}

