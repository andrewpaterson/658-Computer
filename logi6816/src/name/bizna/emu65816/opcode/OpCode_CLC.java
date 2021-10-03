package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_CLC
    extends OpCode
{
  public OpCode_CLC(int mCode, InstructionCycles cycles)
  {
    super("CLC", "Clear Carry Flag", mCode, cycles);
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
}

