package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.DirectIndirectLong;

public class DirectIndirectLongCycles
    extends InstructionCycles
{
  //15
  public DirectIndirectLongCycles(Consumer<Cpu65816> operation)
  {
    super(DirectIndirectLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DP(), D0(), o(2)), Read_AAB()),
          new BusCycle(Address(AAB(), AA()), Read_DataLow(), E8Bit(operation), DONE8Bit(Width.A)),
          new BusCycle(Address(AAB(), AA(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(Width.A)));
  }
}
