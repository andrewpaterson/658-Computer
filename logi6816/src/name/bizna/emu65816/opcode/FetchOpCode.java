package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class FetchOpCode
    extends OpCode
{
  public FetchOpCode(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    if (cycle == 0)
    {
      cpu.readOpCode(cpu.getProgramCounter());
    }
    else
    {
      invalidCycle();
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    if (cycle == 0)
    {
      cpu.setOpCode(cpu.getPinData());
      cpu.incrementProgramAddress();
    }
    else
    {
      invalidCycle();
    }
  }
}

