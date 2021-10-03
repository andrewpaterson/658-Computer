package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXCycles
    extends InstructionCycles
{
  //16a
  public DirectIndexedWithXCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), X()), Read_DataLow(), E8Bit(operation), DONE8Bit(width)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(width)));
  }
}

