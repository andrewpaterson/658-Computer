package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_JML
    extends OpCode
{
  public OpCode_JML(int mCode, InstructionCycles cycles)
  {
    super("JML", "Jump Long to New Location", mCode, cycles);
  }
}

