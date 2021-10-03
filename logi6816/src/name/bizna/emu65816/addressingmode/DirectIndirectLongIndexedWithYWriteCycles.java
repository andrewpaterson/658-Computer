package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.DirectIndirectLongIndexedWithY;

public class DirectIndirectLongIndexedWithYWriteCycles
    extends InstructionCycles
{
  //14
  public DirectIndirectLongIndexedWithYWriteCycles(Consumer<Cpu65816> operation)
  {
    super(DirectIndirectLongIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DP(), D0(), o(2)), Read_AAB()),
          new BusCycle(Address(AAB(), AA(), Y()), E(operation), Write_DataLow(), DONE8Bit(Width.A)),
          new BusCycle(Address(AAB(), AA(), Y(), o(1)), Write_DataHigh(), DONE16Bit(Width.A)));
  }
}

