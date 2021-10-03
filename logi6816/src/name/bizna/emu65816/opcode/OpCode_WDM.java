package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_WDM
    extends OpCode
{
  public OpCode_WDM(int mCode, InstructionCycles cycles)
  {
    super("WDM", "Reserved for future use", mCode, cycles);
  }
}

