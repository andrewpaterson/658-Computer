package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_RTS
    extends OpCode
{
  public OpCode_RTS(int mCode, InstructionCycles cycles)
  {
    super("RTS", "Return from Subroutine", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address returnAddress = new Address(cpu.getProgramCounter().getBank(), toShort (cpu.pull16Bit() + 1));
    cpu.setProgramAddress(returnAddress);
    cpu.addToCycles(6);
  }
}

