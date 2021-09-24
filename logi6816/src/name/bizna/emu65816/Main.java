package name.bizna.emu65816;

import java.io.*;

import static name.bizna.emu65816.util.FileUtil.readBytes;

public class Main
{
  public static void main(String[] args)
  {
    EmulationModeInterrupts emi = new EmulationModeInterrupts(0xfff4,
                                                              0xfff6,
                                                              0xfff8,
                                                              0xfffa,
                                                              0xfffc,
                                                              0xfffe);
    NativeModeInterrupts nmi = new NativeModeInterrupts(0xffe4,
                                                        0xffe6,
                                                        0xffe8,
                                                        0xffea,
                                                        0xfffc,
                                                        0xffee);
    SystemBus systemBus = new SystemBus();

    MemoryDevice memory = new MemoryDevice(readBytes(new File("../Test816/Test816.bin")));

    systemBus.registerDevice(memory);

    Cpu65816 cpu = new Cpu65816(systemBus, emi, nmi);
    cpu.setRESPin(false);

    int count = 1024;
    while (!cpu.isStopped() && count > 0)
    {
      cpu.executeNextInstruction();
      count--;
    }

    byte[] progress = memory.get(0, 18);
    String s = new String(progress);
    System.out.println(s);
  }
}

