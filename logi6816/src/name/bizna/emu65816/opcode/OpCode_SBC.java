package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_SBC
    extends OpCode
{
  public OpCode_SBC(int mCode, InstructionCycles cycles)
  {
    super("SBC", "Subtract memory and carry from A; result in A and update NZC.", mCode, cycles);
  }
}

