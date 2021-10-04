package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_ASL
    extends OpCode
{
  public OpCode_ASL(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift memory left 1 bit; result in memory and update NZC.", mCode, busCycles);
  }
}

