package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXWriteCycles
    extends InstructionCycles
{
  //16a
  public DirectIndexedWithXWriteCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), X()), E(operation), Write_DataLow(), DONE8Bit(width)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Write_DataHigh(), DONE16Bit(width)));
  }
}
