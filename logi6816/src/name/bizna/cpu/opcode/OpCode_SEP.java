package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_SEP
    extends OpCode
{
  public OpCode_SEP(int mCode, InstructionCycles cycles)
  {
    super("SEP", "Set Processor Status Bit", mCode, cycles);
  }
}

