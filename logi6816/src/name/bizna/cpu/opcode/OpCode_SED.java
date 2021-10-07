package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_SED
    extends OpCode
{
  public OpCode_SED(int mCode, InstructionCycles cycles)
  {
    super("SED", "Set Decimal Mode", mCode, cycles);
  }
}

