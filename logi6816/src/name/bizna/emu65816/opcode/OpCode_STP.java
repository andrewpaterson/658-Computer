package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_STP
    extends OpCode
{
  public OpCode_STP(int mCode, InstructionCycles cycles)
  {
    super("STP", "Stop the Clock", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.stop();
    cpu.addToProgramAddress(1);
    cpu.addToCycles(3);
  }
}

