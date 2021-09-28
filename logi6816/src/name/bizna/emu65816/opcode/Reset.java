package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.CpuStatus;

public class Reset
    extends OpCode
{
  public Reset(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();

    switch (cycle)
    {
      case 1:
        CpuStatus cpuStatus = cpu.getCpuStatus();
        cpuStatus.setEmulationFlag(true);
        cpuStatus.setAccumulatorWidthFlag(true);
        cpuStatus.setIndexWidthFlag(true);
        cpu.readProgram(new Address(0x00, cpu.getEmulationInterrupts().reset));
        break;
      case 2:
        cpu.readProgram(new Address(0x00, cpu.getEmulationInterrupts().reset + 1));
        break;
      default:
        invalidCycle();
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    switch (cycle)
    {
      case 1:
        cpu.setProgramAddressBank(0);
        cpu.setProgramAddressLow(cpu.getPinData());
        break;
      case 2:
        cpu.setProgramAddressHigh(cpu.getPinData());
        cpu.doneInstruction();
        break;
      default:
        invalidCycle();
    }
  }
}

