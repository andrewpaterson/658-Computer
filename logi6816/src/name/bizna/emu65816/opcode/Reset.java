package name.bizna.emu65816.opcode;

import name.bizna.emu65816.*;

public class Reset
    extends OpCode
{
  public Reset(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    if (cycle == 1)
    {
      if (!clock)
      {
        CpuStatus cpuStatus = cpu.getCpuStatus();
        cpuStatus.setEmulationFlag(true);
        cpuStatus.setAccumulatorWidthFlag(true);
        cpuStatus.setIndexWidthFlag(true);
        cpu.readAddress(new Address(cpu.getEmulationInterrupts().reset));
      }
      else
      {
        cpu.setProgramAddressBank(0);
        cpu.setProgramAddressLow(cpu.getData());
        cpu.nextCycle();
      }
    }
    else if (cycle == 2)
    {
      if (!clock)
      {
        cpu.readAddress(new Address(cpu.getEmulationInterrupts().reset + 1));
      }
      else
      {
        cpu.setProgramAddressHigh(cpu.getData());
        cpu.doneInstruction();
      }
    }
  }
}

