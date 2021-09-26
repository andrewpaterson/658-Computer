package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_CLC
    extends OpCode
{
  public OpCode_CLC(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    if (cycle == 1)
    {
      cpu.getCpuStatus().setCarryFlag(false);
      cpu.noAddress();
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
    if (cycle == 1)
    {
      cpu.doneInstruction();
    }
    else
    {
      invalidCycle();
    }
  }
}

